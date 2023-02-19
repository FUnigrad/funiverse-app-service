package com.unigrad.funiverseappservice.entity.converter;

import com.unigrad.funiverseappservice.entity.academic.Syllabus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class SyllabusConverter implements AttributeConverter<List<Syllabus>, String> {

    @Override
    public String convertToDatabaseColumn(List<Syllabus> attribute) {

        if (attribute == null) {
            return null;
        }

        return attribute.stream()
                .map(Syllabus::getId)
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Syllabus> convertToEntityAttribute(String dbData) {

        if (dbData == null || dbData.isBlank()) {
            return null;
        }

        return Arrays
                .stream(dbData.split(","))
                .toList()
                .stream()
                .map(syllabusId -> Syllabus
                        .builder()
                        .id(Long.valueOf(syllabusId))
                        .build())
                .collect(Collectors.toList());
    }
}