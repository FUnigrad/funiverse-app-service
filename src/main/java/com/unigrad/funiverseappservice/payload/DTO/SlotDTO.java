package com.unigrad.funiverseappservice.payload.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlotDTO {

    private Long id;

    private Integer no;

    private Integer order;

    private EntityBaseDTO group;

    private String room;

    private LocalDate date;
}