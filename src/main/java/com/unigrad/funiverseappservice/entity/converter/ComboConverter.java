package com.unigrad.funiverseappservice.entity.converter;

import com.unigrad.funiverseappservice.entity.academic.Subject;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;
import java.util.stream.Collectors;

@Converter
public class ComboConverter implements AttributeConverter<List<Subject>, String> {

    @Override
    public String convertToDatabaseColumn(List<Subject> attribute) {
        return attribute.stream()
                .map(Subject::getId)
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Subject> convertToEntityAttribute(String dbData) {
        return null;
    }
}