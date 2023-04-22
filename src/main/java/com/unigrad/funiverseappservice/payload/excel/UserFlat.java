package com.unigrad.funiverseappservice.payload.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFlat {

    private Long id;

    private String name;

    private String personal_mail;

    private String identify_number;

    private String phone_number;

    private String role;

    private String curriculum_code;

}