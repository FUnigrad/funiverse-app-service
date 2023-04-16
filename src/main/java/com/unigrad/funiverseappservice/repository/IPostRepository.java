package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.socialnetwork.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    void deleteById(Long id);

    List<Post> getAllByGroup_Id(Long groupId);

    @Query(value = "select p from Post p join Group g on p.group.id = g.id where g.type = 'DEPARTMENT'")
    List<Post> getAllAnnouncement();

    @Query(value = "select p from Post p where p.owner.id = :userId and p.content like %:content% ")
    List<Post> getAllPostContainContentForUser(Long userId, String content);
    @Query(value = "select distinct p from Post p left join Comment c on p.id = c.post.id " +
            "where p.group.id = :groupId and (c.content like %:content% or p.content like %:content%)")
    List<Post> getAllPostInGroupWhoseContentContain(Long groupId, String content);
}