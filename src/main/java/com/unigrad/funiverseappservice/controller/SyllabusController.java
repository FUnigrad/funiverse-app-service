package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import com.unigrad.funiverseappservice.service.ISyllabusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("syllabus")
public class SyllabusController {

    private final ISyllabusService syllabusService;

    public SyllabusController(ISyllabusService syllabusService) {
        this.syllabusService = syllabusService;
    }

    @GetMapping
    public ResponseEntity<List<Syllabus>> getAll(@RequestParam(required = false) String code) {

        List<Syllabus> syllabi = code == null
                ? syllabusService.getAllActive() : List.of();

        return ResponseEntity.ok(syllabi);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Syllabus> getById(@PathVariable Long id) {

        return syllabusService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody Syllabus syllabus) {
        Syllabus newSyllabus = syllabusService.save(syllabus);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newSyllabus.getId()).toUri();

        return ResponseEntity.created(location).body(newSyllabus.getId().toString());
    }

    @PutMapping
    public ResponseEntity<Syllabus> update(@RequestBody Syllabus syllabus) {

        return syllabusService.isExist(syllabus.getId())
                ? ResponseEntity.ok(syllabusService.save(syllabus))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Syllabus> deactivate(@PathVariable Long id) {
        syllabusService.inactivate(id);

        return syllabusService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Syllabus> activate(@PathVariable Long id) {
        syllabusService.activate(id);

        return syllabusService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}