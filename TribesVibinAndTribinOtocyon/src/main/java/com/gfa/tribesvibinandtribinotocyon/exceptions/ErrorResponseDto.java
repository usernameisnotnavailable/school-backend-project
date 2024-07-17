package com.gfa.tribesvibinandtribinotocyon.exceptions;

import lombok.Data;

/**
 * Following fields are currently used in RestExceptionHandler:
 *  - Integer statusCode
 *  - String message
 *  - String timestamp - this is a String type due to the formatting which is happening in RestExceptionHandler
 *
 * Fields that can be subject to discussion and can be added into RestExceptionHandler later:
 *  - String errorCode
 *  - String username
 *  - String endpoint
 */


@Data
public class ErrorResponseDto {

    private Integer statusCode;
    private String message;
    private String timestamp;
    private String errorCode;
    private String username;
    private String endpoint;

}