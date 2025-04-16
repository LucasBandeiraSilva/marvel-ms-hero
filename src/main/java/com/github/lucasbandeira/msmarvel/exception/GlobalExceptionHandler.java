package com.github.lucasbandeira.msmarvel.exception;

import com.github.lucasbandeira.msmarvel.model.dto.ApiFieldError;
import com.github.lucasbandeira.msmarvel.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleMethodArgumentNotValidException( MethodArgumentNotValidException exception ){
        List<FieldError>fieldErrors = exception.getFieldErrors();
        List<ApiFieldError>fieldErrorList = fieldErrors
                .stream()
                .map(fieldError -> new ApiFieldError(fieldError.getField(),fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),"Validation Error!",fieldErrorList);
    }

    @ExceptionHandler(HeroNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleHeroNotFoundException(HeroNotFoundException exception){
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(), List.of());
    }

    @ExceptionHandler(DuplicateHeroException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateHeroException(DuplicateHeroException exception){
        return new ErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage(),List.of());
    }
}
