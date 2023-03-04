package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.academic.Term;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ITermService extends IBaseService<Term, Long>{

    Optional<Term> get(Term.Season season, String year);
}