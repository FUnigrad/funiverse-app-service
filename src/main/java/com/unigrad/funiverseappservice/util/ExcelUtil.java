package com.unigrad.funiverseappservice.util;

import com.unigrad.funiverseappservice.exception.InvalidValueException;
import com.unigrad.funiverseappservice.payload.excel.CurriculumFlat;
import com.unigrad.funiverseappservice.payload.excel.CurriculumPlanFlat;
import com.unigrad.funiverseappservice.payload.excel.GroupFlat;
import com.unigrad.funiverseappservice.payload.excel.MajorFlat;
import com.unigrad.funiverseappservice.payload.excel.SpecializationFlat;
import com.unigrad.funiverseappservice.payload.excel.SubjectFlat;
import com.unigrad.funiverseappservice.payload.excel.SyllabusFlat;
import com.unigrad.funiverseappservice.payload.excel.UserFlat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static List<String> SHEETS = List.of("MAJOR", "SPECIALIZATION",
                                                "SUBJECT", "SYLLABUS", "CURRICULUM", "CURRICULUM_PLAN",
                                                "USER", "GROUP");

    public static List<Class<?>> CLAZZ = List.of(MajorFlat.class, SpecializationFlat.class,
                                                 SubjectFlat.class, SyllabusFlat.class, CurriculumFlat.class, CurriculumPlanFlat.class,
                                                 UserFlat.class, GroupFlat.class);

    public static boolean hasExcelFormat(MultipartFile file) {

        return TYPE.equals(file.getContentType());
    }

    public static Map<String, List<Object>> readObject(InputStream is) {
        try {
            Map<String, List<Object>> result = new HashMap<>();
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
                        DataFormatter formatter = new DataFormatter();
                        String value = formatter.formatCellValue(currentCell);

                        if (value.isEmpty()) {
                            break;
                        }

                        if (Utils.hasField(CLAZZ.get(SHEETS.indexOf(sheetName)),value)) {
                            header.add(currentCell.getStringCellValue());
                            cellNum++;
                        } else {
                            throw new InvalidValueException("Invalid header at column %s: %s do not have field %s".formatted(cellNum, sheetName, currentCell.getStringCellValue()));
                        }
                    }
                }

                List<Object> objects = new ArrayList<>();
                int rowNumber = 2;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    DataFormatter formatter = new DataFormatter();
                    String valueFirstCell = formatter.formatCellValue(currentRow.getCell(0));
                    if (valueFirstCell.isEmpty()) {
                        break;
                    }

                    try {
                        objects.add(make(currentRow, header, CLAZZ.get(SHEETS.indexOf(sheetName))));
                    } catch (RuntimeException e) {
                        throw new InvalidValueException("An error occurred when parse data at line %s at sheet %s".formatted(rowNumber, sheetName), e);
                    }

                    rowNumber++;
                }

                result.put(sheetName, objects);
            }

            workbook.close();

            return result;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    public static XSSFWorkbook getTemplate(String object) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        if ("All".equals(object)) {
            for (String sheet : SHEETS) {
                createSheet(sheet, workbook);
            }
        }

        return workbook;
    }

    private static void createSheet(String object, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet(object);
        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        int cellNum = 0;
        for (String field : fields(object)) {
            Cell cell = row.createCell(cellNum);
            cell.setCellValue(field);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(cellNum);
            cellNum++;
        }
    }

    private static List<String> fields (String clazz) {
        List<String> result = new ArrayList<>();
        switch (clazz) {
            case "MAJOR", "SUBJECT" -> result = List.of("id", "code", "name");
            case "SPECIALIZATION" -> result = List.of("id", "code", "name", "student_code", "major_code");
            case "SYLLABUS" -> result = List.of("id", "code", "name", "description", "min_avg_mark_to_pass", "no_credit", "subject_code", "no_slot");
            case "CURRICULUM" -> result = List.of("id", "description", "no_semester", "specialization_code", "start_term");
            case "CURRICULUM_PLAN" -> result = List.of("curriculum_code", "syllabus_code", "semester");
            case "USER" -> result = List.of("id", "name", "personal_mail", "phone_number", "identify_number", "role", "curriculum_code");
            case "GROUP" -> result = List.of("id", "name", "type", "curriculum_code");
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

    private static Object make(Row row, List<String> headers, Class<?> clazz) {
        Object object = null;
        try {
            object = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        int cellNum = 0;
        for (Cell cell : row) {

            DataFormatter formatter = new DataFormatter();

            String value = formatter.formatCellValue(cell);
            if (value == null || value.isEmpty()) {
                break;
            }
            try {
                Utils.setValue(object, clazz, headers.get(cellNum), value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("An error occurred when set value at %s in %s".formatted(cell.getAddress(), cell.getSheet()), e);
            }

            cellNum++;
        }
        return object;
    }
}