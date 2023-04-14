package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.Workspace;
import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.Slot;
import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import com.unigrad.funiverseappservice.entity.socialnetwork.Event;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.TimetableEvent;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.exception.ServiceCommunicateException;
import com.unigrad.funiverseappservice.payload.DTO.TimetableEventDTO;
import com.unigrad.funiverseappservice.payload.DTO.UserDTO;
import com.unigrad.funiverseappservice.service.IAuthenCommunicateService;
import com.unigrad.funiverseappservice.service.ICurriculumPlanService;
import com.unigrad.funiverseappservice.service.IEventService;
import com.unigrad.funiverseappservice.service.IGroupService;
import com.unigrad.funiverseappservice.service.ITimetableEventService;
import com.unigrad.funiverseappservice.service.IWorkspaceService;
import com.unigrad.funiverseappservice.service.impl.EmitterService;
import com.unigrad.funiverseappservice.service.impl.GroupMemberService;
import com.unigrad.funiverseappservice.service.impl.UserDetailService;
import com.unigrad.funiverseappservice.util.DTOConverter;
import com.unigrad.funiverseappservice.util.SlotCalculator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final GroupMemberService groupMemberService;

    private final UserDetailService userDetailService;

    private final DTOConverter dtoConverter;

    private final IEventService eventService;

    private final IAuthenCommunicateService authenCommunicateService;

    private final ICurriculumPlanService curriculumPlanService;

    private final IGroupService groupService;

    private final IWorkspaceService workspaceService;

    private final ITimetableEventService timetableEventService;

    private final EmitterService emitterService;

    @GetMapping
    public ResponseEntity<List<UserDetail>> getAll() {

        return ResponseEntity.ok(userDetailService.getAllActive());
    }

    @GetMapping("/{id}/groups")
    public ResponseEntity<List<String>> getAllJoinedGroup(@PathVariable Long id) {

        return ResponseEntity.ok(
                groupMemberService.getAllByUserId(id).stream()
                        .map(groupMember -> groupMember.getGroupMemberKey().getGroupId().toString())
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetail> getById(@PathVariable Long id) {

        return userDetailService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("admin")
    public ResponseEntity<Void> saveAuthen(@RequestBody UserDetail userDetail, HttpServletRequest request) {

        UserDetail newUserDetail = userDetailService.save(userDetail);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUserDetail.getId()).toUri();



        return ResponseEntity.created(location).build();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> save(@RequestBody UserDetail userDetail, HttpServletRequest request) {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!authenCommunicateService.saveUser(userDetail, token)) {
            throw new ServiceCommunicateException("An error occurs when call to Authen Service");
        }

        //generate code


        UserDetail newUserDetail = userDetailService.save(userDetail);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUserDetail.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<UserDetail> update(@RequestBody UserDetail user) {

        return userDetailService.isExist(user.getId())
                ? ResponseEntity.ok(userDetailService.save(user))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDetail> deactivate(@PathVariable Long id) {

        userDetailService.inactivate(id);

        return userDetailService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetail> activate(@PathVariable Long id) {
        userDetailService.activate(id);

        return userDetailService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("curriculum/none")
    public ResponseEntity<List<UserDTO>> getUsersWithNoCurriculum() {

        return ResponseEntity.ok(Arrays.stream(dtoConverter.convert(userDetailService.getAllUsersHaveNoCurriculum(), UserDTO[].class)).toList());
    }

    @GetMapping("group/none")
    public ResponseEntity<List<UserDTO>> getUsersNotInGroup(@RequestParam Long id) {

        return ResponseEntity.ok(Arrays.stream(dtoConverter.convert(userDetailService.getAllUsersNotInGroup(id), UserDTO[].class)).toList());
    }

    @GetMapping("event")
    public ResponseEntity<List<Event>> getListEvent(@RequestParam(defaultValue = "false") boolean unread) {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Event> events = eventService.getAllForUser(userDetail.getId());

        if (unread) {
            events = events.stream().filter(event -> !event.isRead()).toList();
        }

        if (!events.isEmpty()) {
            events = new ArrayList<>(events);
            events.sort(Comparator.comparing(Event::getCreatedTime).reversed());
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("me")
    public ResponseEntity<UserDetail> getMe() {

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(userDetail);
    }

    @GetMapping("timetable")
    public ResponseEntity<List<TimetableEventDTO>> getTimeTable() {
        UserDetail currentUser = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (currentUser.isAdmin()) {
            throw new AccessDeniedException("Admin cannot perform this request");
        }

        Curriculum curriculum = currentUser.getCurriculum();
        Group clazz = groupService.getClassByStudentId(currentUser.getId());

        List<Syllabus> syllabiInTerm = curriculumPlanService.getAllSyllabusByCurriculumIdAndSemester(curriculum.getId(), curriculum.getCurrentSemester());

        //noinspection OptionalGetWithoutIsPresent
        List<Group> courses = syllabiInTerm.stream().map(syllabus -> groupService.getBySyllabusIdAndReferenceClassId(syllabus.getId(), clazz.getId()).get()).toList();

        List<TimetableEvent> result = new ArrayList<>();

        courses.forEach(course -> course.getSlots().stream().map(slot -> getOrCreateTimetableEvent(currentUser, slot, course)).forEach(result::add));

        return ResponseEntity.ok().body(Arrays.stream(dtoConverter.convert(result, TimetableEventDTO[].class)).toList());
    }

    private TimetableEvent getOrCreateTimetableEvent(UserDetail userDetail, Slot slot, Group course) {

        Optional<TimetableEvent> timetableEventOptional = timetableEventService.getByUserIdAndSlotId(userDetail.getId(), slot.getId());
        Workspace workspace = workspaceService.get();

        if (timetableEventOptional.isPresent()) {
            return timetableEventOptional.get();
        }

        LocalTime startTime = SlotCalculator.calculateStartTime(slot.getOrder(), workspace.getMorningStartTime(), workspace.getMorningEndTime(),
                workspace.getAfternoonStartTime(), workspace.getSlotDurationInMin(), workspace.getRestTimeInMin());
        LocalTime endTime = startTime.plusMinutes(workspace.getSlotDurationInMin());

        TimetableEvent timetableEvent = TimetableEvent.builder()
                .title(course.getSyllabus().getCode())
                .startDateTime(LocalDateTime.of(slot.getDate(), startTime))
                .endDateTime(LocalDateTime.of(slot.getDate(), endTime))
                .description("Slot %s of syllabus %s".formatted(slot.getNo(), course.getSyllabus().getName()))
                .slot(slot)
                .userDetail(userDetail)
                .location(slot.getRoom())
                .build();

        return timetableEventService.save(timetableEvent);
    }

    @GetMapping("/user/notification")
    public SseEmitter subscribe() {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOG.info("%s subscribing notification".formatted(userDetail.getCode()));

        SseEmitter sseEmitter = new SseEmitter(24 * 60 * 60 * 1000L);
        emitterService.addEmitter(sseEmitter);

        LOG.info("Subscribed");
        return sseEmitter;
    }
}