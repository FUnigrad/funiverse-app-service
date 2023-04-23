package com.unigrad.funiverseappservice.util;

import com.unigrad.funiverseappservice.entity.academic.Curriculum;
import com.unigrad.funiverseappservice.entity.academic.CurriculumPlan;
import com.unigrad.funiverseappservice.entity.academic.Major;
import com.unigrad.funiverseappservice.entity.academic.Specialization;
import com.unigrad.funiverseappservice.entity.academic.Subject;
import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import com.unigrad.funiverseappservice.entity.socialnetwork.Group;
import com.unigrad.funiverseappservice.entity.socialnetwork.UserDetail;
import com.unigrad.funiverseappservice.exception.InvalidValueException;
import com.unigrad.funiverseappservice.payload.excel.UserFlat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ExcelUtil {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static List<String> SHEETS = List.of("MAJOR", "SPECIALIZATION",
                                                "SUBJECT", "SYLLABUS", "CURRICULUM", "CURRICULUM_PLAN",
                                                "USER", "GROUP");

    public static List<Class<?>> CLAZZ = List.of(Major.class, Specialization.class,
                                                 Subject.class, Syllabus.class, Curriculum.class, CurriculumPlan.class,
                                                 UserFlat.class, Group.class);

    public static boolean hasExcelFormat(MultipartFile file) {

        return TYPE.equals(file.getContentType());
    }

    public static List<Object> readObject(InputStream is) {
        try {
            List<Object> result = new ArrayList<>();
            Workbook workbook = new XSSFWorkbook(is);

            for (String sheetName : SHEETS) {
                Sheet sheet = workbook.getSheet(sheetName);

                if (sheet == null) {
                    continue;
                }

                Iterator<Row> rows = sheet.iterator();

                List<String> header = new ArrayList<>();

                if (rows.hasNext()) {
                    Row headerRow = rows.next();

                    int cellNum = 1;
                    for (Cell currentCell : headerRow) {
                        if (Utils.hasField(CLAZZ.get(SHEETS.indexOf(sheetName)),currentCell.getStringCellValue())) {
                            header.add(currentCell.getStringCellValue());
                            cellNum++;
                        } else {
                            throw new InvalidValueException("Invalid header at column %s: %s do not have field %s".formatted(cellNum, sheetName, currentCell.getStringCellValue()));
                        }
                    }
                }

                int rowNumber = 2;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();

                    try {
                        result.add(makeUser(currentRow, header));
                    } catch (RuntimeException e) {
                        throw new InvalidValueException("An error occurred when parse data at line %s".formatted(rowNumber), e);
                    }

                    rowNumber++;
                }
            }

            workbook.close();

            return result;
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static XSSFWorkbook getTemplate(String object) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        if ("User".equals(object)) {
            XSSFSheet sheet = workbook.createSheet("USER");
            Row row = sheet.createRow(0);

            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            font.setFontHeight(16);
            style.setFont(font);

            int cellNum = 0;
            for (String field : fields("USER")) {
                Cell cell = row.createCell(cellNum);
                cell.setCellValue(field);
                cell.setCellStyle(style);
                sheet.autoSizeColumn(cellNum);
                cellNum++;
            }
        }

        return workbook;
    }

    private static List<String> fields (String clazz) {
        List<String> result = new ArrayList<>();
        switch (clazz) {
            case "MAJOR", "SUBJECT" -> result = List.of("id", "code", "name");
            case "SPECIALIZATION" -> result = List.of("id", "code", "name", "student_code", "major_id");
            case "SYLLABUS" -> result = List.of("id", "code", "name", "description", "min_avg_mark_to_pass", "no_credit", "subject_id");
            case "CURRICULUM" -> result = List.of("id", "name", "description", "no_semester", "specialization_id");
            case "CURRICULUM_PLAN" -> result = List.of("curriculum_id", "syllabus_id", "semester");
            case "USER" -> result = List.of("id", "name", "personal_mail", "phone_number", "identify_number", "role", "curriculum_code");
            default -> throw new IllegalStateException("Unexpected value: " + clazz);
        }

        return result;
    }

    private static UserFlat makeUser(Row row, List<String> headers) throws NoSuchFieldException, IllegalAccessException {
        UserFlat userDetail = new UserFlat();

        int cellNum = 0;
        for (Cell cell : row) {
            DataFormatter formatter = new DataFormatter();

            String value = formatter.formatCellValue(cell);
            Utils.setValue(userDetail, UserFlat.class, headers.get(cellNum), value);

            cellNum++;
        }

        return userDetail;
    }
}