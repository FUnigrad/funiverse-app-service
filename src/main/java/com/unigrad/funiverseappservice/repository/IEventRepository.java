package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.socialnetwork.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByReceiverId(Long id);

    @Query("update Event e set e.isRead = true where e.id = :id")
    @Modifying
    void read(Long id);
}