package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.service.impl.GroupMemberService;
import com.unigrad.funiverseappservice.service.impl.UserDetailService;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {

    private final GroupMemberService groupMemberService;

    private final UserDetailService userDetailService;

    public UserController(GroupMemberService groupMemberService, UserDetailService userDetailService) {
        this.groupMemberService = groupMemberService;
        this.userDetailService = userDetailService;
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

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody UserDetail userDetail) {
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

        userDetailService.deactivate(id);

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
}
