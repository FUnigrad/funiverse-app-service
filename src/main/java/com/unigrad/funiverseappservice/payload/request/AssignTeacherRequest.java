package com.unigrad.funiverseappservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignTeacherRequest {

    private Long groupId;

    protected Long teacherId;
}