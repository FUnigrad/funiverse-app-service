package com.unigrad.funiverseappservice.entity.academic;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SchoolYear {

    @Id
    private String schoolYear;

    private Long nextStudentSeq;
}