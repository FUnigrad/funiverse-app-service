package com.unigrad.funiverseappservice.payload.DTO;

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

    private EntityBaseDTO syllabus;

    private byte semester;

    private boolean isComboPlan;
}