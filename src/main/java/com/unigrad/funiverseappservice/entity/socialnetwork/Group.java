package com.unigrad.funiverseappservice.entity.socialnetwork;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "[group]")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String conversationId;

    @ManyToOne
    @JoinColumn
    private Curriculum curriculum;

    @ManyToOne
    @JoinColumn
    private Syllabus syllabus;

    @ManyToOne
    @JoinColumn
    private UserDetail teacher;

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    private List<Post> posts;

    public enum Type {
        CLASS,
        SUBJECT,
        DEPARTMENT,
        NORMAL
    }
}