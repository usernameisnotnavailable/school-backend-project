package com.gfa.tribesvibinandtribinotocyon.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TribesForbiddenException extends Exception {

    public TribesForbiddenException(String message) {
        super(message);
    }

}