package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.socialnetwork.GroupMember;
import com.unigrad.funiverseappservice.service.impl.GroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("group-member")
public class GroupMemberController {

    @Autowired
    private GroupMemberService groupMemberService;

    @GetMapping("user-id/{id}")
    public ResponseEntity<List<GroupMember>> getAllByUserId(@PathVariable Long id) {

        List<GroupMember> groupMembers = id == null
                ? groupMemberService.getAll()
                : groupMemberService.getAllByUserId(id);

        return ResponseEntity.ok(groupMembers);
    }
}
