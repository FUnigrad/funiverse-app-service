package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.socialnetwork.Post;
import com.unigrad.funiverseappservice.payload.DTO.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPostService extends IBaseService<Post, Long> {

    void deleteById(Long id);

    List<Post> getAllPostInGroup(Long groupId);

    Page<PostDTO> getAllPostForNewFeed(Long userId, Pageable pageable);
}