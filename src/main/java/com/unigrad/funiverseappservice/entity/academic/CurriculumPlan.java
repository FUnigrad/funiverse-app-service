package com.unigrad.funiverseappservice.entity.academic;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CurriculumPlan {

    @EmbeddedId
    private CurriculumPlanKey curriculumPlanKey;

    @ManyToOne
    @MapsId("curriculumId")
    private Curriculum curriculum;

    @ManyToOne
    @MapsId("syllabusId")
    private Syllabus syllabus;

    private Byte semester;

    private boolean isComboPlan = false;

    @ManyToOne
    @JoinColumn
    private Combo combo;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurriculumPlanKey implements Serializable {

        @Column(name = "curriculumId")
        private long curriculumId;

        @Column(name = "syllabusId")
        private long syllabusId;
    }
}