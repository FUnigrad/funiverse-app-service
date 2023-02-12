package com.unigrad.funiverseappservice.entity.socialnetwork;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn
    private UserDetail actor;

    @ManyToOne
    @JoinColumn
    private UserDetail receiver;

    @Enumerated(EnumType.STRING)
    private Type type;

    private LocalDateTime createdTime;

    private boolean isRead;

    @Getter
    @AllArgsConstructor
    public enum Type {
        MENTION(" mention you in "),
        NEW_POST(" posted a post "),
        NEW_COMMENT(" comment in post ");

        private final String text;
    }
}