package com.unigrad.funiverseappservice.entity.academic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unigrad.funiverseappservice.entity.Workspace;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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

    public Term(Season season, String year) {
        this.season = season;
        this.year = year;
    }

    @OneToMany(mappedBy = "startedTerm")
    @JsonIgnore
    private List<Curriculum> curricula;

    @OneToOne(mappedBy = "currentTerm")
    @JsonIgnore
    private Workspace workspace;

    @Override
    public String toString() {
        return "%s %s".formatted(season.getName(), year);
    }
}