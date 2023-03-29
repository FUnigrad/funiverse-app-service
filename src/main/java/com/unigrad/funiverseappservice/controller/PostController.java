package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.socialnetwork.Event;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.payload.DTO.CommentDTO;
import com.unigrad.funiverseappservice.payload.DTO.PostDTO;
import com.unigrad.funiverseappservice.entity.socialnetwork.Post;
import com.unigrad.funiverseappservice.service.ICommentService;
import com.unigrad.funiverseappservice.service.IEventService;
import com.unigrad.funiverseappservice.service.IGroupMemberService;
import com.unigrad.funiverseappservice.service.IGroupService;
import com.unigrad.funiverseappservice.service.IPostService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import com.unigrad.funiverseappservice.util.DTOConverter;
import com.unigrad.funiverseappservice.util.Utils;
import lombok.RequiredArgsConstructor;
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

    private final IUserDetailService userDetailService;

    private final IGroupService groupService;

    private final IEventService eventService;

    private final IGroupMemberService groupMemberService;

    private final DTOConverter dtoConverter;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody PostDTO newPost) {
        Optional<UserDetail> ownerOptional = userDetailService.get(newPost.getOwnerId());
        Optional<Group> groupOptional = groupService.get(newPost.getGroupId());

        if (ownerOptional.isPresent() && groupOptional.isPresent()) {

            Post post = postService.save(Post.builder()
                    .content(newPost.getContent())
                    .group(groupOptional.get())
                    .owner(ownerOptional.get())
                    .createdDateTime(LocalDateTime.now())
                    .build()
            );

            // event for user who are mentioned
            List<Long> mentionUserIds = Utils.extractUserFromContent(newPost.getContent());

            mentionUserIds
                    .forEach(userId -> {
                        Optional<UserDetail> userDetail = userDetailService.get(userId);

                        if (userDetail.isPresent()) {
                            Event event = Event.builder()
                                    .actor(ownerOptional.get())
                                    .receiver(userDetail.get())
                                    .type(Event.Type.MENTION)
                                    .sourceId(post.getId())
                                    .sourceType(Event.SourceType.POST)
                                    .createdTime(LocalDateTime.now())
                                    .build();

                            eventService.save(event);
                        }
                    });

            // event for all users in group
            List<UserDetail> members = groupMemberService.getAllUsersInGroup(groupOptional.get().getId());

            members
                    .forEach(user -> {
                        Event event = Event.builder()
                                .actor(ownerOptional.get())
                                .receiver(user)
                                .type(Event.Type.MENTION)
                                .sourceId(post.getId())
                                .sourceType(Event.SourceType.POST)
                                .createdTime(LocalDateTime.now())
                                .build();

                        eventService.save(event);
                    });

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@RequestBody String content, @PathVariable Long id) {

        Optional<Post> postOptional = postService.get(id);

        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Long> mentionUserIds = Utils.extractUserFromContent(content);

        mentionUserIds
                .forEach(userId -> {
                    Optional<UserDetail> userDetail = userDetailService.get(userId);

                    if (userDetail.isPresent()) {
                        Event event = Event.builder()
                                .actor(postOptional.get().getOwner())
                                .receiver(userDetail.get())
                                .type(Event.Type.MENTION)
                                .sourceId(id)
                                .sourceType(Event.SourceType.POST)
                                .createdTime(LocalDateTime.now())
                                .build();

                        eventService.save(event);
                    }
                });

        postOptional.get().setContent(content);

        return ResponseEntity.ok(postService.save(postOptional.get()));
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