package com.unigrad.funiverseappservice.entity.socialnetwork;

import com.unigrad.funiverseappservice.entity.academic.Slot;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TimetableEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text")
    private String title;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private String location;

    @ManyToOne
    @JoinColumn
    private UserDetail userDetail;

    @ManyToOne
    @JoinColumn
    private Slot slot;
}