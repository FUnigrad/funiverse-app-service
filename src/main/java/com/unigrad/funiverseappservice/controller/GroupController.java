package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.GroupMember;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.exception.MissingRequiredPropertyException;
import com.unigrad.funiverseappservice.payload.GroupMemberDTO;
import com.unigrad.funiverseappservice.payload.MemberDTO;
import com.unigrad.funiverseappservice.payload.PostDTO;
import com.unigrad.funiverseappservice.service.ICurriculumService;
import com.unigrad.funiverseappservice.service.IGroupMemberService;
import com.unigrad.funiverseappservice.service.IGroupService;
import com.unigrad.funiverseappservice.service.IPostService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import com.unigrad.funiverseappservice.util.DTOConverter;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("group")
public class GroupController {

    private final IGroupService groupService;

    private final IGroupMemberService groupMemberService;

    private final IUserDetailService userDetailService;

    private final IPostService postService;

    private final ICurriculumService curriculumService;

    private final DTOConverter dtoConverter;

    public GroupController(IGroupService groupService, IGroupMemberService groupMemberService, IUserDetailService userDetailService, IPostService postService, ICurriculumService curriculumService, DTOConverter dtoConverter) {
        this.groupService = groupService;
        this.groupMemberService = groupMemberService;
        this.userDetailService = userDetailService;
        this.postService = postService;
        this.curriculumService = curriculumService;
        this.dtoConverter = dtoConverter;
    }

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
    public ResponseEntity<List<Group>> getAll() {

        List<Group> subjects = groupService.getAllActive();

        return ResponseEntity.ok(subjects);
    }

    @PutMapping
    public ResponseEntity<Group> update(@RequestBody Group group) {

        return groupService.isExist(group.getId())
                ? ResponseEntity.ok(groupService.save(group))
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<Void> addNewMemberToGroup(@PathVariable Long id, @RequestBody List<Long> memberIds) {

        Optional<Group> groupOpt = groupService.get(id);

        if (groupOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        for (Long memberId : memberIds) {
            Optional<UserDetail> member = userDetailService.get(memberId);

            member.ifPresent(userDetail -> groupMemberService.addMemberToGroup(new GroupMemberDTO(userDetail.getId(), groupOpt.get().getId(), false)));
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{gid}/user/{uid}")
    public ResponseEntity<Void> removeMemberFromGroup(@PathVariable Long gid, @PathVariable Long uid) {

        GroupMember.GroupMemberKey key = new GroupMember.GroupMemberKey(uid, gid);

        if (groupMemberService.isExist(key)) {
            groupMemberService.deleteByGroupMemberKey(key);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Group> deactivate(@PathVariable Long id) {

        groupService.inactivate(id);

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
    public ResponseEntity<GroupMemberDTO> setAdminOfGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        Optional<Group> groupOpt = groupService.get(groupId);
        Optional<UserDetail> userDetailOpt = userDetailService.get(userId);

        if (groupOpt.isEmpty() || userDetailOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<GroupMember> groupMemberOpt = groupMemberService.get(new GroupMember.GroupMemberKey(userId, groupId));

        if (groupMemberOpt.isPresent()) {
            groupMemberOpt.get().setGroupAdmin(true);
            return ResponseEntity.ok(dtoConverter.convert(groupMemberService.save(groupMemberOpt.get()), GroupMemberDTO.class));
        }

        return ResponseEntity.notFound().build();
    }
}