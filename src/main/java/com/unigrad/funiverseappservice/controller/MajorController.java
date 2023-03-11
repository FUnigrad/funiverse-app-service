package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Major;
import com.unigrad.funiverseappservice.service.IMajorService;
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
@RequestMapping("major")
public class MajorController {

    private final IMajorService majorService;

    public MajorController(IMajorService majorService) {
        this.majorService = majorService;
    }

    @GetMapping
    public ResponseEntity<List<Major>> getAll() {

        return ResponseEntity.ok(majorService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Major> getById(@PathVariable Long id) {

        return majorService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Major major) {
        Major newMajor = majorService.save(major);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newMajor.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Major> update(@RequestBody Major major) {

        return majorService.isExist(major.getId())
                ? ResponseEntity.ok(majorService.save(major))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Major> deactivate(@PathVariable Long id) {

        majorService.inactivate(id);

        return majorService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Major> activate(@PathVariable Long id) {
        majorService.activate(id);

        return majorService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //todo get list spec in major
}