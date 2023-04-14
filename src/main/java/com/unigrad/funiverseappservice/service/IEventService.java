package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.socialnetwork.Event;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IEventService {

    Event save(Event event);

    Optional<Event> get(Long id);

    List<Event> getAllForUser(Long userId);

    void read(Long id);


}