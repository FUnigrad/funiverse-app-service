package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.service.impl.GroupMemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {

    private final GroupMemberService groupMemberService;

    public UserController(GroupMemberService groupMemberService) {
        this.groupMemberService = groupMemberService;
    }

    @GetMapping("/{id}/groups")
    public ResponseEntity<List<String>> getAllJoinedGroup(@PathVariable Long id) {

        return ResponseEntity.ok(
                groupMemberService.getAllByUserId(id).stream()
                        .map(groupMember -> groupMember.getGroupMemberKey().getGroupId().toString())
                        .collect(Collectors.toList())
        );
    }
}
