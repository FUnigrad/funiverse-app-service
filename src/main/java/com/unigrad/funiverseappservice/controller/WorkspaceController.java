package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.Workspace;
import com.unigrad.funiverseappservice.entity.academic.Term;
import com.unigrad.funiverseappservice.payload.request.OnBoardingRequest;
import com.unigrad.funiverseappservice.payload.request.StartDateRequest;
import com.unigrad.funiverseappservice.service.IWorkspaceService;
import com.unigrad.funiverseappservice.util.DTOConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("workspace")
public class WorkspaceController {

    private final IWorkspaceService workspaceService;

    private final DTOConverter dtoConverter;

    public WorkspaceController(IWorkspaceService workspaceService, DTOConverter dtoConverter) {
        this.workspaceService = workspaceService;
        this.dtoConverter = dtoConverter;
    }

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

    @PostMapping("term/start-new")
    public ResponseEntity<Term> startNewTerm(@RequestBody StartDateRequest startDateRequest) {

        return ResponseEntity.ok(workspaceService.startNewTerm(startDateRequest.getStartDate()));
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
}