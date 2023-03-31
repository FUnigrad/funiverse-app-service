package com.unigrad.funiverseappservice.entity.academic;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer no;

    @Column(name = "[order]")
    private Integer order;

    private Integer dayOfWeek;

    @ManyToOne
    @JoinColumn
    private Group group;

    private String room;

    private LocalDate date;
}