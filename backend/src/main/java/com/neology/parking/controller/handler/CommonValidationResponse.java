package com.neology.parking.controller.handler;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonValidationResponse {
    private final int status;
    private final String errorMessage;
    private final List<String> errorDetail;
}
