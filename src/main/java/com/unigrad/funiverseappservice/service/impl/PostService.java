package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.socialnetwork.Post;
import com.unigrad.funiverseappservice.payload.DTO.CommentDTO;
import com.unigrad.funiverseappservice.payload.DTO.PostDTO;
import com.unigrad.funiverseappservice.repository.IPostRepository;
import com.unigrad.funiverseappservice.service.IGroupMemberService;
import com.unigrad.funiverseappservice.service.IPostService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import com.unigrad.funiverseappservice.util.DTOConverter;
import com.unigrad.funiverseappservice.util.PageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService implements IPostService {

    private final IPostRepository postRepository;

    private final PageConverter pageConverter;

    private final IGroupMemberService groupMemberService;

    private final DTOConverter dtoConverter;

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
    public List<Post> saveAll(List<Post> entities) {
        return null;
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
        return postRepository.findAll(specification);
    }

    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> getAllPostInGroup(Long groupId) {
        return postRepository.getAllByGroup_Id(groupId);
    }

    @Override
    public Page<PostDTO> getAllPostForNewFeed(Long userId, Pageable pageable) {
        List<Post> posts = postRepository.findAll();
        List<PostDTO> result = new ArrayList<>();

        for (Post post : posts) {
            if ((!post.getGroup().isPrivate() || groupMemberService.isGroupMember(userId, post.getGroup().getId()))
            && post.getGroup().isPublish() && post.getGroup().isActive()) {
                result.add(dtoConverter.convert(post, PostDTO.class));
            }
        }

        result.sort(Comparator.comparing(PostDTO::getCreatedDateTime));

        Collections.reverse(result);

        result.forEach(
                post -> post.getComments().sort(Comparator.comparing(CommentDTO::getCreatedDateTime))
        );

        return pageConverter.convert(result, pageable);
    }

    @Override
    public List<Post> getAllContainContentForUser(Long userId, String content) {
        return postRepository.getAllPostContainContentForUser(userId, content);
    }

    @Override
    public List<Post> getAllInGroupWhoseContentContain(Long groupId, String content) {
        return postRepository.getAllPostInGroupWhoseContentContain(groupId, content);
    }
}