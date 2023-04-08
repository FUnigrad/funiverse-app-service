package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Slot;
import com.unigrad.funiverseappservice.repository.ISlotRepository;
import com.unigrad.funiverseappservice.service.ISlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class
SlotService implements ISlotService {

    private final ISlotRepository slotRepository;

    @Override
    public Optional<Slot> get(Long key) {
        return slotRepository.findById(key);
    }

    @Override
    public Slot save(Slot entity) {
        return slotRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        slotRepository.deleteById(id);
    }

    @Override
    public List<Slot> getAllSlotsInGroup(Long syllabusId) {
        return slotRepository.getAllSlotsByGroupId(syllabusId);
    }
}