package com.unigrad.funiverseappservice.payload.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComboPlanDTO {

    private EntityBaseDTO combo;

    private EntityBaseDTO curriculum;

    private List<CurriculumPlanDTO> comboPlans;
}