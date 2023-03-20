package com.unigrad.funiverseappservice.entity.academic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unigrad.funiverseappservice.entity.converter.SyllabusConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Combo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "nvarchar(255)")
    private String name;

    private String code;

    private boolean isActive = true;

    @Convert(converter = SyllabusConverter.class)
    private List<Syllabus> syllabi;

    @OneToMany(mappedBy = "combo")
    @JsonIgnore
    private List<CurriculumPlan> curriculumPlans;
}