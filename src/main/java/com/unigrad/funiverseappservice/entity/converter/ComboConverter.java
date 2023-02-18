package com.unigrad.funiverseappservice.entity.converter;

import com.unigrad.funiverseappservice.entity.academic.Subject;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class ComboConverter implements AttributeConverter<List<Subject>, String> {

    @Override
    public String convertToDatabaseColumn(List<Subject> attribute) {

        if (attribute == null) {
            return null;
        }

        return attribute.stream()
                .map(Subject::getId)
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Subject> convertToEntityAttribute(String dbData) {

        if (dbData == null || dbData.isBlank()) {
            return null;
        }

        return Arrays
                .stream(dbData.split(","))
                .toList()
                .stream()
                .map(subjectId -> Subject
                        .builder()
                        .id(Long.valueOf(subjectId))
                        .build())
                .collect(Collectors.toList());
    }
}