package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Specialization;
import com.unigrad.funiverseappservice.service.ISpecializationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("specialization")
public class SpecializationController {

    private final ISpecializationService specializationService;

    public SpecializationController(ISpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @GetMapping
    public ResponseEntity<List<Specialization>> getAll() {

        return ResponseEntity.ok(specializationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialization> getById(@PathVariable Long id) {

        return specializationService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Specialization specialization) {
        Specialization newSpecialization = specializationService.save(specialization);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newSpecialization.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Specialization> update(@RequestBody Specialization specialization) {

        return specializationService.isExist(specialization.getId())
                ? ResponseEntity.ok(specializationService.save(specialization))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Specialization> deactivate(@PathVariable Long id) {

        specializationService.deactivate(id);

        return specializationService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Specialization> activate(@PathVariable Long id) {
        specializationService.activate(id);

        return specializationService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}