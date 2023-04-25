package com.unigrad.funiverseappservice.payload.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SyllabusFlat {

    private String id;

    private String code;

    private String name;

    private String description;

    private String min_avg_mark_to_pass;

    private String no_credit;

    private String subject_code;

    private String no_slot;
}