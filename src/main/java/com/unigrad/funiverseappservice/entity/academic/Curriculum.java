package com.unigrad.funiverseappservice.entity.academic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
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

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Curriculum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    private String schoolYear;

    private String description;

    private boolean isActive = true;

    @ManyToOne
    @JoinColumn
    private Major major;

    @ManyToOne
    @JoinColumn
    private Specialization specialization;

    @ManyToOne
    @JoinColumn
    private Term startedTerm;

    private byte noSemester;

    private byte currentSemester;

    @OneToMany(mappedBy = "curriculum")
    @JsonIgnore
    private List<UserDetail> users;

    @OneToMany(mappedBy = "curriculum")
    @JsonIgnore
    private List<Group> groups;
}