package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.payload.CommentDTO;
import com.unigrad.funiverseappservice.entity.socialnetwork.Comment;
import com.unigrad.funiverseappservice.entity.socialnetwork.Post;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.repository.ICommentRepository;
import com.unigrad.funiverseappservice.service.ICommentService;
import com.unigrad.funiverseappservice.service.IPostService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService implements ICommentService {

    private final ICommentRepository commentRepository;

    private final IUserDetailService userDetailService;

    private final IPostService postService;


    public CommentService(ICommentRepository commentRepository, IUserDetailService userDetailService, IPostService postService) {
        this.commentRepository = commentRepository;
        this.userDetailService = userDetailService;
        this.postService = postService;
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Comment addFromDTO(CommentDTO commentDTO) {

        Optional<UserDetail> userDetail = userDetailService.get(commentDTO.getOwnerId());
        Optional<Post> post = postService.get(commentDTO.getPostId());
        LocalDateTime date = LocalDateTime.now();
        Comment comment = new Comment();

        comment.setOwner(userDetail.get());
        comment.setPost(post.get());
        comment.setContent(commentDTO.getContent());
        comment.setCreatedDateTime(date);

        commentRepository.save(comment);
        return comment;
    }

    @Override
    public List<Comment> getAllCommentsInPost(Long pid) {
        return commentRepository.getAllByPost_Id(pid);
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> getAllActive() {
        return null;
    }

    @Override
    public Optional<Comment> get(Long key) {
        return commentRepository.findById(key);
    }

    @Override
    public Comment save(Comment entity) {

        LocalDateTime date = LocalDateTime.now();
        entity.setCreatedDateTime(date);

        return commentRepository.save(entity);
    }

    @Override
    public void activate(Long key) {

    }

    @Override
    public void inactivate(Long key) {

    }

    @Override
    public boolean isExist(Long key) {
        return commentRepository.existsById(key);
    }

    @Override
    public List<Comment> search(EntitySpecification<Comment> specification) {
        return null;
    }


}