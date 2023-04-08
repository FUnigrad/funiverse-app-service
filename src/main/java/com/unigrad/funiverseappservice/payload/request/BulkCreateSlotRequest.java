package com.unigrad.funiverseappservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulkCreateSlotRequest {

    private Long groupId;

    private Integer amount;

    private String startDate;

    private List<CreateSlotRequest> slots;
}