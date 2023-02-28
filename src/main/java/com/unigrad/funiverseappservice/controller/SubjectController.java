package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Subject;
import com.unigrad.funiverseappservice.service.ISubjectService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("subject")
public class SubjectController {

    private final ISubjectService subjectService;

    public SubjectController(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<Subject>> getAll(@RequestParam(required = false) String code) {

        List<Subject> subjects = code == null
                ? subjectService.getAll()
                : subjectService.getByCode(code);

        for (Subject subject : subjects) {
            if (subject.isCombo() && !subject.getSubjects().isEmpty()) {
                subject.setSubjects(subject.getSubjects().stream()
                        .map(subjectInCombo -> subjectService.get(subjectInCombo.getId()).get())
                        .collect(Collectors.toList())
                );
            }
        }

        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getById(@PathVariable Long id) {

        return subjectService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Subject subject) {
        Subject newSubject = subjectService.save(subject);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newSubject.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Subject> update(@RequestBody Subject subject) {

        return subjectService.isExist(subject.getId())
                ? ResponseEntity.ok(subjectService.save(subject))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Subject> deactivate(@PathVariable Long id) {
        subjectService.inactivate(id);

        return subjectService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subject> activate(@PathVariable Long id) {
        subjectService.activate(id);

        return subjectService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}