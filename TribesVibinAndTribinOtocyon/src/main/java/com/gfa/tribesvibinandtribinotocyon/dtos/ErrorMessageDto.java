package com.gfa.tribesvibinandtribinotocyon.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessageDto {
    private String message;
    private LocalDateTime timestamp;
}
