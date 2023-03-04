package com.unigrad.funiverseappservice.entity;

import com.unigrad.funiverseappservice.entity.academic.Term;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
public class Workspace {

    private static Workspace WORKSPACE = new Workspace();

    @PostConstruct
    private void init() {
        WORKSPACE.setId(1L);
        WORKSPACE.setName("FPT University Da Nang");
        WORKSPACE.setCode("FUDN");
        WORKSPACE.setCurrentTerm(new Term(Term.Season.SPRING, "2023"));
    }

    public static Workspace get() {
        return WORKSPACE;
    }
    private Long id;

    private String name;

    private String code;

    private Term currentTerm;
}