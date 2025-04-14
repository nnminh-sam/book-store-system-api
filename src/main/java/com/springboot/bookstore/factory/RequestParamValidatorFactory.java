package com.springboot.bookstore.factory;

import com.springboot.bookstore.dto.BaseRequestDto;

public class RequestParamValidatorFactory {
    public static void validateDto(BaseRequestDto dto) {
        dto.validateSortBy();
    }
}
