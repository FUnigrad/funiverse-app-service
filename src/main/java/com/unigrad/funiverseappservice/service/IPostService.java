package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.dto.PostDTO;
import com.unigrad.funiverseappservice.entity.socialnetwork.Post;

import java.util.List;

public interface IPostService extends IBaseService<Post, Long> {

    void deletePostById(Long id);

    Post addNewPost(PostDTO postDTO);

    List<Post> getAllPostInGroup(Long groupId);
}
