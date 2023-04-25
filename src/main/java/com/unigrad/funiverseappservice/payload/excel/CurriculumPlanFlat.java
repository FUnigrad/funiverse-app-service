package com.unigrad.funiverseappservice.payload.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumPlanFlat {


    private String curriculum_code;

    private String syllabus_code;

    private String semester;
}