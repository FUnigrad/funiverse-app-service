package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.Workspace;
import com.unigrad.funiverseappservice.entity.academic.*;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.Role;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.exception.InvalidActionException;
import com.unigrad.funiverseappservice.exception.InvalidValueException;
import com.unigrad.funiverseappservice.exception.ServiceCommunicateException;
import com.unigrad.funiverseappservice.payload.excel.*;
import com.unigrad.funiverseappservice.service.*;
import com.unigrad.funiverseappservice.util.ExcelUtil;
import com.unigrad.funiverseappservice.util.Utils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("import")
@RequiredArgsConstructor
public class ExcelController {

    private final Logger LOG = LoggerFactory.getLogger(ExcelController.class);

    private final IUserDetailService userDetailService;

    private final ICurriculumService curriculumService;

    private final IWorkspaceService workspaceService;

    private final ICurriculumPlanService curriculumPlanService;

    private final ISubjectService subjectService;

    private final ISyllabusService syllabusService;

    private final ITermService termService;

    private final ISpecializationService specializationService;

    private final IMajorService majorService;

    private final IGroupService groupService;

    private final IAuthenCommunicateService authenCommunicateService;

    @PostMapping
    @Transactional(rollbackOn = {InvalidActionException.class, EntityNotFoundException.class, InvalidValueException.class})
    public ResponseEntity<Map<String, List<Object>>> importData(@RequestParam("file")MultipartFile file, HttpServletRequest request) {

        String message = "";

        if (ExcelUtil.hasExcelFormat(file)) {
            try {
                Map<String, List<Object>> map = ExcelUtil.readObject(file.getInputStream());
                Map<String, List<Object>> result = new HashMap<>();
                Map<String, Curriculum> curriculumMap = new HashMap<>();
                Map<String, Subject> subjectMap = new HashMap<>();
                Map<String, Syllabus> syllabusMap = new HashMap<>();

                for (String s : ExcelUtil.SHEETS) {
                    List<Object> objects = new ArrayList<>();
                    for (Object o : map.get(s)) {
                        switch (o.getClass().getSimpleName()) {
                            case "MajorFlat" -> objects.add(majorService.save(buildMajor((MajorFlat) o)));
                            case "SpecializationFlat" -> objects.add(specializationService.save(buildSpecialization((SpecializationFlat) o)));
                            case "SubjectFlat" -> {
                                Subject subject = subjectService.save(buildSubject((SubjectFlat) o));
                                objects.add(subject);
                                subjectMap.put(((SubjectFlat) o).getId(), subject);
                            }
                            case "SyllabusFlat" -> {
                                Syllabus syllabus = syllabusService.save(buildSyllabus((SyllabusFlat) o, subjectMap));
                                objects.add(syllabus);
                                syllabusMap.put(((SyllabusFlat) o).getId(), syllabus);
                            }
                            case "CurriculumFlat" -> {
                                Curriculum curriculum = curriculumService.save(buildCurriculum((CurriculumFlat) o));
                                objects.add(curriculum);
                                curriculumMap.put(((CurriculumFlat) o).getId(), curriculum);
                            }
                            case "CurriculumPlanFlat" -> objects.add(curriculumPlanService.save(buildCurriculumPlan((CurriculumPlanFlat) o, curriculumMap, syllabusMap)));
                            case "UserFlat" -> {
                                UserDetail userDetail = buildUser((UserFlat) o, curriculumMap);
                                objects.add(userDetailService.save(userDetail));

                                String token = request.getHeader(HttpHeaders.AUTHORIZATION);

                                if (!authenCommunicateService.saveUser(userDetail, token)) {
                                    throw new ServiceCommunicateException("An error occurs when call to Authen Service");
                                }
                            }
                            case "GroupFlat" -> objects.add(groupService.save(buildGroup((GroupFlat) o, curriculumMap)));
                            default -> {
                                LOG.warn("No class found");
                            }
                            // Handle unknown class type
                        }
                    }

                    result.put(s, objects);
                }

                return ResponseEntity.ok(result);
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                throw new InvalidValueException(message, e);
            }
        }

        message = "Please upload an excel file!";
        throw new InvalidValueException(message);
    }

    @GetMapping
    public ResponseEntity<Void> generateTemplate(@RequestParam String entity, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=%s.xlsx".formatted(entity);
        response.setHeader(headerKey, headerValue);

        XSSFWorkbook workbook = ExcelUtil.getTemplate(entity);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();

        return ResponseEntity.ok().build();
    }

    private UserDetail buildUser(UserFlat userFlat, Map<String, Curriculum> curriculumMap) {
        UserDetail userDetail = UserDetail.builder()
                .name(userFlat.getName())
                .personalMail(userFlat.getPersonal_mail())
                .role(Role.valueOf(userFlat.getRole()))
                .identifyNumber(userFlat.getIdentify_number())
                .phoneNumber(userFlat.getPhone_number())
                .build();

        if (Role.STUDENT.equals(userDetail.getRole()) && userFlat.getCurriculum_code() != null) {
            Curriculum curriculum = curriculumMap.get(userFlat.getCurriculum_code());

            if (curriculum == null) {
                Optional<Curriculum> curriculumOptional = curriculumService.getCurriculumByCode(userFlat.getCurriculum_code());

                if (curriculumOptional.isEmpty()) {
                    throw new EntityNotFoundException("Curriculum code %s not found".formatted(userFlat.getCurriculum_code()));
                }

                curriculum = curriculumOptional.get();
            }
            userDetail.setCurriculum(curriculum);
            String userCode = userDetailService.generateStudentCode(curriculum.getSchoolYear(),
                    curriculum.getSpecialization().getStudentCode());
            userDetail.setCode(userCode);
            userDetail.setSchoolYear(curriculum.getSchoolYear());
            userDetail.setEduMail("%s%s@%s".formatted(Utils.generateUserCode(userDetail.getName()), userCode, workspaceService.getEmailSuffix()));
        } else if (!Role.STUDENT.equals(userDetail.getRole())){
            String userCode = userDetailService.generateUserCode(Utils.generateUserCode(userDetail.getName()));
            userDetail.setCode(userCode);
            userDetail.setEduMail("%s@%s".formatted(userCode, workspaceService.getEmailSuffix()));
        }


        return userDetailService.save(userDetail);
    }

    private Curriculum buildCurriculum(CurriculumFlat curriculumFlat) {
        return Curriculum.builder()
                .description(curriculumFlat.getDescription())
                .noSemester(Integer.valueOf(curriculumFlat.getNo_semester()))
                .startedTerm(termService.getOrCreate(curriculumFlat.getStart_term()))
                .specialization(specializationService.getByCode(curriculumFlat.getSpecialization_code())
                        .orElseThrow(() -> new EntityNotFoundException("Specialization code %s not found".formatted(curriculumFlat.getSpecialization_code()))))
                .build();

    }

    private CurriculumPlan buildCurriculumPlan(CurriculumPlanFlat curriculumPlanFlat, Map<String, Curriculum> curriculumMap, Map<String, Syllabus> syllabusMap) {
        Curriculum curriculum = curriculumMap.get(curriculumPlanFlat.getCurriculum_code());
        Syllabus syllabus = syllabusMap.get(curriculumPlanFlat.getSyllabus_code());
        CurriculumPlan curriculumPlan = CurriculumPlan.builder()
                .semester(Integer.valueOf(curriculumPlanFlat.getSemester()))
                .build();


        if (curriculum == null) {
            Optional<Curriculum> curriculumOptional = curriculumService.getCurriculumByCode(curriculumPlanFlat.getCurriculum_code());

            if (curriculumOptional.isEmpty()) {
                throw new EntityNotFoundException("Curriculum code %s not found".formatted(curriculumPlanFlat.getCurriculum_code()));
            }
            curriculum = curriculumOptional.get();
        }

        curriculumPlan.setCurriculum(curriculum);
        if (syllabus == null) {
            Optional<Syllabus> syllabusOptional = syllabusService.findByCode(curriculumPlanFlat.getSyllabus_code());

            if (syllabusOptional.isEmpty()) {
                throw new EntityNotFoundException("Syllabus code %s not found".formatted(curriculumPlanFlat.getSyllabus_code()));
            }
            syllabus = syllabusOptional.get();
        }
        curriculumPlan.setSyllabus(syllabus);
        return curriculumPlan;
    }

    private Group buildGroup(GroupFlat groupFlat, Map<String, Curriculum> curriculumMap) {
        Group group = Group.builder()
                .name(groupFlat.getName())
                .type(Group.Type.valueOf(groupFlat.getType()))
                .build();

        if (Group.Type.COURSE.equals(group.getType())) {
            throw new InvalidActionException("Do not allow to create Course");
        } else if (Group.Type.CLASS.equals(group.getType())) {
            if (groupFlat.getCurriculum_code().isEmpty()) {
                throw new InvalidActionException("Class must have Curriculum");
            }

            Curriculum curriculum = curriculumMap.get(groupFlat.getCurriculum_code());

            if (curriculum == null) {
                Optional<Curriculum> curriculumOptional = curriculumService.getCurriculumByCode(groupFlat.getCurriculum_code());

                if (curriculumOptional.isEmpty()) {
                    throw new EntityNotFoundException("Curriculum code %s not found".formatted(groupFlat.getCurriculum_code()));
                }

                group.setCurriculum(curriculumOptional.get());

                String name = curriculumOptional.get().getSpecialization().getCode() + curriculumOptional.get().getSchoolYear().substring(1);
                int order = groupService.getNextNameOrderForClass(name);
                name += String.format("%02d", order);
                group.setName(name);

            } else {
                group.setCurriculum(curriculum);

                String name = curriculum.getSpecialization().getCode() + curriculum.getSchoolYear().substring(1);
                int order = groupService.getNextNameOrderForClass(name);
                name += String.format("%02d", order);
                group.setName(name);
            }
        }

        return group;
    }

    private Major buildMajor(MajorFlat majorFlat) {
        return Major.builder()
                .code(majorFlat.getCode())
                .name(majorFlat.getName())
                .build();
    }

    private Specialization buildSpecialization(SpecializationFlat specializationFlat) {
        return Specialization.builder()
                .name(specializationFlat.getName())
                .code(specializationFlat.getCode())
                .studentCode(specializationFlat.getStudent_code())
                .major(majorService.findByCode(specializationFlat.getMajor_code())
                        .orElseThrow(() -> new EntityNotFoundException("Major code %s not found".formatted(specializationFlat.getMajor_code()))))
                .build();
    }

    private Subject buildSubject(SubjectFlat subjectFlat) {
        return Subject.builder()
                .code(subjectFlat.getCode())
                .name(subjectFlat.getName())
                .build();
    }

    private Syllabus buildSyllabus(SyllabusFlat syllabusFlat, Map<String, Subject> subjectMap) {
        Syllabus syllabus = Syllabus.builder()
                .name(syllabusFlat.getName())
                .code(syllabusFlat.getCode())
                .noCredit(Integer.valueOf(syllabusFlat.getNo_credit()))
                .noSlot(Integer.valueOf(syllabusFlat.getNo_slot()))
                .minAvgMarkToPass(Integer.valueOf(syllabusFlat.getMin_avg_mark_to_pass()))
                .description(syllabusFlat.getDescription())
                .build();

        Subject subject = subjectMap.get(syllabusFlat.getSubject_code());

        if (subject == null) {
            Optional<Subject> subjectOptional = subjectService.getByCode(syllabusFlat.getSubject_code());

            if (subjectOptional.isEmpty()) {
                throw new EntityNotFoundException("Subject code %s not found".formatted(syllabusFlat.getSubject_code()));
            }
            subject = subjectOptional.get();
        }

        syllabus.setSubject(subject);
        return syllabus;
    }
}