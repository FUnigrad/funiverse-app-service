package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITermRepository extends JpaRepository<Term, Long> {

    Optional<Term> getBySeasonIdAndYear(Long seasonId, String year);
}