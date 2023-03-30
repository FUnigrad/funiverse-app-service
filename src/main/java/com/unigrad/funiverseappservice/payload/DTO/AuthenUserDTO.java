package com.unigrad.funiverseappservice.payload.DTO;

import com.unigrad.funiverseappservice.entity.Workspace;
import com.unigrad.funiverseappservice.entity.socialnetwork.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenUserDTO {

    private String eduMail;

    private String personalMail;

    private Workspace workspace;

    private Role role;
}