package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Combo;
import com.unigrad.funiverseappservice.payload.ComboDTO;
import com.unigrad.funiverseappservice.payload.EntityBaseDTO;
import com.unigrad.funiverseappservice.service.IComboService;
import com.unigrad.funiverseappservice.service.ICurriculumService;
import com.unigrad.funiverseappservice.service.ISyllabusService;
import com.unigrad.funiverseappservice.util.DTOConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("combo")
public class ComboController {

    private final IComboService comboService;

    private final DTOConverter dtoConverter;

    public ComboController(IComboService comboService, ISyllabusService syllabusService, ICurriculumService curriculumService, DTOConverter dtoConverter) {
        this.comboService = comboService;
        this.dtoConverter = dtoConverter;
    }

    @GetMapping
    public ResponseEntity<List<EntityBaseDTO>> get() {

        return ResponseEntity.ok(Arrays.stream(dtoConverter.convert(comboService.getAll(), EntityBaseDTO[].class)).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComboDTO> getById(@PathVariable Long id) {

        return comboService.get(id)
                .map(combo -> ResponseEntity.ok(dtoConverter.convert(combo, ComboDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Combo> save(@RequestBody Combo combo) {
        Combo newCombo = comboService.save(combo);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCombo.getId()).toUri();

        return ResponseEntity.created(location).body(combo);
    }

    @PutMapping
    public ResponseEntity<Combo> update(@RequestBody Combo combo) {

        return comboService.isExist(combo.getId())
                ? ResponseEntity.ok(comboService.save(combo))
                : ResponseEntity.notFound().build();
    }
}