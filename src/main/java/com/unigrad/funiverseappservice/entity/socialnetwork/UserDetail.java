package com.unigrad.funiverseappservice.entity.socialnetwork;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

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

    @NonNull
    private String name;

    private String code;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String schoolYear;

    @Email
    private String personalMail;

    @Email
    private String eduMail;

    private String avatar;

    private String phoneNumber;

    private boolean isActive = true;

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