package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.Workspace;
import com.unigrad.funiverseappservice.payload.TermDTO;
import com.unigrad.funiverseappservice.service.IWorkspaceService;
import com.unigrad.funiverseappservice.util.DTOConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("workspace")
public class WorkspaceController {

    private final IWorkspaceService workspaceService;

    private final DTOConverter dtoConverter;

    public WorkspaceController(IWorkspaceService workspaceService, DTOConverter dtoConverter) {
        this.workspaceService = workspaceService;
        this.dtoConverter = dtoConverter;
    }

    @PostMapping
    private ResponseEntity<Workspace> save(@RequestBody Workspace workspace) {

        return ResponseEntity.ok(workspaceService.save(workspace));
    }

    @GetMapping("term/current")
    public ResponseEntity<TermDTO> getCurrent() {

        return ResponseEntity.ok(dtoConverter.convert(workspaceService.getCurrentTerm(), TermDTO.class));
    }
    @GetMapping("term/next")
    public ResponseEntity<TermDTO> getNextTerm() {

        return ResponseEntity.ok(dtoConverter.convert(workspaceService.getNextTerm(), TermDTO.class));
    }

    @PostMapping("term/start-new")
    public ResponseEntity<TermDTO> startNewTerm() {

        return ResponseEntity.ok(dtoConverter.convert(workspaceService.startNewTerm(), TermDTO.class));
    }
}