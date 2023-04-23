package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.Workspace;
import com.unigrad.funiverseappservice.entity.academic.Season;
import com.unigrad.funiverseappservice.entity.academic.Term;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.payload.DTO.PostDTO;
import com.unigrad.funiverseappservice.payload.DTO.UserDTO;
import com.unigrad.funiverseappservice.payload.request.OnBoardingRequest;
import com.unigrad.funiverseappservice.payload.request.SeasonOnboardRequest;
import com.unigrad.funiverseappservice.service.IPostService;
import com.unigrad.funiverseappservice.service.ISeasonService;
import com.unigrad.funiverseappservice.service.IWorkspaceService;
import com.unigrad.funiverseappservice.service.impl.UserDetailService;
import com.unigrad.funiverseappservice.util.DTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("workspace")
@RequiredArgsConstructor
public class WorkspaceController {

    private final IWorkspaceService workspaceService;

    private final UserDetailService userDetailService;

    private final ISeasonService seasonService;

    private final IPostService postService;

    private final DTOConverter dtoConverter;

    @GetMapping
    private ResponseEntity<Workspace> get() {

        return ResponseEntity.ok(workspaceService.get());
    }

    @PostMapping
    private ResponseEntity<Workspace> save(@RequestBody Workspace workspace) {

        return ResponseEntity.ok(workspaceService.save(workspace));
    }

    @PutMapping
    private ResponseEntity<Workspace> update(@RequestBody Workspace workspace) {

        if (!workspaceService.get().getId().equals(workspace.getId())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(workspaceService.save(workspace));
    }

    @GetMapping("term/current")
    public ResponseEntity<Term> getCurrent() {

        return ResponseEntity.ok(workspaceService.getCurrentTerm());
    }
    @GetMapping("term/next")
    public ResponseEntity<Term> getNextTerm() {

        return ResponseEntity.ok(workspaceService.getNextTerm());
    }

    @PostMapping("onboarding")
    public ResponseEntity<Void> onboarding(@RequestBody OnBoardingRequest request) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("no-slot")
    public ResponseEntity<Long> getNoSlot() {
        Workspace workspace = workspaceService.get();
        Integer slotDurationInMin = workspace.getSlotDurationInMin();
        Integer restTimeInMin = workspace.getRestTimeInMin();
        long hoursDiff = workspace.getMorningStartTime().until(workspace.getMorningEndTime(), ChronoUnit.MINUTES)
                + workspace.getAfternoonStartTime().until(workspace.getAfternoonEndTime(), ChronoUnit.MINUTES);

        return ResponseEntity.ok((hoursDiff + restTimeInMin)/(slotDurationInMin + restTimeInMin));
    }

    @GetMapping("user")
    public ResponseEntity<List<UserDTO>> getAllUser() {

        List<UserDetail> userDetails = userDetailService.getAllActive();

        return ResponseEntity.ok().body(Arrays.stream(dtoConverter.convert(userDetails, UserDTO[].class)).toList());
    }

    @GetMapping("post")
    public ResponseEntity<Page<PostDTO>> getNewFeed(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        return ResponseEntity.ok(postService.getAllPostForNewFeed(userDetail.getId(), PageRequest.of(page, size)));
    }

    @PostMapping("season")
    public ResponseEntity<List<Season>> save(@RequestBody SeasonOnboardRequest seasonOnboardRequest) {
        List<Season> newSeasons = seasonOnboardRequest.getSeason().stream().map(seasonService::save).toList();

        return ResponseEntity.ok(newSeasons);
    }
}