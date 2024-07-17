package com.gfa.tribesvibinandtribinotocyon.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TribesBadRequestException extends Exception {

    public TribesBadRequestException(String message) {
        super(message);
    }

}