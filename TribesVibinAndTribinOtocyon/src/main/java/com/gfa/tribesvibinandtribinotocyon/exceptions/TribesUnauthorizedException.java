package com.gfa.tribesvibinandtribinotocyon.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TribesUnauthorizedException extends Exception {

    public TribesUnauthorizedException(String message) {
        super(message);
    }

}