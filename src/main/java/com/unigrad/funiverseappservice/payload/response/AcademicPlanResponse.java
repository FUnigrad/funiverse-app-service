package com.unigrad.funiverseappservice.payload.response;

import com.unigrad.funiverseappservice.payload.DTO.EntityBaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcademicPlanResponse {

    private EntityBaseDTO syllabus;

    private byte semester;

    private Long groupId;
}