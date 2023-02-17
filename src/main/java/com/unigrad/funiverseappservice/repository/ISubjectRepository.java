package com.unigrad.funiverseappservice.repository;

import com.unigrad.funiverseappservice.entity.academic.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISubjectRepository extends JpaRepository<Subject, Long> {

    @Modifying
    @Query("update Subject s set s.isActive = :isActive where s.id = :id")
    void updateIsActive(Long id, boolean isActive);

    List<Subject> findAllByActiveIsTrue();

    List<Subject> findAllByCodeLike(String code);
}