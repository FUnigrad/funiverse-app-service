package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Term;
import com.unigrad.funiverseappservice.payload.DTO.TermDTO;
import com.unigrad.funiverseappservice.service.ITermService;
import com.unigrad.funiverseappservice.util.DTOConverter;
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

    private final DTOConverter dtoConverter;

    public TermController(ITermService termService, DTOConverter dtoConverter) {
        this.termService = termService;
        this.dtoConverter = dtoConverter;
    }

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody TermDTO termDTO) {

        termService.save(dtoConverter.convert(termDTO, Term.class));

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<TermDTO> update(@RequestBody TermDTO termDTO) {

        Optional<Term> termOpt = termService.get(termDTO.getId());

        return termOpt
                .map(term -> ResponseEntity.ok(dtoConverter.convert(termService.save(term), TermDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TermDTO> getById(@PathVariable Long id) {

        return termService.get(id)
                .map(term -> ResponseEntity.ok(dtoConverter.convert(term, TermDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TermDTO>> getAll() {

        return ResponseEntity.ok(Arrays.stream(dtoConverter.convert(termService.getAllActive(), TermDTO[].class)).toList());
    }
}