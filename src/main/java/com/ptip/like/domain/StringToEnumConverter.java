package com.ptip.like.domain;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumConverter implements Converter<String, TargetType> {

    @Override
    public TargetType convert(String source) {
        return TargetType.parsing(source); // 여기서 예외 던짐
    }
}