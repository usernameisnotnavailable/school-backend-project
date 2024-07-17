package com.gfa.tribesvibinandtribinotocyon.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtValidationResult {
    private final boolean isValid;
    private final String message;

    public boolean isValid() {
        return isValid;
    }
}