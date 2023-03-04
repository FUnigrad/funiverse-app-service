package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.payload.CurriculumPlanDTO;
import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.CurriculumPlan;
import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import com.unigrad.funiverseappservice.service.ICurriculumPlanService;
import com.unigrad.funiverseappservice.service.ICurriculumService;
import com.unigrad.funiverseappservice.service.ISyllabusService;
import com.unigrad.funiverseappservice.util.Converter;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("curriculum")
public class CurriculumController {

    private final ICurriculumService curriculumService;

    private final ICurriculumPlanService curriculumPlanService;

    private final ISyllabusService syllabusService;

    private final Converter converter;

    public CurriculumController(ICurriculumService curriculumService, ICurriculumPlanService curriculumPlanService, ISyllabusService syllabusService, Converter converter) {
        this.curriculumService = curriculumService;
        this.curriculumPlanService = curriculumPlanService;
        this.syllabusService = syllabusService;
        this.converter = converter;
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
    public ResponseEntity<String> save(@RequestBody Curriculum curriculum) {
        curriculum.setCode(curriculumService.generateCode(curriculum));
        curriculum.setName(curriculumService.generateName(curriculum));

        Curriculum newCurriculum = curriculumService.save(curriculum);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCurriculum.getId()).toUri();

        return ResponseEntity.created(location).body(newCurriculum.getId().toString());
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

    @PostMapping("/{id}/syllabus")
    public ResponseEntity<String> addSyllabusToCurriculum(@RequestBody CurriculumPlanDTO curriculumPlanDTO, @PathVariable Long id) {
        Optional<Curriculum> curriculumOpt = curriculumService.get(id);
        Optional<Syllabus> syllabusOpt = syllabusService.get(curriculumPlanDTO.getSyllabus().getId());

        if (syllabusOpt.isEmpty()) {
            throw new EntityNotFoundException("Syllabus ID %s not exist".formatted(curriculumPlanDTO.getSyllabus().getId()));
        }

        if (curriculumOpt.isPresent()) {

            if (curriculumPlanService.isExist(new CurriculumPlan.CurriculumPlanKey(id, syllabusOpt.get().getId()))) {
                return ResponseEntity.badRequest().body("Syllabus %s is already exist in Curriculum!".formatted(syllabusOpt.get().getCode()));
            }

            CurriculumPlan curriculumPlan = converter.convert(curriculumPlanDTO, CurriculumPlan.class);
            curriculumPlan.setCurriculum(curriculumOpt.get());
            curriculumPlan = curriculumPlanService.save(curriculumPlan);

            return ResponseEntity.ok(curriculumPlan.getCurriculum().getId().toString());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/syllabus")
    public ResponseEntity<CurriculumPlanDTO> editSyllabusInCurriculum(@RequestBody CurriculumPlanDTO curriculumPlanDTO, @PathVariable Long id) {
        Optional<Curriculum> curriculumOpt = curriculumService.get(id);
        Optional<Syllabus> syllabusOpt = syllabusService.get(curriculumPlanDTO.getSyllabus().getId());

        if (syllabusOpt.isEmpty()) {
            throw new EntityNotFoundException("Syllabus ID %s not exist".formatted(curriculumPlanDTO.getSyllabus().getId()));
        }

        if (curriculumOpt.isPresent()) {

            if (curriculumPlanService.isExist(new CurriculumPlan.CurriculumPlanKey(id, syllabusOpt.get().getId()))) {

                CurriculumPlan curriculumPlan = converter.convert(curriculumPlanDTO, CurriculumPlan.class);
                curriculumPlan.setCurriculum(curriculumOpt.get());
                curriculumPlan = curriculumPlanService.save(curriculumPlan);

                return ResponseEntity.ok(converter.convert(curriculumPlan, CurriculumPlanDTO.class));
            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{cid}/syllabus/{sid}")
    public ResponseEntity<Void> removeSyllabusToCurriculum(@PathVariable Long cid, @PathVariable Long sid) {
        if (curriculumPlanService.removeSyllabusFromCurriculum(sid, cid)) {

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/syllabus")
    public ResponseEntity<List<CurriculumPlanDTO>> getAllSyllabusInCurriculum(@PathVariable Long id) {
        Optional<Curriculum> curriculum = curriculumService.get(id);

        if (curriculum.isPresent()) {
            List<CurriculumPlan> curriculumPlans = curriculumPlanService.getAllByCurriculumId(curriculum.get().getId());
            List<CurriculumPlanDTO> curriculumPlanDTOs = Arrays.stream(converter.convert(curriculumPlans, CurriculumPlanDTO[].class)).toList();

            return ResponseEntity.ok(curriculumPlanDTOs);
        }

        return ResponseEntity.notFound().build();
    }
}