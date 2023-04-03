package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.socialnetwork.Comment;
import com.unigrad.funiverseappservice.entity.socialnetwork.Event;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.payload.request.UpdateContentRequest;
import com.unigrad.funiverseappservice.service.ICommentService;
import com.unigrad.funiverseappservice.service.IEventService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import com.unigrad.funiverseappservice.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;

    private final IUserDetailService userDetailService;

    private final IEventService eventService;

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getById(@PathVariable Long id) {

        return commentService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> update(@RequestBody UpdateContentRequest contentRequest, @PathVariable Long id) {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Comment> commentOptional = commentService.get(id);

        return commentOptional
                .map(comment -> {
                    if (comment.getOwner().getId().equals(userDetail.getId())) {
                        comment.setContent(contentRequest.getContent());
                        commentService.save(comment);

                        List<Long> mentionUserIds = Utils.extractUserFromContent(contentRequest.getContent());

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
                                                .group(comment.getPost().getGroup())
                                                .createdTime(LocalDateTime.now())
                                                .build();

                                        eventService.save(event);
                                    }
                                });

                        return ResponseEntity.ok(comment);
                    } else {
                        throw new AccessDeniedException("You don't have permission to edit this comment");
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Comment> commentOptional = commentService.get(id);

        if (commentOptional.isPresent()) {
            if (commentOptional.get().getOwner().getId().equals(userDetail.getId())) {
                commentService.deleteById(id);
                return ResponseEntity.ok().build();
            } else {
                throw new AccessDeniedException("You don't have permission to delete this comment");
            }
        }

        return ResponseEntity.notFound().build();
    }
}