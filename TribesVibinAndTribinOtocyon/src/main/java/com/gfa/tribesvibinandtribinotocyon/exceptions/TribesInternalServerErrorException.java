package com.gfa.tribesvibinandtribinotocyon.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TribesInternalServerErrorException extends Exception {

    public TribesInternalServerErrorException(String message) {
        super(message);
    }

}