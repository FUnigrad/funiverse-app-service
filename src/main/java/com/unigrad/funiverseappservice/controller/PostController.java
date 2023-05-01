package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.socialnetwork.Comment;
import com.unigrad.funiverseappservice.entity.socialnetwork.Event;
import com.unigrad.funiverseappservice.entity.socialnetwork.Post;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.payload.DTO.CommentDTO;
import com.unigrad.funiverseappservice.payload.request.UpdateContentRequest;
import com.unigrad.funiverseappservice.service.ICommentService;
import com.unigrad.funiverseappservice.service.IEventService;
import com.unigrad.funiverseappservice.service.IPostService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import com.unigrad.funiverseappservice.service.impl.EmitterService;
import com.unigrad.funiverseappservice.util.DTOConverter;
import com.unigrad.funiverseappservice.util.HTMLDecode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final IEventService eventService;

    private final EmitterService emitterService;

    private final DTOConverter dtoConverter;

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@RequestBody UpdateContentRequest content, @PathVariable Long id) {

        Optional<Post> postOptional = postService.get(id);

        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDetail currentUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!postOptional.get().getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Current user don't have permission to edit this post");
        }

        List<Long> mentionUserIds = HTMLDecode.extractUser(content.getContent());

        mentionUserIds
                .forEach(userId -> {
                    Optional<UserDetail> userDetail = userDetailService.get(userId);

                    if (userDetail.isPresent() && !userId.equals(currentUser.getId())) {
                        Event event = Event.builder()
                                .actor(postOptional.get().getOwner())
                                .receiver(userDetail.get())
                                .type(Event.Type.MENTION)
                                .sourceId(id)
                                .sourceType(Event.SourceType.POST)
                                .group(postOptional.get().getGroup())
                                .createdTime(LocalDateTime.now())
                                .build();

//                        emitterService.pushNotification(eventService.save(event));
                    }
                });

        postOptional.get().setContent(content.getContent());

        return ResponseEntity.ok(postService.save(postOptional.get()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        Optional<Post> postOptional = postService.get(id);

        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDetail currentUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!postOptional.get().getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Current user don't have permission to delete this post");
        }

        postService.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable Long id) {

        return postService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/{pid}/comment")
    public ResponseEntity<List<CommentDTO>> getAllCommentsInPost(@PathVariable Long pid) {

        List<CommentDTO> commentDTOList = Arrays.stream(dtoConverter.convert(commentService.getAllCommentsInPost(pid), CommentDTO[].class)).toList();

        return ResponseEntity.ok(commentDTOList);
    }

    @PostMapping("{id}/comment")
    public ResponseEntity<CommentDTO> createComment(@RequestBody Comment newComment, @PathVariable Long id) {
        Optional<Post> postOptional = postService.get(id);

        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        newComment.setOwner(userDetail);
        newComment.setPost(postOptional.get());

        Comment comment = commentService.save(newComment);

        List<Long> mentionUserIds = HTMLDecode.extractUser(newComment.getContent());

        mentionUserIds
                .forEach(userId -> {
                    Optional<UserDetail> user = userDetailService.get(userId);

                    if (user.isPresent() && !userId.equals(userDetail.getId())) {
                        Event event = Event.builder()
                                .actor(comment.getOwner())
                                .receiver(user.get())
                                .type(Event.Type.MENTION)
                                .sourceId(comment.getId())
                                .sourceType(Event.SourceType.POST)
                                .group(postOptional.get().getGroup())
                                .createdTime(LocalDateTime.now())
                                .build();

//                        emitterService.pushNotification(eventService.save(event));
                    }
                });

        return ResponseEntity.ok().body(dtoConverter.convert(comment, CommentDTO.class));
    }
}