package com.unigrad.funiverseappservice.entity.socialnetwork;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.Slot;
import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import jakarta.persistence.Column;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "[group]")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "nvarchar(255)")
    private String name;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String conversationId;

    private boolean isPrivate;

    private LocalDateTime createdDateTime;

    private boolean isActive = true;

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

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    private List<Slot> slots;

    public enum Type {
        CLASS,
        COURSE,
        DEPARTMENT,
        NORMAL
    }
}