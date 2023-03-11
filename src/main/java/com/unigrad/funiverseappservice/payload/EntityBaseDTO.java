package com.unigrad.funiverseappservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntityBaseDTO {

    private Long id;

    private String name;

    private String code;

    private boolean isActive;
}