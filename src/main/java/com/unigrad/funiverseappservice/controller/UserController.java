package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.Workspace;
import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.Slot;
import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import com.unigrad.funiverseappservice.entity.socialnetwork.*;
import com.unigrad.funiverseappservice.exception.InvalidActionException;
import com.unigrad.funiverseappservice.exception.ServiceCommunicateException;
import com.unigrad.funiverseappservice.payload.DTO.TimetableEventDTO;
import com.unigrad.funiverseappservice.payload.DTO.UserDTO;
import com.unigrad.funiverseappservice.service.*;
import com.unigrad.funiverseappservice.service.impl.EmitterService;
import com.unigrad.funiverseappservice.service.impl.GroupMemberService;
import com.unigrad.funiverseappservice.service.impl.UserDetailService;
import com.unigrad.funiverseappservice.util.DTOConverter;
import com.unigrad.funiverseappservice.util.SlotCalculator;
import com.unigrad.funiverseappservice.util.Utils;
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
import java.util.*;
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

    private final ICurriculumService curriculumService;

    private final IPostService postService;

    private final ICommentService commentService;

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
    @Transactional(rollbackOn = ServiceCommunicateException.class)
    public ResponseEntity<Void> save(@RequestBody UserDetail userDetail, HttpServletRequest request) {

        //generate code
        if (Role.STUDENT.equals(userDetail.getRole())) {
            if (userDetail.getCurriculum() != null) {
                Optional<Curriculum> curriculumOptional = curriculumService.get(userDetail.getCurriculum().getId());

                if (curriculumOptional.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }
                Curriculum curriculum = curriculumOptional.get();
                String userCode = userDetailService.generateStudentCode(curriculum.getSchoolYear(),
                        curriculum.getSpecialization().getStudentCode());
                userDetail.setCode(userCode);
                userDetail.setSchoolYear(curriculum.getSchoolYear());
                userDetail.setEduMail("%s%s@%s".formatted(Utils.generateUserCode(userDetail.getName()), userCode, workspaceService.getEmailSuffix()));
            }
        } else {
            String userCode = userDetailService.generateUserCode(Utils.generateUserCode(userDetail.getName()));
            userDetail.setCode(userCode);
            userDetail.setEduMail("%s@%s".formatted(userCode, workspaceService.getEmailSuffix()));
        }

        UserDetail newUserDetail = userDetailService.save(userDetail);

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!authenCommunicateService.saveUser(userDetail, token)) {
            throw new ServiceCommunicateException("An error occurs when call to Authen Service");
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUserDetail.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<UserDetail> update(@RequestBody UserDetail userDetail) {

        //generate code
        if (Role.STUDENT.equals(userDetail.getRole())) {
            if (userDetail.getCurriculum() != null) {
                Optional<Curriculum> curriculumOptional = curriculumService.get(userDetail.getCurriculum().getId());

                if (curriculumOptional.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }
                Curriculum curriculum = curriculumOptional.get();

                String userCode = userDetailService.generateStudentCode(curriculum.getSchoolYear(),
                        curriculum.getSpecialization().getStudentCode());
                userDetail.setCode(userCode);
                userDetail.setSchoolYear(curriculum.getSchoolYear());
                userDetail.setEduMail("%s%s%%%s".formatted(Utils.generateUserCode(userDetail.getName()), userCode, workspaceService.getEmailSuffix()));
            }
        }

        return userDetailService.isExist(userDetail.getId())
                ? ResponseEntity.ok(userDetailService.save(userDetail))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional(rollbackOn = ServiceCommunicateException.class)
    public ResponseEntity<UserDetail> deactivate(@PathVariable Long id, HttpServletRequest request) {
        Optional<UserDetail> userDetailOptional = userDetailService.get(id);

        if (userDetailOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!authenCommunicateService.inactiveUser(userDetailOptional.get().getPersonalMail(), token)) {
            throw new ServiceCommunicateException("An error occurs when call to Authen Service");
        }

        userDetailService.inactivate(id);

        return ResponseEntity.ok().build();
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
        Optional<Group> groupOptional = groupService.get(id);

        if (groupOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Group group = groupOptional.get();
        List<UserDetail> usersNotInGroup = userDetailService.getAllUsersNotInGroup(id);

        if (Group.Type.CLASS.equals(group.getType()) || Group.Type.COURSE.equals(group.getType())) {
            usersNotInGroup = usersNotInGroup.stream()
                    .filter(user -> user.getCurriculum() != null && Objects.equals(user.getCurriculum().getId(), group.getCurriculum().getId()) && Role.STUDENT.equals(user.getRole())).toList();
        }

        return ResponseEntity.ok(Arrays.stream(dtoConverter.convert(usersNotInGroup, UserDTO[].class)).toList());
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

        if (Role.STUDENT.equals(currentUser.getRole())) {
            Curriculum curriculum = currentUser.getCurriculum();
            Group clazz = groupService.getClassByStudentId(currentUser.getId());

            List<Syllabus> syllabiInTerm = curriculumPlanService.getAllSyllabusByCurriculumIdAndSemester(curriculum.getId(), curriculum.getCurrentSemester());

            //noinspection OptionalGetWithoutIsPresent
            List<Group> courses = syllabiInTerm.stream().map(syllabus -> groupService.getBySyllabusIdAndReferenceClassId(syllabus.getId(), clazz.getId()).get()).toList();

            List<TimetableEvent> result = new ArrayList<>();

            courses.forEach(course -> course.getSlots().stream().map(slot -> getOrCreateTimetableEvent(currentUser, slot, course)).forEach(result::add));

            return ResponseEntity.ok().body(Arrays.stream(dtoConverter.convert(result, TimetableEventDTO[].class)).toList());
        } else if (Role.TEACHER.equals(currentUser.getRole())) {
            List<Group> groups = groupService.getTeachingClass(currentUser.getId());

            List<TimetableEvent> result = new ArrayList<>();

            groups.forEach(course -> course.getSlots().stream().map(slot -> getOrCreateTimetableEvent(currentUser, slot, course)).forEach(result::add));

            return ResponseEntity.ok().body(Arrays.stream(dtoConverter.convert(result, TimetableEventDTO[].class)).toList());
        }

        throw new InvalidActionException("Current User cannot access Timetable");
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

    @GetMapping("/notification")
    public SseEmitter subscribe() {

        SseEmitter sseEmitter = new SseEmitter(24 * 60 * 60 * 1000L);
        emitterService.addEmitter(sseEmitter);

        LOG.info("Subscribed");
        return sseEmitter;
    }

    @GetMapping("permission")
    public ResponseEntity<Boolean> checkPermissionDelete(@RequestParam String object, @RequestParam Long id) {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if ("member".equalsIgnoreCase(object)) {

            if (groupService.isExist(id)) {
                return ResponseEntity.ok(groupMemberService.isGroupAdmin(userDetail.getId(), id));
            }
        } else if ("post".equalsIgnoreCase(object)) {

            Optional<Post> postOptional = postService.get(id);

            if (postOptional.isPresent()) {
                return ResponseEntity.ok(postOptional.get().getOwner().getId().equals(userDetail.getId()));
            }
        } else if ("comment".equalsIgnoreCase(object)) {
            Optional<Comment> commentOptional = commentService.get(id);

            if (commentOptional.isPresent()) {
                return ResponseEntity.ok(commentOptional.get().getOwner().getId().equals(userDetail.getId()));
            }
        }

        return ResponseEntity.notFound().build();
    }
}