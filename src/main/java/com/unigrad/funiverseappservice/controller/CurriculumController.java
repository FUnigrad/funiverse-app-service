package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Combo;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.payload.ComboPlanDTO;
import com.unigrad.funiverseappservice.payload.CurriculumPlanDTO;
import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.CurriculumPlan;
import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import com.unigrad.funiverseappservice.payload.EntityBaseDTO;
import com.unigrad.funiverseappservice.payload.UserDTO;
import com.unigrad.funiverseappservice.payload.response.ListComboResponse;
import com.unigrad.funiverseappservice.service.IComboService;
import com.unigrad.funiverseappservice.service.ICurriculumPlanService;
import com.unigrad.funiverseappservice.service.ICurriculumService;
import com.unigrad.funiverseappservice.service.ISyllabusService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import com.unigrad.funiverseappservice.service.IWorkspaceService;
import com.unigrad.funiverseappservice.util.DTOConverter;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("curriculum")
public class CurriculumController {

    private final ICurriculumService curriculumService;

    private final ICurriculumPlanService curriculumPlanService;

    private final ISyllabusService syllabusService;

    private final IUserDetailService userDetailService;

    private final IComboService comboService;

    private final IWorkspaceService workspaceService;

    private final DTOConverter dtoConverter;

    public CurriculumController(ICurriculumService curriculumService, ICurriculumPlanService curriculumPlanService, ISyllabusService syllabusService, IUserDetailService userDetailService, IComboService comboService, IWorkspaceService workspaceService, DTOConverter dtoConverter) {
        this.curriculumService = curriculumService;
        this.curriculumPlanService = curriculumPlanService;
        this.syllabusService = syllabusService;
        this.userDetailService = userDetailService;
        this.comboService = comboService;
        this.workspaceService = workspaceService;
        this.dtoConverter = dtoConverter;
    }

    @GetMapping
    public ResponseEntity<List<Curriculum>> getAll(@RequestParam(required = false) String code) {

        List<Curriculum> curriculums = code == null
                ? curriculumService.getAllActive() : List.of();

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

        //calculates school year
        Long foundedYear = workspaceService.get().getFoundedYear();
        Long curriculumYear = Long.valueOf(curriculum.getStartedTerm().getYear());

        //todo curriculum name = school year + season: ex: K15A, K15B, K15C
        curriculum.setSchoolYear("K%s".formatted(curriculumYear - foundedYear +1));
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

            CurriculumPlan curriculumPlan = dtoConverter.convert(curriculumPlanDTO, CurriculumPlan.class);
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

                CurriculumPlan curriculumPlan = dtoConverter.convert(curriculumPlanDTO, CurriculumPlan.class);
                curriculumPlan.setCurriculum(curriculumOpt.get());
                curriculumPlan = curriculumPlanService.save(curriculumPlan);

                return ResponseEntity.ok(dtoConverter.convert(curriculumPlan, CurriculumPlanDTO.class));
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
            List<CurriculumPlanDTO> curriculumPlanDTOs = new ArrayList<>(Arrays.stream(dtoConverter.convert(curriculumPlans, CurriculumPlanDTO[].class)).toList());
            curriculumPlanDTOs.sort(Comparator.comparing(CurriculumPlanDTO::getSemester));
            return ResponseEntity.ok(curriculumPlanDTOs);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("{id}/students")
    public ResponseEntity<List<UserDTO>> getStudentsInCurriculum(@PathVariable Long id) {

        return ResponseEntity.ok(Arrays.stream(dtoConverter.convert(curriculumService.getUsersInCurriculum(id), UserDTO[].class)).toList());
    }

    @PostMapping("{id}/students")
    public ResponseEntity<List<UserDTO>> addStudentToCurriculum(@PathVariable Long id, @RequestBody List<Long> studentIds) {
        Optional<Curriculum> curriculum = curriculumService.get(id);
        List<UserDTO> userDTOList = new ArrayList<>();

        if (curriculum.isPresent()) {
            for (Long studentId : studentIds) {
                Optional<UserDetail> student = userDetailService.get(studentId);

                if (student.isPresent()) {
                    student.get().setCurriculum(curriculum.get());

                    userDetailService.save(student.get());
                    userDTOList.add(dtoConverter.convert(student, UserDTO.class));
                }
            }
        } else {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userDTOList);
    }

    @DeleteMapping("{id}/students")
    public ResponseEntity<Void> removeStudentFromCurriculum(@PathVariable Long id, @RequestBody List<Long> studentIds) {
        Optional<Curriculum> curriculum = curriculumService.get(id);

        if (curriculum.isPresent()) {
            for (Long studentId : studentIds) {
                Optional<UserDetail> student = userDetailService.get(studentId);

                if (student.isPresent() && student.get().getCurriculum().getId().equals(curriculum.get().getId())) {
                    student.get().setCurriculum(null);

                    userDetailService.save(student.get());
                }
            }
        } else {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}/combos")
    public ResponseEntity<ListComboResponse> getComboInCurriculum(@PathVariable Long id) {
        Optional<Curriculum> curriculumOpt = curriculumService.get(id);

        if (curriculumOpt.isPresent()) {
            List<Combo> combos = comboService.getAllByCurriculumId(id);
            ListComboResponse listComboResponse = new ListComboResponse(
                    dtoConverter.convert(curriculumOpt.get(), EntityBaseDTO.class),
                    Arrays.stream(dtoConverter.convert(combos, EntityBaseDTO[].class)).toList()
            );

            return ResponseEntity.ok(listComboResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("{id}/combos")
    public ResponseEntity<Combo> addComboToCurriculum(@PathVariable Long id, @RequestBody ComboPlanDTO comboPlanDTO) {
        Optional<Curriculum> curriculumOpt = curriculumService.get(id);

        if (curriculumOpt.isPresent()) {
            comboPlanDTO.setCurriculum(dtoConverter.convert(curriculumOpt.get(), EntityBaseDTO.class));

            return ResponseEntity.ok(comboService.addComboToCurriculum(comboPlanDTO));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{curriculumId}/combos/{comboId}")
    public ResponseEntity<ComboPlanDTO> getComboPlan(@PathVariable Long curriculumId, @PathVariable Long comboId) {
        Optional<Curriculum> curriculumOpt = curriculumService.get(curriculumId);
        Optional<Combo> comboOpt = comboService.get(comboId);

        if (curriculumOpt.isEmpty() || comboOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ComboPlanDTO.builder()
                .combo(dtoConverter.convert(comboOpt.get(), EntityBaseDTO.class))
                .curriculum(dtoConverter.convert(curriculumOpt.get(), EntityBaseDTO.class))
                .comboPlans(Arrays.stream(dtoConverter
                        .convert(curriculumPlanService.getAllComboPlanByCurriculumIdAndComboId(curriculumId, comboId),
                                CurriculumPlanDTO[].class)).toList())
                .build());
    }

    //todo remove combo from curriculum

    //get Syllabus ready for adding to Curriculum
    @GetMapping("{id}/syllabus/get-ready")
    public ResponseEntity<List<EntityBaseDTO>> getReadySyllabusForAdding(@PathVariable Long id) {
        Optional<Curriculum> curriculumOptional = curriculumService.get(id);

        if (curriculumOptional.isPresent()) {
            List<Syllabus> syllabi = syllabusService.getReadySyllabusForAdding(id);

            return ResponseEntity.ok(Arrays.stream(dtoConverter.convert(syllabi, EntityBaseDTO[].class)).toList());
        }

        return ResponseEntity.notFound().build();
    }
}