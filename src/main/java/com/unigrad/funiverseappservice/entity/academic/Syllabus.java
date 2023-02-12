package com.unigrad.funiverseappservice.entity.academic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unigrad.funiverseappservice.entity.converter.DurationConverter;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Syllabus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn
    private Subject subject;

    private byte noCredit;

    private byte noSlot;

    @Convert(converter = DurationConverter.class)
    private Duration durationPerSlot;

    private String preRequisite;

    private String description;

    private byte minAvgMarkToPass;

    @OneToMany(mappedBy = "syllabus")
    @JsonIgnore
    private List<Group> groups;

    @OneToMany(mappedBy = "syllabus")
    @JsonIgnore
    private List<Material> materials;

    @OneToMany(mappedBy = "syllabus")
    @JsonIgnore
    private List<Slot> slots;
}