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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
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

    private Long sourceId;

    @ManyToOne
    @JoinColumn
    private Group group;

    @Enumerated(EnumType.STRING)
    private SourceType sourceType;

    private LocalDateTime createdTime;

    private boolean isRead = false;

    @Getter
    @AllArgsConstructor
    public enum Type {
        MENTION,
        NEW_POST,
        NEW_COMMENT,
        REACTION,
        SET_GROUP_ADMIN,
        ADD_TO_GROUP;
    }

    @Getter
    @AllArgsConstructor
    public enum SourceType {
        GROUP,
        POST,
        COMMENT
    }
}