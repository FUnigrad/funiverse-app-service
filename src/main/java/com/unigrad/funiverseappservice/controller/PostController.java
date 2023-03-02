package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.payload.CommentDTO;
import com.unigrad.funiverseappservice.payload.PostDTO;
import com.unigrad.funiverseappservice.entity.socialnetwork.Post;
import com.unigrad.funiverseappservice.service.ICommentService;
import com.unigrad.funiverseappservice.service.IPostService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("post")
public class PostController {

    private final IPostService postService;

    private final ICommentService commentService;

    private final ModelMapper modelMapper;

    public PostController(IPostService postService, ICommentService commentService, ModelMapper modelMapper) {
        this.postService = postService;
        this.commentService = commentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody PostDTO newPost) {

        postService.addNewFromDTO(newPost);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@RequestBody String content, @PathVariable Long id) {

        Optional<Post> post = postService.get(id);
        post.get().setContent(content);

        return postService.isExist(id)
                ? ResponseEntity.ok(postService.save(post.get()))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        if (postService.isExist(id)) {
            postService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{pid}")
    public ResponseEntity<Post> getById(@PathVariable Long pid) {

        return postService.get(pid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/{pid}/comments")
    public ResponseEntity<List<CommentDTO>> getAllCommentsInPost(@PathVariable Long pid) {

        List<CommentDTO> commentDTOList = Arrays.asList(modelMapper.map(commentService.getAllCommentsInPost(pid), CommentDTO[].class));

        return ResponseEntity.ok(commentDTOList);
    }
}