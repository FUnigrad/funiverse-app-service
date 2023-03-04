package com.unigrad.funiverseappservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurriculumPlanDTO {

    private SyllabusDTO syllabus;

    private byte semester;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SyllabusDTO {

        private Long id;

        private String name;

        private String code;
    }
}