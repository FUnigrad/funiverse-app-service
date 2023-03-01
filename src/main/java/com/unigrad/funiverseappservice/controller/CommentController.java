package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.dto.CommentDTO;
import com.unigrad.funiverseappservice.entity.socialnetwork.Comment;
import com.unigrad.funiverseappservice.service.ICommentService;
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

import java.util.Optional;

@RestController
@RequestMapping("comment")
public class CommentController {

    private final ICommentService commentService;

    private final ModelMapper modelMapper;

    public CommentController(ICommentService commentService, ModelMapper modelMapper) {
        this.commentService = commentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping()
    public ResponseEntity<Comment> create(@RequestBody CommentDTO newComment) {

        commentService.addFromDTO(newComment);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getById(@PathVariable Long id) {

        return commentService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> update(@RequestBody String content, @PathVariable Long id) {
        //if user id is owner of post then oke to update - todo

        return commentService.get(id)
                .map(comment -> {
                    comment.setContent(content);
                    return ResponseEntity.ok(modelMapper.map(commentService.save(comment), CommentDTO.class));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        if (commentService.isExist(id)) {

            commentService.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
