package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.Workspace;
import com.unigrad.funiverseappservice.entity.academic.Term;
import com.unigrad.funiverseappservice.repository.IWorkspaceRepository;
import com.unigrad.funiverseappservice.service.ITermService;
import com.unigrad.funiverseappservice.service.IWorkspaceService;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceService implements IWorkspaceService {

    private Workspace workspace;

    private final IWorkspaceRepository workspaceRepository;

    private final ITermService termService;

    public WorkspaceService(IWorkspaceRepository workspaceRepository, ITermService termService) {
        this.workspaceRepository = workspaceRepository;
        this.termService = termService;
    }

    @Override
    public Workspace get() {
        return workspace;
    }

    @Override
    public Workspace save(Workspace workspace) {
        Term newTerm = termService.get(workspace.getCurrentTerm().getSeason(), workspace.getCurrentTerm().getYear()).orElseGet(() -> termService.save(workspace.getCurrentTerm()));
        workspace.setCurrentTerm(newTerm);

        this.workspace = workspaceRepository.save(workspace);
        return workspace;
    }

    @Override
    public Term getCurrentTerm() {
        return workspace.getCurrentTerm();
    }

    @Override
    public Term startNewTerm() {
        workspace.setCurrentTerm(getNextTerm());

        return save(workspace).getCurrentTerm();
    }

    @Override
    public Term getNextTerm() {
        Term currentTerm = getCurrentTerm();
        Term.Season currentSeason = currentTerm.getSeason();
        Term.Season nextSeason;
        String currentYear = currentTerm.getYear();
        String nextYear;

        switch (currentSeason) {
            case SPRING -> {
                nextSeason = Term.Season.SUMMER;
                nextYear = currentYear;
            }
            case SUMMER -> {
                nextSeason = Term.Season.FALL;
                nextYear = currentYear;
            }
            default -> {
                nextSeason = Term.Season.SPRING;
                nextYear = String.valueOf(Integer.parseInt(currentYear) + 1);
            }
        }

        return new Term(nextSeason, nextYear);
    }
}