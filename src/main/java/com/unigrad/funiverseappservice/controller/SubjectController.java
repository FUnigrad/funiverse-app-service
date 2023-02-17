package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Subject;
import com.unigrad.funiverseappservice.service.ISubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("subject")
public class SubjectController {

    private final ISubjectService subjectService;

    public SubjectController(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<Subject>> getAll(){

        return ResponseEntity.ok(subjectService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getById(@PathVariable Long id){

        return subjectService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{code}")
    public ResponseEntity<Subject> getByCode(@PathVariable String code){

        return subjectService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Subject> save(@RequestBody Subject subject){
        Subject newSubject = subjectService.save(subject);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newSubject.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Subject> update(@RequestBody Subject subject){
        Optional<Subject> oldSubject = subjectService.get(subject.getId());

        return oldSubject
                .map(s -> ResponseEntity.ok(subjectService.save(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Subject> deactivate(@PathVariable Long id){
        subjectService.deactivate(id);

        return subjectService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}