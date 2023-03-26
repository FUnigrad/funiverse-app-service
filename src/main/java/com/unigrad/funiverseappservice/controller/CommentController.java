package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.socialnetwork.Comment;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.service.ICommentService;
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

import java.util.Optional;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

    private final ICommentService commentService;

    @PostMapping()
    public ResponseEntity<Comment> create(@RequestBody Comment newComment) {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication();

        newComment.setOwner(userDetail);

        return ResponseEntity.ok().body(commentService.save(newComment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getById(@PathVariable Long id) {

        return commentService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> update(@RequestBody String content, @PathVariable Long id) {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication();

        Optional<Comment> commentOptional = commentService.get(id);

        return commentOptional
                .map(comment -> {
                    if (comment.getOwner().getId().equals(userDetail.getId())) {
                        comment.setContent(content);
                        commentService.save(comment);

                        return ResponseEntity.ok(comment);
                    } else {
                        throw new AccessDeniedException("You don't have permission to edit this comment");
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication();

        Optional<Comment> commentOptional = commentService.get(id);

        if (commentOptional.isPresent()) {
            if (commentOptional.get().getOwner().getId().equals(userDetail.getId())) {
                commentService.deleteById(id);
                return ResponseEntity.ok().build();
            } else {
                throw new AccessDeniedException("You don't have permission to edit this comment");
            }
        }

        return ResponseEntity.notFound().build();
    }
}