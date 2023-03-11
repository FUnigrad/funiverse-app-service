package com.unigrad.funiverseappservice.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboDTO extends EntityBaseDTO {

    List<EntityBaseDTO> syllabi;
}