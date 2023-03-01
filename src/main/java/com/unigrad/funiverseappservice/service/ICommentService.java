package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.dto.CommentDTO;
import com.unigrad.funiverseappservice.entity.socialnetwork.Comment;

import java.util.List;

public interface ICommentService extends IBaseService<Comment, Long> {

    void deleteById(Long id);

    Comment addFromDTO(CommentDTO newComment);

    List<Comment> getAllCommentsInPost(Long pid);
}
