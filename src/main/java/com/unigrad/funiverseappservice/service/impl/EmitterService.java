package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.socialnetwork.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EmitterService {

    List<SseEmitter> emitters = new ArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitters.add(emitter);
    }

    public void pushNotification(Event event) {
        log.info("Pushing notification for user {}", event.getReceiver().getCode());
        List<SseEmitter> deadEmitters = new ArrayList<>();

        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter
                        .event()
                        .name(event.getReceiver().getCode())
                        .data(event));

            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });

        emitters.removeAll(deadEmitters);
    }
}