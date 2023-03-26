package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.payload.DTO.CommentDTO;
import com.unigrad.funiverseappservice.payload.DTO.PostDTO;
import com.unigrad.funiverseappservice.entity.socialnetwork.Post;
import com.unigrad.funiverseappservice.service.ICommentService;
import com.unigrad.funiverseappservice.service.IGroupService;
import com.unigrad.funiverseappservice.service.IPostService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import com.unigrad.funiverseappservice.util.DTOConverter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("post")
@RequiredArgsConstructor
public class PostController {

    private final IPostService postService;

    private final ICommentService commentService;

    private final ModelMapper modelMapper;

    private final IUserDetailService userDetailService;

    private final IGroupService groupService;

    private final DTOConverter dtoConverter;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody PostDTO newPost) {
        Optional<UserDetail> userDetailOptional = userDetailService.get(newPost.getOwnerId());
        Optional<Group> groupOptional = groupService.get(newPost.getGroupId());

        if (userDetailOptional.isPresent() && groupOptional.isPresent()) {
            Post post = Post.builder()
                    .content(newPost.getContent())
                    .group(groupOptional.get())
                    .owner(userDetailOptional.get())
                    .createdDateTime(LocalDateTime.now())
                    .build();
            postService.save(post);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@RequestBody String content, @PathVariable Long id) {

        Optional<Post> postOptional = postService.get(id);

        return postOptional
                .map(post -> {
                    post.setContent(content);
                    return ResponseEntity.ok(postService.save(post));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        if (postService.isExist(id)) {
            postService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable Long id) {

        return postService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/{pid}/comments")
    public ResponseEntity<List<CommentDTO>> getAllCommentsInPost(@PathVariable Long pid) {

        List<CommentDTO> commentDTOList = Arrays.stream(dtoConverter.convert(commentService.getAllCommentsInPost(pid), CommentDTO[].class)).toList();

        return ResponseEntity.ok(commentDTOList);
    }
}