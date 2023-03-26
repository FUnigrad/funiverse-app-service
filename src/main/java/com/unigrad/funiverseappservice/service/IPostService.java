package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.socialnetwork.Post;

import java.util.List;

public interface IPostService extends IBaseService<Post, Long> {

    void deleteById(Long id);

    List<Post> getAllPostInGroup(Long groupId);
}