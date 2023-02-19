package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.exception.MissingRequiredPropertyException;
import com.unigrad.funiverseappservice.service.ICurriculumService;
import com.unigrad.funiverseappservice.service.IGroupService;
import com.unigrad.funiverseappservice.service.ISyllabusService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
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
@RequestMapping("group")
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



        switch (newGroupType){

            case CLASS ->{
                if(newGroup.getName() == null || newGroup.getName().isEmpty()){
                    throw new MissingRequiredPropertyException();
                }
                if (newGroup.getCurriculum() == null){
                    throw new MissingRequiredPropertyException("Curriculum");
                }
            }

            case COURSE -> {

                if (newGroup.getTeacher() == null){
                    throw new MissingRequiredPropertyException("Teacher");
                }
                if (newGroup.getSyllabus() == null){
                    throw new MissingRequiredPropertyException("Syllabus");
                }

                newGroup.setName("COURSE ");
            }

            case DEPARTMENT, NORMAL -> {
                if(newGroup.getName() == null || newGroup.getName().isEmpty()){
                    throw new MissingRequiredPropertyException();
                }
            }
        }

        Group returnGroup = groupService.save(newGroup);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(returnGroup.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getById(@PathVariable Long id) {

        return groupService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAll(@RequestParam(required = false) String groupName) {

        List<Group> subjects = groupName == null
                ? groupService.getAll()
                : groupService.getByName(groupName);

        return ResponseEntity.ok(subjects);
    }

    @PutMapping
    public ResponseEntity<Group> update(@RequestBody Group group) {

        return groupService.isExist(group.getId())
                ? ResponseEntity.ok(groupService.save(group))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Group> deactivate(@PathVariable Long id) {
        groupService.deactivate(id);

        return groupService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> activate(@PathVariable Long id) {
        groupService.activate(id);

        return groupService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
