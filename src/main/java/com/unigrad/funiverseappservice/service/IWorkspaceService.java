package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.Workspace;
import com.unigrad.funiverseappservice.entity.academic.Term;

public interface IWorkspaceService {

    Workspace get();

    Workspace save(Workspace workspace);

    Term getCurrentTerm();

    Term getNextTerm();

    Term setStartDate(String startDateString);

    Term startNextTerm();

    Integer getSlotDurationInMin();

    Integer getRestTimeInMin();
}