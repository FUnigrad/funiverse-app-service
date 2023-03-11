package com.unigrad.funiverseappservice.payload.response;

import com.unigrad.funiverseappservice.payload.EntityBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListComboResponse {

    private EntityBaseDTO curriculum;

    private List<EntityBaseDTO> combos;

}