package com.gfa.tribesvibinandtribinotocyon.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Each method in RestExceptionHandler has two parameters: particular Exception and WebRequest. WebRequest parameter is not used in the current version of code, however it might be used later when code evolves, or it can be deprecated.
 */

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {TribesNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponseDto handleTribesNotFoundException(
            TribesNotFoundException ex,
            WebRequest request) {
        String message =
                ex.getMessage() == null || ex.getMessage().isBlank() ?
                        "Sorry, the requested resource was not found." :
                        ex.getMessage();
        return createErrorResponse(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(value = {TribesInternalServerErrorException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponseDto handleTribesInternalServerErrorException(
            TribesInternalServerErrorException ex,
            WebRequest request) {

        String message =
                ex.getMessage() == null || ex.getMessage().isBlank() ?
                        "Oops! Something went wrong. Our team has been notified about this issue. Please try again later." :
                        ex.getMessage();
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    @ExceptionHandler(value = {TribesUnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponseDto handleTribesUnauthorizedException(
            TribesUnauthorizedException ex,
            WebRequest request) {

        String message =
                ex.getMessage() == null || ex.getMessage().isBlank() ?
                        "Access Denied. Please log in to access this resource." :
                        ex.getMessage();
        return createErrorResponse(HttpStatus.UNAUTHORIZED, message);
    }

    @ExceptionHandler(value = {TribesForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ErrorResponseDto handleTribesForbiddenException(
            TribesForbiddenException ex,
            WebRequest request) {

        String message =
                ex.getMessage() == null || ex.getMessage().isBlank() ?
                        "Access Forbidden. You don't have permission to access this resource." :
                        ex.getMessage();
        return createErrorResponse(HttpStatus.FORBIDDEN, message);
    }

    @ExceptionHandler(value = {TribesBadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponseDto handleTribesBadRequestException(
            TribesBadRequestException ex,
            WebRequest request) {

        String message =
                ex.getMessage() == null || ex.getMessage().isBlank() ?
                        "Invalid request. Please check your input and try again." :
                        ex.getMessage();
        return createErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.ENGLISH);
        return dateFormat.format(date);
    }

    private ErrorResponseDto createErrorResponse(HttpStatus status, String message) {
        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.setStatusCode(status.value());
        errorResponse.setMessage(message);
        errorResponse.setTimestamp(formatDate(new Date()));
        return errorResponse;
    }

}