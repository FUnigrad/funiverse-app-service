package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.payload.PostDTO;
import com.unigrad.funiverseappservice.entity.socialnetwork.Post;

import java.util.List;

public interface IPostService extends IBaseService<Post, Long> {

    void deleteById(Long id);

    Post addNewFromDTO(PostDTO postDTO);

    List<Post> getAllPostInGroup(Long groupId);
}