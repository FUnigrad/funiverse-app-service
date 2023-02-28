package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.dto.GroupMemberDTO;
import com.unigrad.funiverseappservice.dto.PostDTO;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.GroupMember;
import com.unigrad.funiverseappservice.exception.MissingRequiredPropertyException;
import com.unigrad.funiverseappservice.service.IGroupMemberService;
import com.unigrad.funiverseappservice.service.IGroupService;
import com.unigrad.funiverseappservice.service.IPostService;
import io.micrometer.common.util.StringUtils;
import org.modelmapper.ModelMapper;
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

@RestController
@RequestMapping("group")
public class GroupController {

    private final IGroupService groupService;

    private final IGroupMemberService groupMemberService;

    private final IPostService postService;

    private final ModelMapper modelMapper;

    public GroupController(IGroupService groupService, IGroupMemberService groupMemberService, IPostService postService, ModelMapper modelMapper) {
        this.groupService = groupService;
        this.groupMemberService = groupMemberService;
        this.postService = postService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<Group> create(@RequestBody Group newGroup) {

        Group.Type newGroupType = newGroup.getType();

        switch (newGroupType) {

            case CLASS -> {
                if (StringUtils.isBlank(newGroup.getName())) {
                    throw new MissingRequiredPropertyException("Name");
                }
                if (newGroup.getCurriculum() == null) {
                    throw new MissingRequiredPropertyException("Curriculum");
                }
            }

            case COURSE -> {

                if (newGroup.getTeacher() == null) {
                    throw new MissingRequiredPropertyException("Teacher");
                }
                if (newGroup.getSyllabus() == null) {
                    throw new MissingRequiredPropertyException("Syllabus");
                }

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

        List<Group> subjects = groupService.getAll();

        return ResponseEntity.ok(subjects);
    }

    @PutMapping
    public ResponseEntity<Group> update(@RequestBody Group group) {

        return groupService.isExist(group.getId())
                ? ResponseEntity.ok(groupService.save(group))
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/add-member")
    public ResponseEntity<Void> addNewMemberToGroup(@RequestBody GroupMemberDTO groupMemberDTO) {

        groupMemberService.addMemberToGroup(groupMemberDTO);

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

        List<PostDTO> postDtoList = Arrays.asList(modelMapper.map(postService.getAllPostInGroup(gid), PostDTO[].class));

        return ResponseEntity.ok(postDtoList);
    }



}
