package com.gfa.tribesvibinandtribinotocyon.repositories;

import com.gfa.tribesvibinandtribinotocyon.models.roles.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNameIgnoreCase(String name);
}