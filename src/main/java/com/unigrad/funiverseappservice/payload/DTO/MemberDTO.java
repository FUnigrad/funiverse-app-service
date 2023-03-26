package com.unigrad.funiverseappservice.payload.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO extends UserDTO{

    private boolean isGroupAdmin;
}