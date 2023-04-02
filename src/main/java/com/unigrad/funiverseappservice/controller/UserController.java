package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.socialnetwork.Event;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.exception.ServiceCommunicateException;
import com.unigrad.funiverseappservice.payload.DTO.UserDTO;
import com.unigrad.funiverseappservice.service.IAuthenCommunicateService;
import com.unigrad.funiverseappservice.service.IEventService;
import com.unigrad.funiverseappservice.service.impl.GroupMemberService;
import com.unigrad.funiverseappservice.service.impl.UserDetailService;
import com.unigrad.funiverseappservice.util.DTOConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final GroupMemberService groupMemberService;

    private final UserDetailService userDetailService;

    private final DTOConverter dtoConverter;

    private final IEventService eventService;

    private final IAuthenCommunicateService authenCommunicateService;

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
    public ResponseEntity<List<Event>> getListEvent() {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(eventService.getAllForUser(userDetail.getId()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetail> getMe() {

        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(userDetail);
    }
}