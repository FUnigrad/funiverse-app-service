package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.Workspace;
import com.unigrad.funiverseappservice.entity.academic.Term;
import com.unigrad.funiverseappservice.payload.TermDTO;
import com.unigrad.funiverseappservice.service.ITermService;
import com.unigrad.funiverseappservice.util.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("term")
public class TermController {

    private final ITermService termService;

    private final Converter converter;

    public TermController(ITermService termService, Converter converter) {
        this.termService = termService;
        this.converter = converter;
    }

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody TermDTO termDTO) {

        termService.save(converter.convert(termDTO, Term.class));

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<TermDTO> update(@RequestBody TermDTO termDTO) {

        Optional<Term> termOpt = termService.get(termDTO.getId());

        return termOpt
                .map(term -> ResponseEntity.ok(converter.convert(termService.save(term), TermDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TermDTO> getById(@PathVariable Long id) {

        return termService.get(id)
                .map(term -> ResponseEntity.ok(converter.convert(term, TermDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TermDTO>> getAll() {

        return ResponseEntity.ok(Arrays.stream(converter.convert(termService.getAll(), TermDTO[].class)).toList());
    }

    @GetMapping("current")
    public ResponseEntity<TermDTO> getCurrent() {

        return ResponseEntity.ok(converter.convert(Workspace.get().getCurrentTerm(), TermDTO.class));
    }
    @GetMapping("next")
    public ResponseEntity<TermDTO> startNewTerm() {

        return ResponseEntity.ok(converter.convert(termService.startNewTerm(), TermDTO.class));
    }
}