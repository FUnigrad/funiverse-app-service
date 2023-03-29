package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Season;
import com.unigrad.funiverseappservice.service.ISeasonService;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@RestController
@RequestMapping("season")
@RequiredArgsConstructor
public class SeasonController {

    private final ISeasonService seasonService;

    @GetMapping
    public ResponseEntity<List<Season>> getAll(@RequestParam(required = false) boolean isActive) {
        return ResponseEntity.ok(isActive ? seasonService.getAll() : seasonService.getAllActive());
    }

    @GetMapping("{id}")
    public ResponseEntity<Season> get(@PathVariable Long id) {
        return seasonService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody Season season) {

        return ResponseEntity.ok(seasonService.save(season).getId());
    }

    @PutMapping
    public ResponseEntity<Season> update(@RequestBody Season season) {

        return seasonService.isExist(season.getId())
                ? ResponseEntity.ok(seasonService.save(season))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        if (seasonService.isExist(id)) {
            seasonService.inactivate(id);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}