package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.*;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.service.*;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import com.unigrad.funiverseappservice.specification.SearchCriteria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

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

    public SearchController(ISyllabusService syllabusService, ICurriculumService curriculumService, IUserDetailService userDetailService, IGroupService groupService, ISubjectService subjectService, IMajorService majorService, ISpecializationService specializationService) {
        this.syllabusService = syllabusService;
        this.curriculumService = curriculumService;
        this.userDetailService = userDetailService;
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.majorService = majorService;
        this.specializationService = specializationService;
    }


    @GetMapping
    public List<?> search(@RequestParam String entity,
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

                return syllabusService.search(specification);
            }
            case "curriculum" -> {
                EntitySpecification<Curriculum> specification = new EntitySpecification<>(searchCriteria);

                return curriculumService.search(specification);
            }
            case "user" -> {
                EntitySpecification<UserDetail> specification = new EntitySpecification<>(searchCriteria);

                return userDetailService.search(specification);
            }
            case "group" -> {
                EntitySpecification<Group> specification = new EntitySpecification<>(searchCriteria);

                return groupService.search(specification);
            }
            case "subject" -> {
                EntitySpecification<Subject> specification = new EntitySpecification<>(searchCriteria);

                return subjectService.search(specification);
            }
            case "major" -> {
                EntitySpecification<Major> specification = new EntitySpecification<>(searchCriteria);

                return majorService.search(specification);
            }
            case "specialization" -> {
                EntitySpecification<Specialization> specification = new EntitySpecification<>(searchCriteria);

                return specializationService.search(specification);
            }
        }

        return List.of();
    }
}