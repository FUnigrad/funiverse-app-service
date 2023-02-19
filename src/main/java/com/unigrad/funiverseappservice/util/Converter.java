package com.unigrad.funiverseappservice.util;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Converter {

    private final ModelMapper modelMapper;

    public <T, U> U convertToDTO(T source, Class<U> targetClass){
        return modelMapper.map(source, targetClass);
    }
}