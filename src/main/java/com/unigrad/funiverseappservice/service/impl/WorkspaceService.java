package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.Workspace;
import com.unigrad.funiverseappservice.entity.academic.Season;
import com.unigrad.funiverseappservice.entity.academic.Term;
import com.unigrad.funiverseappservice.repository.IWorkspaceRepository;
import com.unigrad.funiverseappservice.service.ISeasonService;
import com.unigrad.funiverseappservice.service.ITermService;
import com.unigrad.funiverseappservice.service.IWorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkspaceService implements IWorkspaceService {

    private Workspace workspace;

    private final IWorkspaceRepository workspaceRepository;

    private final ISeasonService seasonService;

    private final ITermService termService;

    @Override
    public Workspace get() {
        return workspaceRepository.findAll().get(0);
    }

    @Override
    public Workspace save(Workspace workspace) {
        if (workspace.getCurrentTerm() != null) {
            if (termService.get(workspace.getCurrentTerm().getSeason().getId(), workspace.getCurrentTerm().getYear()).isEmpty()) {
                Term currentTerm = newTerm(workspace.getCurrentTerm().getSeason(), workspace.getCurrentTerm().getYear());
                workspace.setCurrentTerm(currentTerm);
            } else {
                workspace.setCurrentTerm(termService.get(workspace.getCurrentTerm().getSeason().getId(), workspace.getCurrentTerm().getYear()).get());
            }
        }
        this.workspace = workspaceRepository.save(workspace);
        return workspace;
    }

    @Override
    public Term getCurrentTerm() {
        return get().getCurrentTerm();
    }

    @Override
    public Term startNewTerm(String startDateString) {
        LocalDate startDate = LocalDate.parse(startDateString, DateTimeFormatter.ISO_LOCAL_DATE);
        workspace = get();

        Term nextTerm = getNextTerm();
        nextTerm.setStartDate(startDate);
        workspace.setCurrentTerm(nextTerm);

        return save(workspace).getCurrentTerm();
    }

    @Override
    public Term getNextTerm() {
        Term currentTerm = getCurrentTerm();
        Season currentSeason = currentTerm.getSeason();
        Season nextSeason = seasonService.getNextSeasonOf(currentSeason);

        String currentYear = currentTerm.getYear();
        String nextYear = seasonService.isLastSeason(currentSeason.getOrdinalNumber())
                ? String.valueOf(Integer.parseInt(currentYear) + 1)
                : currentYear;

        Optional<Term> nextTermOptional = termService.get(nextSeason.getId(), nextYear);

        return nextTermOptional.orElseGet(() -> termService.save(new Term(nextSeason, nextYear)));
    }

    private Term newTerm(Season season, String year) {

        return termService.save(new Term(season, year));
    }

    @Override
    public Integer getSlotDurationInMin() {
        return workspaceRepository.getSlotDurationInMin();
    }

    @Override
    public Integer getRestTimeInMin() {
        return workspaceRepository.getRestTimeInMin();
    }
}