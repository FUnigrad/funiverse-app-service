package com.unigrad.funiverseappservice.entity.socialnetwork;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import jakarta.persistence.*;
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
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String code;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String schoolYear;

    private String personalMail;

    private String eduMail;

    private String avatar;

    private String phoneNumber;

    private boolean isActive;

    @ManyToOne
    @JoinColumn
    private Curriculum curriculum;

    @OneToMany(mappedBy = "teacher")
    @JsonIgnore
    private List<Group> teachingGroup;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Post> posts;

    @OneToMany(mappedBy = "actor")
    @JsonIgnore
    private List<Event> performedEvents;

    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private List<Event> events;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<Comment> comments;
}