package com.unigrad.funiverseappservice.controller;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.socialnetwork.Role;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.exception.InvalidValueException;
import com.unigrad.funiverseappservice.payload.excel.UserFlat;
import com.unigrad.funiverseappservice.service.ICurriculumService;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import com.unigrad.funiverseappservice.service.IWorkspaceService;
import com.unigrad.funiverseappservice.util.ExcelUtil;
import com.unigrad.funiverseappservice.util.Utils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("import")
@RequiredArgsConstructor
public class ExcelController {

    private final IUserDetailService userDetailService;

    private final ICurriculumService curriculumService;

    private final IWorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<List<UserDetail>> importData(@RequestParam("file")MultipartFile file) {

        String message = "";

        if (ExcelUtil.hasExcelFormat(file)) {
            try {
                List<Object> data = ExcelUtil.readObject(file.getInputStream());
                List<UserDetail> user = new ArrayList<>();

                for (Object o : data) {
                    if (o instanceof UserFlat) {
                        user.add(buildUser((UserFlat) o));
                    }
                }

                return ResponseEntity.ok(user);
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

    private UserDetail buildUser(UserFlat userFlat) {
        UserDetail userDetail = UserDetail.builder()
                .name(userFlat.getName())
                .personalMail(userFlat.getPersonal_mail())
                .role(Role.valueOf(userFlat.getRole()))
                .identifyNumber(userFlat.getIdentify_number())
                .phoneNumber(userFlat.getPhone_number())
                .build();

        if (Role.STUDENT.equals(userDetail.getRole()) && userFlat.getCurriculum_code()!=null) {
            Optional<Curriculum> curriculumOptional = curriculumService.getCurriculumByCode(userFlat.getCurriculum_code());

            if (curriculumOptional.isEmpty()) {
                throw new InvalidValueException("Curriculum %s not exist".formatted(userFlat.getCurriculum_code()));
            }

            Curriculum curriculum = curriculumOptional.get();
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
}