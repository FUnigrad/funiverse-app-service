package com.unigrad.funiverseappservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSlotRequest {

    private Integer order;

    private Integer dayOfWeek;

    private String room;
}