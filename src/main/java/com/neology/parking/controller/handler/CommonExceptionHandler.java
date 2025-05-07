package com.neology.parking.controller.handler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.neology.parking.util.exception.CommonException;
import com.neology.parking.util.exception.CommonNotFoundException;
import com.neology.parking.util.exception.CommonUnathorizedException;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(exception = {
            CommonException.class,
            CommonNotFoundException.class,
            CommonUnathorizedException.class,
            Exception.class
    }, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonExceptionResponse> handleCommonNotFoundException(Exception exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (exception instanceof CommonException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof CommonNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (exception instanceof CommonUnathorizedException) {
            status = HttpStatus.UNAUTHORIZED;
        }
        return new ResponseEntity<>(new CommonExceptionResponse(status.value(), exception.getMessage()), status);
    }

    @ExceptionHandler(exception = MethodArgumentNotValidException.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonValidationResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        List<String> errors = ex.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList();
        if (errors.size() < 2) {
            return new ResponseEntity<>(new CommonValidationResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    errors.getFirst(),
                    null),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new CommonValidationResponse(
                HttpStatus.BAD_REQUEST.value(),
                "La peticion contiene errores",
                errors),
                HttpStatus.BAD_REQUEST);
    }
}
