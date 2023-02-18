package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.exception.MissingRequiredPropertyException;
import com.unigrad.funiverseappservice.service.ICurriculumService;
import com.unigrad.funiverseappservice.service.IGroupService;
import com.unigrad.funiverseappservice.service.ISyllabusService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/group")
public class GroupController {

    private final IGroupService groupService;

    private final ICurriculumService curriculumService;

    private final IUserDetailService userDetailService;

    private final ISyllabusService syllabusService;

    public GroupController(IGroupService groupService, ICurriculumService curriculumService, IUserDetailService userDetailService, ISyllabusService syllabusService) {
        this.groupService = groupService;
        this.curriculumService = curriculumService;
        this.userDetailService = userDetailService;
        this.syllabusService = syllabusService;
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group newGroup) {

        Group.Type newGroupType= newGroup.getType();

        if(newGroup.getName() == null || newGroup.getName().isEmpty()){
            throw new MissingRequiredPropertyException();
        }

        switch (newGroupType){

            case CLASS ->{

                if (curriculumService.get(newGroup.getCurriculum().getId()).isEmpty())
                    throw new MissingRequiredPropertyException("Curriculum");

                Group returnGroup = groupService.save(newGroup);

                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(returnGroup.getId()).toUri();

                return ResponseEntity.created(location).build();
            }

            case COURSE -> {

                if (curriculumService.get(newGroup.getCurriculum().getId()).isEmpty()){
                    throw new MissingRequiredPropertyException("Curriculum");
                }
                if (userDetailService.get(newGroup.getTeacher().getId()).isEmpty()){
                    throw new MissingRequiredPropertyException("Teacher");
                }
                if (syllabusService.get(newGroup.getSyllabus().getId()).isEmpty()){
                    throw new MissingRequiredPropertyException("Syllabus");
                }

                newGroup.setName("COURSE "+newGroup.getName());
            }

            case DEPARTMENT, NORMAL -> {
            }
        }

        Group returnGroup = groupService.save(newGroup);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(returnGroup.getId()).toUri();

        return ResponseEntity.created(location).build();
    }
}
