package com.gfa.tribesvibinandtribinotocyon.services.emailValidagtion;

import com.gfa.tribesvibinandtribinotocyon.models.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @SequenceGenerator(
            name="confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "confirmation_token_sequence")
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime localDateTime;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    @Value("false")
    private boolean confirmed;

    public ConfirmationToken(String token,
                             LocalDateTime localDateTime,
                             LocalDateTime expiredAt) {
        this.token = token;
        this.localDateTime = localDateTime;
        this.expiresAt = expiredAt;
    }

}
