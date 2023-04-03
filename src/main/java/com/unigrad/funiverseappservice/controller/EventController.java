package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.socialnetwork.Event;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.service.IEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("event")
@RequiredArgsConstructor
public class EventController {

    private final IEventService eventService;

    @PutMapping("{id}")
    public ResponseEntity<Void> readEvent(@PathVariable Long id) {
        Optional<Event> eventOptional = eventService.get(id);

        if (eventOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userDetail.getId().equals(eventOptional.get().getReceiver().getId())) {
            throw new AccessDeniedException("Current user cannot perform this action!");
        }

        eventOptional.get().setRead(true);

        eventService.save(eventOptional.get());

        return ResponseEntity.ok().build();
    }
}