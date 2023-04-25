package com.unigrad.funiverseappservice.payload.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecializationFlat {

    private String id;

    private String name;

    private String code;

    private String student_code;

    private String major_code;
}