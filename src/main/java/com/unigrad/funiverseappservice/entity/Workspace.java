package com.unigrad.funiverseappservice.entity;

import com.unigrad.funiverseappservice.entity.academic.Term;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {

    @Id
    private Long id;

    private String name;

    private String code;

    @OneToOne
    @JoinColumn
    private Term currentTerm;

    private Long foundedYear;

    private Integer slotDurationInMin;

    private Integer restTimeInMin;

    private String domain;

    private LocalTime morningStartTime;

    private LocalTime morningEndTime;

    private LocalTime afternoonStartTime;

    private LocalTime afternoonEndTime;

    private String emailSuffix;
}