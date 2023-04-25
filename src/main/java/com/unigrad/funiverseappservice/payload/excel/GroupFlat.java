package com.unigrad.funiverseappservice.payload.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupFlat {

    private String id;

    private String name;

    private String type;

    private String curriculum_code;
}