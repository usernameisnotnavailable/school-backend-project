package com.gfa.tribesvibinandtribinotocyon.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TribesNotFoundException extends Exception {

    public TribesNotFoundException(String message) {
        super(message);
    }

}