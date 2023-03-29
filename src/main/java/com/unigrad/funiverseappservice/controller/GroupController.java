package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.socialnetwork.Event;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.GroupMember;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.exception.MissingRequiredPropertyException;
import com.unigrad.funiverseappservice.payload.DTO.GroupMemberDTO;
import com.unigrad.funiverseappservice.payload.DTO.MemberDTO;
import com.unigrad.funiverseappservice.payload.DTO.PostDTO;
import com.unigrad.funiverseappservice.service.ICurriculumService;
import com.unigrad.funiverseappservice.service.IEventService;
import com.unigrad.funiverseappservice.service.IGroupMemberService;
import com.unigrad.funiverseappservice.service.IGroupService;
import com.unigrad.funiverseappservice.service.IPostService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import com.unigrad.funiverseappservice.util.DTOConverter;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("group")
@RequiredArgsConstructor
public class GroupController {

    private final IGroupService groupService;

    private final IGroupMemberService groupMemberService;

    private final IUserDetailService userDetailService;

    private final IPostService postService;

    private final ICurriculumService curriculumService;

    private final IEventService eventService;

    private final DTOConverter dtoConverter;

    @PostMapping
    public ResponseEntity<Group> create(@RequestBody Group newGroup) {
        Group.Type newGroupType = newGroup.getType();

        switch (newGroupType) {

            case CLASS -> {
                Optional<Curriculum> curriculumOpt = curriculumService.get(newGroup.getCurriculum().getId());

                if (curriculumOpt.isEmpty()) {
                    throw new MissingRequiredPropertyException("Curriculum");
                }

                String name = curriculumOpt.get().getSpecialization().getCode() + curriculumOpt.get().getSchoolYear().substring(1);
                int order = groupService.getNextNameOrderForClass(name);
                name += String.format("%02d", order);
                newGroup.setName(name);
            }

            case COURSE -> {

                if (newGroup.getTeacher() == null) {
                    throw new MissingRequiredPropertyException("Teacher");
                }
                if (newGroup.getSyllabus() == null) {
                    throw new MissingRequiredPropertyException("Syllabus");
                }
                //todo need Class to copy students to this group
                newGroup.setName("COURSE ");
            }

            case DEPARTMENT, NORMAL -> {
                if (StringUtils.isBlank(newGroup.getName())) {
                    throw new MissingRequiredPropertyException("Name");
                }
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetail userDetail = (UserDetail) authentication.getPrincipal();

        if (!userDetail.isAdmin()) {
            newGroup = Group.builder()
                    .name(newGroup.getName())
                    .type(Group.Type.NORMAL)
                    .isActive(true)
                    .build();
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

        Optional<Group> groupOptional = groupService.get(id);

        if (groupOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userDetail.isAdmin()) {
            if (groupMemberService.isGroupMember(userDetail.getId(), id)) {
                return ResponseEntity.ok(groupOptional.get());
            } else {
                throw new AccessDeniedException("You don not have permission to perform this action");
            }
        }

        return ResponseEntity.ok(groupOptional.get());
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAll() {

        List<Group> groups = groupService.getAllActive();

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userDetail.isAdmin()) {
            groups = groups.stream()
                    .filter(group -> groupMemberService.isGroupMember(userDetail.getId(), group.getId()))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(groups);
    }

    @PutMapping
    public ResponseEntity<Group> update(@RequestBody Group group) {

        if (!groupService.isExist(group.getId())) {
            return ResponseEntity.notFound().build();
        }

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetail.isAdmin() || groupMemberService.isGroupAdmin(userDetail.getId(), group.getId())) {
            return ResponseEntity.ok(groupService.save(group));
        } else {
            throw new AccessDeniedException("You don not have permission to perform this action");
        }
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Void> addNewMemberToGroup(@PathVariable Long id, @RequestBody List<Long> memberIds) {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Group> groupOpt = groupService.get(id);

        if (groupOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (groupMemberService.isGroupAdmin(userDetail.getId(), id) || userDetail.isAdmin()) {
            for (Long memberId : memberIds) {
                Optional<UserDetail> memberOptional = userDetailService.get(memberId);

                memberOptional.ifPresent(member -> {
                    groupMemberService.addMemberToGroup(new GroupMemberDTO(member.getId(), groupOpt.get().getId(), false));

                    Event event = Event.builder()
                            .actor(userDetail)
                            .receiver(member)
                            .sourceId(groupOpt.get().getId())
                            .sourceType(Event.SourceType.GROUP)
                            .type(Event.Type.ADD_TO_GROUP)
                            .createdTime(LocalDateTime.now())
                            .build();

                    eventService.save(event);
                });
            }

            return ResponseEntity.ok().build();
        } else {
            throw new AccessDeniedException("You don not have permission to perform this action");
        }
    }

    @DeleteMapping("/{gid}/user/{uid}")
    public ResponseEntity<Void> removeMemberFromGroup(@PathVariable Long gid, @PathVariable Long uid) {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        GroupMember.GroupMemberKey key = new GroupMember.GroupMemberKey(uid, gid);

        if (!groupMemberService.isExist(key)) {
            return ResponseEntity.notFound().build();
        }

        if (groupMemberService.isGroupAdmin(userDetail.getId(), gid) || userDetail.isAdmin()) {

            groupMemberService.deleteByGroupMemberKey(key);
            return ResponseEntity.ok().build();
        } else {
            throw new AccessDeniedException("You don not have permission to perform this action");
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Group> deactivate(@PathVariable Long id) {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!groupService.isExist(id)) {
            return ResponseEntity.notFound().build();
        }

        if (groupMemberService.isGroupAdmin(userDetail.getId(), id) || userDetail.isAdmin()) {

            groupService.inactivate(id);
            return ResponseEntity.ok().build();
        } else {
            throw new AccessDeniedException("You don not have permission to perform this action");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> activate(@PathVariable Long id) {

        groupService.activate(id);

        return groupService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("{gid}/posts")
    public ResponseEntity<List<PostDTO>> getAllPostsInGroup(@PathVariable Long gid) {

        List<PostDTO> postDtoList = Arrays.asList(dtoConverter.convert(postService.getAllPostInGroup(gid), PostDTO[].class));

        return ResponseEntity.ok(postDtoList);
    }

    @GetMapping("{id}/users")
    public ResponseEntity<List<MemberDTO>> getUsersInGroup(@PathVariable Long id) {
        List<UserDetail> members = groupMemberService.getAllUsersInGroup(id);
        List<MemberDTO> memberDTOs = Arrays.stream(dtoConverter.convert(members, MemberDTO[].class)).toList();

        for (MemberDTO memberDTO : memberDTOs) {
            Optional<GroupMember> groupMember = groupMemberService.get(new GroupMember.GroupMemberKey(memberDTO.getId(), id));

            //noinspection OptionalGetWithoutIsPresent
            memberDTO.setGroupAdmin(groupMember.get().isGroupAdmin());
        }
        return ResponseEntity.ok(memberDTOs);
    }

    @PutMapping("{groupId}/users/{userId}/set-admin")
    public ResponseEntity<GroupMemberDTO> setAdminOfGroup(@PathVariable Long groupId, @PathVariable Long userId, @RequestParam boolean value) {

        Optional<GroupMember> groupMemberOpt = groupMemberService.get(new GroupMember.GroupMemberKey(userId, groupId));

        if (groupMemberOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userDetail.isAdmin() || groupMemberService.isGroupAdmin(userDetail.getId(), groupId)) {

            groupMemberOpt.get().setGroupAdmin(value);

            if (value) {
                //noinspection OptionalGetWithoutIsPresent
                Event event = Event.builder()
                        .actor(userDetail)
                        .receiver(userDetailService.get(userId).get())
                        .sourceId(groupService.get(groupId).get().getId())
                        .sourceType(Event.SourceType.GROUP)
                        .type(Event.Type.SET_GROUP_ADMIN)
                        .createdTime(LocalDateTime.now())
                        .build();

                eventService.save(event);
            }

            return ResponseEntity.ok(dtoConverter.convert(groupMemberService.save(groupMemberOpt.get()), GroupMemberDTO.class));
        } else {
            throw new AccessDeniedException("You don not have permission to perform this action");
        }
    }
}