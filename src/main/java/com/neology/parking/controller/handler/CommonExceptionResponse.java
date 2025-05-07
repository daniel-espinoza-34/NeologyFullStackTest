package com.neology.parking.controller.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonExceptionResponse {
    private final int status;
    private final String errorMessage;
}
