package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.models.roles.*;
import com.gfa.tribesvibinandtribinotocyon.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements ApplicationListener<ApplicationReadyEvent> {

    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initializeRoles();
    }

    public void initializeRoles() {
        createRoleIfNotExists("visitor");
        createRoleIfNotExists("user");
        createRoleIfNotExists("admin");
    }

    public void createRoleIfNotExists(String roleName) {
        Optional<Role> roleOpt = roleRepository.findByNameIgnoreCase(roleName);
        Role role;
        if (roleOpt.isEmpty()) {
            if (roleName.equals("visitor")) {
                role = new RoleVisitor();
            } else if (roleName.equals("user")) {
                role = new RoleUser();
            } else if (roleName.equals("admin")) {
                role = new RoleAdmin();
            } else {
                throw new IllegalArgumentException("Unknown role type: " + roleName);
            }
            role.setName(roleName);
            roleRepository.save(role);
        }
    }

}