package com.unigrad.funiverseappservice.service.impl;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.CurriculumPlan;
import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import com.unigrad.funiverseappservice.repository.ISyllabusRepository;
import com.unigrad.funiverseappservice.service.ICurriculumPlanService;
import com.unigrad.funiverseappservice.service.ICurriculumService;
import com.unigrad.funiverseappservice.service.ISyllabusService;
import com.unigrad.funiverseappservice.specification.EntitySpecification;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class SyllabusService implements ISyllabusService {

    private final ISyllabusRepository syllabusRepository;

    private final ICurriculumPlanService curriculumPlanService;

    private final ICurriculumService curriculumService;

    public SyllabusService(ISyllabusRepository syllabusRepository, ICurriculumPlanService curriculumPlanService, ICurriculumService curriculumService) {
        this.syllabusRepository = syllabusRepository;
        this.curriculumPlanService = curriculumPlanService;
        this.curriculumService = curriculumService;
    }

    @Override
    public List<Syllabus> getAll() {
        return syllabusRepository.findAll();
    }

    @Override
    public List<Syllabus> getAllActive() {
        return syllabusRepository.findAllByActiveIsTrue();
    }

    @Override
    public Optional<Syllabus> get(Long key) {
        return syllabusRepository.findById(key);
    }

    @Override
    public Syllabus save(Syllabus entity) {
        return syllabusRepository.save(entity);
    }

    @Override
    public List<Syllabus> saveAll(List<Syllabus> entities) {
        return syllabusRepository.saveAll(entities);
    }

    @Override
    @Transactional
    public void activate(Long key) {
        syllabusRepository.updateIsActive(key, true);
    }

    @Override
    @Transactional
    public void inactivate(Long key) {
        syllabusRepository.updateIsActive(key, false);
    }

    @Override
    public boolean isExist(Long key) {
        return syllabusRepository.existsById(key);
    }

    @Override
    public List<Syllabus> search(EntitySpecification<Syllabus> specification) {
        return syllabusRepository.findAll(specification);
    }


    /**
     * This function will find all combo plan and create new syllabus combo which will
     * be used to represent subject of combo
     *
     * @param curriculumId
     * @return list syllabi to which are used in curriculum plan
     */
    @Override
    public List<Syllabus> updateSyllabusCombo(long curriculumId, String comboCode) {
        List<CurriculumPlan> comboPlans = curriculumPlanService.getAllComboPlanByCurriculumId(curriculumId);
        comboPlans.sort(Comparator.comparing(CurriculumPlan::getSemester));
        Optional<Curriculum> curriculumOpt = curriculumService.get(curriculumId);

        Map<Integer, List<Syllabus>> syllabiBySemester = comboPlans.stream()
                .collect(Collectors.groupingBy(CurriculumPlan::getSemester,
                        Collectors.mapping(CurriculumPlan::getSyllabus, Collectors.toList())));
        String preComboCode = comboCode.substring(0, comboCode.length()-1);
        AtomicInteger index = new AtomicInteger(0);

        List<Syllabus> result = new ArrayList<>();

        //create syllabus combo
        for (Map.Entry<Integer, List<Syllabus>> entry : syllabiBySemester.entrySet()) {
            List<Syllabus> syllabi = entry.getValue();


            String code = "%s*%s".formatted(preComboCode, index.incrementAndGet());
            Optional<Syllabus> syllabusOpt = syllabusRepository.getByCodeEquals(code);

            Syllabus syllabus;

            syllabusOpt.ifPresent(value -> {
                curriculumPlanService.removeSyllabusFromCurriculum(value.getId(), curriculumId);
                syllabusRepository.deleteById(value.getId());
            });

            syllabus = Syllabus.builder()
                    .isSyllabusCombo(true)
                    .referenceSyllabusInCombo(syllabi)
                    .code(code)
                    .name("Subject %s of Combo".formatted(index.get()))
                    .description("Subject %s of Combo".formatted(index.get()))
                    .noCredit(syllabi.get(0).getNoCredit())
                    .minAvgMarkToPass(syllabi.get(0).getMinAvgMarkToPass())
                    .isActive(true)
                    .build();

            Syllabus newSyllabus = save(syllabus);
            //noinspection OptionalGetWithoutIsPresent


            CurriculumPlan curriculumPlan = CurriculumPlan.builder()
                    .curriculum(curriculumOpt.get())
                    .isComboPlan(false)
                    .syllabus(syllabus)
                    .semester(entry.getKey()).build();

            curriculumPlanService.save(curriculumPlan);

            result.add(newSyllabus);
        }
        return result;
    }

    @Override
    public List<Syllabus> getReadySyllabusForAdding(Long curriculumId) {
        return syllabusRepository.getReadySyllabusForAdding(curriculumId);
    }

    @Override
    public Optional<Syllabus> findByCode(String code) {
        return syllabusRepository.findByCode(code);
    }
}