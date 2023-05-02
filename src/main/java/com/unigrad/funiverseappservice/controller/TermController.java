package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import com.unigrad.funiverseappservice.entity.academic.Term;
import com.unigrad.funiverseappservice.entity.socialnetwork.Event;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.GroupMember;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.exception.InvalidActionException;
import com.unigrad.funiverseappservice.payload.DTO.GroupMemberDTO;
import com.unigrad.funiverseappservice.payload.DTO.TermDTO;
import com.unigrad.funiverseappservice.payload.request.StartDateRequest;
import com.unigrad.funiverseappservice.service.*;
import com.unigrad.funiverseappservice.util.DTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("term")
@RequiredArgsConstructor
public class TermController {

    private final ITermService termService;

    private final DTOConverter dtoConverter;

    private final IWorkspaceService workspaceService;

    private final ISyllabusService syllabusService;

    private final ICurriculumPlanService curriculumPlanService;

    private final ICurriculumService curriculumService;

    private final IGroupService groupService;

    private final IGroupMemberService groupMemberService;

    private final IUserDetailService userDetailService;

    private final IEventService eventService;

    @PostMapping()
    public ResponseEntity<Void> create(@RequestBody TermDTO termDTO) {

        termService.save(dtoConverter.convert(termDTO, Term.class));

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<TermDTO> update(@RequestBody TermDTO termDTO) {

        Optional<Term> termOpt = termService.get(termDTO.getId());

        return termOpt
                .map(term -> ResponseEntity.ok(dtoConverter.convert(termService.save(term), TermDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TermDTO> getById(@PathVariable Long id) {

        return termService.get(id)
                .map(term -> ResponseEntity.ok(dtoConverter.convert(term, TermDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TermDTO>> getAll() {

        return ResponseEntity.ok(Arrays.stream(dtoConverter.convert(termService.getAllActive(), TermDTO[].class)).toList());
    }

    // Return List group Course of next term
    @GetMapping("prepare-group")
    public ResponseEntity<List<Group>> getGroupInTerm() {
        // 1. Get Term information
        Term currentTerm = workspaceService.getCurrentTerm();
        Term nextTerm = workspaceService.getNextTerm();

        // 2. Find all curriculum in next term
        List<Curriculum> curriculaInNextTerm = new ArrayList<>();
        curriculaInNextTerm.addAll(curriculumService.getCurriculumByCurrentTerm(currentTerm.getId()));
        curriculaInNextTerm.addAll(curriculumService.getCurriculumByStartedTerm(nextTerm.getId()));

        // 3. Find syllabus will start
        // One Curriculum -> Many Class
        // One Class -> Many Syllabus
        Map<Long, List<Syllabus>> curriculumSyllabusMap = new HashMap<>();
        Map<Long, List<Group>> curriculumClassMap = new HashMap<>();
        Map<Long, Curriculum> curriculumMap = new HashMap<>();

        curriculaInNextTerm.forEach(curriculum -> {
            curriculumSyllabusMap.put(curriculum.getId(), curriculumPlanService.getAllSyllabusByCurriculumIdAndSemester(curriculum.getId(), curriculum.getCurrentSemester() + 1));
            curriculumClassMap.put(curriculum.getId(), groupService.getAllClassByCurriculumId(curriculum.getId()));
            curriculumMap.put(curriculum.getId(), curriculum);
        });

        for (Long aLong : curriculumClassMap.keySet()) {
            if (curriculumClassMap.get(aLong).isEmpty()) {
                throw new InvalidActionException("Do not have any Class for curriculum %s".formatted(curriculumMap.get(aLong).getCode()));
            }
        }

        // 4. Create Group by Syllabus and Class
        // There are some group that already created and some group will be created
        List<Group> groups = new ArrayList<>();

        for (Map.Entry<Long, List<Syllabus>> entry : curriculumSyllabusMap.entrySet()) {
            for (Group group : curriculumClassMap.get(entry.getKey())) {
                for (Syllabus syllabus : entry.getValue()) {
                    groups.add(prepareCourseGroup(syllabus, group, curriculumMap.get(entry.getKey())));
                }
            }
        }

        return ResponseEntity.ok(groups);
    }

    private Group prepareCourseGroup(Syllabus syllabus, Group clazz, Curriculum curriculum) {
        Optional<Group> courseOptional = groupService.getBySyllabusIdAndReferenceClassId(syllabus.getId(), clazz.getId());

        if (courseOptional.isPresent()) {
            return courseOptional.get();
        }

        Group course = Group.builder()
                .name(clazz.getName() + "-" + syllabus.getCode())
                .syllabus(syllabus)
                .referenceClass(clazz)
                .curriculum(curriculum)
                .isPrivate(true)
                .isActive(true)
                .isPublish(false)
                .createdDateTime(LocalDateTime.now())
                .type(Group.Type.COURSE)
                .build();

        List<UserDetail> students = groupMemberService.getAllUsersInGroup(clazz.getId());
        for (UserDetail student : students) {
            groupMemberService.addMemberToGroup(new GroupMemberDTO(student.getId(), clazz.getId(), false));
        }

        return groupService.save(course);
    }

    @GetMapping("start-new")
    public ResponseEntity<Term> startNewTerm() {
        // 1. Get Term information
        Term currentTerm = workspaceService.getCurrentTerm();
        Term nextTerm = workspaceService.getNextTerm();

        if (nextTerm.getState().equals(Term.State.READY)) {
            // 2. Find all curriculum in next term
            List<Curriculum> curriculaInNextTerm = new ArrayList<>();
            curriculaInNextTerm.addAll(curriculumService.getCurriculumByCurrentTerm(currentTerm.getId()));
            curriculaInNextTerm.addAll(curriculumService.getCurriculumByStartedTerm(nextTerm.getId()));

            Map<Long, List<Syllabus>> curriculumSyllabusMap = new HashMap<>();
            Map<Long, List<Group>> curriculumClassMap = new HashMap<>();
            Map<Long, Curriculum> curriculumMap = new HashMap<>();

            curriculaInNextTerm.forEach(curriculum -> {
                curriculumSyllabusMap.put(curriculum.getId(), curriculumPlanService.getAllSyllabusByCurriculumIdAndSemester(curriculum.getId(), curriculum.getCurrentSemester() + 1));
                curriculumClassMap.put(curriculum.getId(), groupService.getAllClassByCurriculumId(curriculum.getId()));
                curriculumMap.put(curriculum.getId(), curriculum);
            });

            // Update next current term
            curriculaInNextTerm.forEach(curriculum -> {
                // Check if current is last semester
                if (curriculum.getCurrentSemester() < curriculum.getNoSemester()) {
                    curriculum.setCurrentTerm(nextTerm);
                    curriculum.setCurrentSemester(curriculum.getCurrentSemester() + 1);

                    curriculumService.save(curriculum);
                }
            });

            List<Group> groups = new ArrayList<>();

            for (Map.Entry<Long, List<Syllabus>> entry : curriculumSyllabusMap.entrySet()) {
                for (Group group : curriculumClassMap.get(entry.getKey())) {
                    for (Syllabus syllabus : entry.getValue()) {
                        groups.add(prepareCourseGroup(syllabus, group, curriculumMap.get(entry.getKey())));
                    }
                }
            }

            for (Group group : groups) {
                group.setPublish(true);
                groupService.save(group);
            }

            List<UserDetail> userDetails = userDetailService.getAllActive();
            UserDetail admin = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            for (UserDetail user : userDetails) {
                Event event = Event.builder()
                        .actor(admin)
                        .receiver(user)
                        .sourceId(null)
                        .sourceType(null)
                        .type(Event.Type.NEW_SEMESTER)
                        .group(null)
                        .term(nextTerm.toString())
                        .createdTime(LocalDateTime.now())
                        .build();

                eventService.save(event);
            }

            return ResponseEntity.ok(workspaceService.startNextTerm());
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("startdate")
    public ResponseEntity<Term> setStartDate(@RequestBody StartDateRequest startDateRequest) {

        return  ResponseEntity.ok(workspaceService.setStartDate(startDateRequest.getStartDate()));
    }
}