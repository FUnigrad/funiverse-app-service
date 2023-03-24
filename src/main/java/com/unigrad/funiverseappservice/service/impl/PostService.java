package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.payload.PostDTO;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.Post;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.repository.IPostRepository;
import com.unigrad.funiverseappservice.service.IGroupService;
import com.unigrad.funiverseappservice.service.IPostService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService {

    private final IPostRepository postRepository;

    private final IUserDetailService userDetailService;

    private final IGroupService groupService;

    public PostService(IPostRepository postRepository, IUserDetailService userDetailService, IGroupService groupService) {
        this.postRepository = postRepository;
        this.userDetailService = userDetailService;
        this.groupService = groupService;
    }

    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getAllActive() {
        return null;
    }

    @Override
    public Optional<Post> get(Long key) {
        return postRepository.findById(key);
    }

    @Override
    public Post save(Post entity) {
        return postRepository.save(entity);
    }

    @Override
    public void activate(Long key) {

    }

    @Override
    public void inactivate(Long key) {

    }

    @Override
    public boolean isExist(Long key) {
        return postRepository.existsById(key);
    }

    @Override
    public List<Post> search(EntitySpecification<Post> specification) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> getAllPostInGroup(Long groupId) {
        return postRepository.getAllByGroup_Id(groupId);
    }
}