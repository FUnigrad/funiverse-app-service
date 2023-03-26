package com.unigrad.funiverseappservice.payload.request;

import com.unigrad.funiverseappservice.entity.academic.Term;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OnBoardingRequest {

    private int foundedYear;

    private Term currentTerm;

    private String logoImage;
}