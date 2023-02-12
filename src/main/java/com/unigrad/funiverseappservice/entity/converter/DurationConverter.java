package com.unigrad.funiverseappservice.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter
public  class DurationConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        return attribute.toMinutes();
    }

    @Override
    public Duration convertToEntityAttribute(Long dbData) {
        return Duration.ofMinutes(dbData);
    }
}