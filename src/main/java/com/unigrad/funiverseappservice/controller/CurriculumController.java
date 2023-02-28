package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.service.ICurriculumService;
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
@RequestMapping("curriculum")
public class CurriculumController {

    private final ICurriculumService curriculumService;

    public CurriculumController(ICurriculumService curriculumService) {
        this.curriculumService = curriculumService;
    }

    @GetMapping
    public ResponseEntity<List<Curriculum>> getAll(@RequestParam(required = false) String code) {

        List<Curriculum> curriculums = code == null
                ? curriculumService.getAll() : List.of();

        return ResponseEntity.ok(curriculums);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curriculum> getById(@PathVariable Long id) {

        return curriculumService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Curriculum curriculum) {
        Curriculum newCurriculum = curriculumService.save(curriculum);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCurriculum.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Curriculum> update(@RequestBody Curriculum curriculum) {

        return curriculumService.isExist(curriculum.getId())
                ? ResponseEntity.ok(curriculumService.save(curriculum))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Curriculum> deactivate(@PathVariable Long id) {
        curriculumService.inactivate(id);

        return curriculumService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curriculum> activate(@PathVariable Long id) {
        curriculumService.activate(id);

        return curriculumService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}