package com.unigrad.funiverseappservice.service;

import com.unigrad.funiverseappservice.entity.academic.Slot;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ISlotService {

    List<Slot> getAllSlotsInGroup(Long syllabusId);

    Optional<Slot> get(Long key);

    Slot save(Slot slot);

    void delete(Long id);
}