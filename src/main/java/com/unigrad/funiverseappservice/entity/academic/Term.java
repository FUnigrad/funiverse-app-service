package com.unigrad.funiverseappservice.entity.academic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unigrad.funiverseappservice.entity.Workspace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(name = "term_UK", columnNames = {"season_id", "year"}))
public class Term {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Season season;

    private String year;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private State state = State.PENDING;

    @OneToMany(mappedBy = "startedTerm")
    @JsonIgnore
    private List<Curriculum> curricula;

    @OneToMany(mappedBy = "currentTerm")
    @JsonIgnore
    private List<Curriculum> currentCurricula;

    @OneToOne(mappedBy = "currentTerm")
    @JsonIgnore
    private Workspace workspace;

    public Term(Season season, String year) {
        this.season = season;
        this.year = year;
    }

    @Override
    public String toString() {
        return "%s %s".formatted(season.getName(), year);
    }

    public enum State {
        PENDING,
        READY,
        ONGOING,
        COMPLETE
    }
}