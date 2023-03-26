package com.unigrad.funiverseappservice.payload.DTO;

import com.unigrad.funiverseappservice.entity.academic.Term;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TermDTO {

    private Long id;

    private Term.Season season;

    private String year;
}