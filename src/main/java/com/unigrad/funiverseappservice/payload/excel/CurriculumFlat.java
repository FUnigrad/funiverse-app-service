package com.unigrad.funiverseappservice.payload.excel;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumFlat {

    private String id;

    private String description;

    private String no_semester;

    private String specialization_code;

    private String start_term;

}