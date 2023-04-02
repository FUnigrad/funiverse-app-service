package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.socialnetwork.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPostRepository extends JpaRepository<Post, Long> {

    void deleteById(Long id);

    List<Post> getAllByGroup_Id(Long groupId);

    @Query(value = "select p from Post p join Group g on p.group.id = g.id where g.type = 'DEPARTMENT'")
    List<Post> getAllAnnouncement();
}