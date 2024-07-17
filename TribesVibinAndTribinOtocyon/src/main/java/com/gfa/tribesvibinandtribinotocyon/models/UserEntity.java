package com.gfa.tribesvibinandtribinotocyon.models;

import com.gfa.tribesvibinandtribinotocyon.services.emailValidagtion.ConfirmationToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.gfa.tribesvibinandtribinotocyon.models.roles.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    @Value("false")
    private boolean enabled;

    @OneToOne
    private Kingdom kingdom;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne
    @JoinColumn(name = "token_id")
    private ConfirmationToken confirmationToken;
}