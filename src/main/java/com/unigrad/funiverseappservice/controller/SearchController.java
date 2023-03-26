package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Combo;
import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.Major;
import com.unigrad.funiverseappservice.entity.academic.Specialization;
import com.unigrad.funiverseappservice.entity.academic.Subject;
import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.payload.DTO.ComboDTO;
import com.unigrad.funiverseappservice.service.IComboService;
import com.unigrad.funiverseappservice.service.ICurriculumService;
import com.unigrad.funiverseappservice.service.IGroupService;
import com.unigrad.funiverseappservice.service.IMajorService;
import com.unigrad.funiverseappservice.service.ISpecializationService;
import com.unigrad.funiverseappservice.service.ISubjectService;
import com.unigrad.funiverseappservice.service.ISyllabusService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import com.unigrad.funiverseappservice.specification.SearchCriteria;
import com.unigrad.funiverseappservice.util.DTOConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("search")
public class SearchController {

    private final ISyllabusService syllabusService;

    private final ICurriculumService curriculumService;

    private final IUserDetailService userDetailService;

    private final IGroupService groupService;

    private final ISubjectService subjectService;

    private final IMajorService majorService;

    private final ISpecializationService specializationService;

    private final IComboService comboService;

    private final DTOConverter dtoConverter;

    public SearchController(ISyllabusService syllabusService, ICurriculumService curriculumService, IUserDetailService userDetailService, IGroupService groupService, ISubjectService subjectService, IMajorService majorService, ISpecializationService specializationService, IComboService comboService, DTOConverter dtoConverter) {
        this.syllabusService = syllabusService;
        this.curriculumService = curriculumService;
        this.userDetailService = userDetailService;
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.majorService = majorService;
        this.specializationService = specializationService;
        this.comboService = comboService;
        this.dtoConverter = dtoConverter;
    }


    @GetMapping
    public ResponseEntity<List<?>> search(@RequestParam String entity,
                                         @RequestParam String[] field,
                                         @RequestParam String[] operator,
                                         @RequestParam(defaultValue = "") String[] value){
        //in case the condition of search criteria is not enough, search base on the least criteria
        int noCriteria = Math.min(Math.min(field.length, operator.length), value.length);

        List<SearchCriteria> searchCriteria = new ArrayList<>();
        for (int i = 0; i < noCriteria; i++) {
            searchCriteria.add(new SearchCriteria(field[i], operator[i], value[i] == null ? "" : value[i]));
        }

        switch (entity){
            case "syllabus" -> {
                EntitySpecification<Syllabus> specification = new EntitySpecification<>(searchCriteria);

                return ResponseEntity.ok(syllabusService.search(specification));
            }
            case "curriculum" -> {
                EntitySpecification<Curriculum> specification = new EntitySpecification<>(searchCriteria);

                return ResponseEntity.ok(curriculumService.search(specification));
            }
            case "user" -> {
                EntitySpecification<UserDetail> specification = new EntitySpecification<>(searchCriteria);

                return ResponseEntity.ok(userDetailService.search(specification));
            }
            case "group" -> {
                EntitySpecification<Group> specification = new EntitySpecification<>(searchCriteria);

                return ResponseEntity.ok(groupService.search(specification));
            }
            case "subject" -> {
                EntitySpecification<Subject> specification = new EntitySpecification<>(searchCriteria);

                return ResponseEntity.ok(subjectService.search(specification));
            }
            case "major" -> {
                EntitySpecification<Major> specification = new EntitySpecification<>(searchCriteria);

                return ResponseEntity.ok(majorService.search(specification));
            }
            case "specialization" -> {
                EntitySpecification<Specialization> specification = new EntitySpecification<>(searchCriteria);

                return ResponseEntity.ok(specializationService.search(specification));
            }
            case "combo" -> {
                EntitySpecification<Combo> specification = new EntitySpecification<>(searchCriteria);
                List<Combo> combos = comboService.search(specification);

                for (Combo combo : combos) {
                    //noinspection OptionalGetWithoutIsPresent
                    List<Syllabus> syllabi = combo.getSyllabi().stream()
                            .map(syllabus -> syllabusService.get(syllabus.getId()).get())
                            .collect(Collectors.toList());

                    combo.setSyllabi(syllabi);
                }

                return ResponseEntity.ok(Arrays.stream(dtoConverter.convert(combos, ComboDTO[].class)).toList());
            }
            default -> throw new IllegalStateException("Unexpected value: " + entity);
        }
    }
}