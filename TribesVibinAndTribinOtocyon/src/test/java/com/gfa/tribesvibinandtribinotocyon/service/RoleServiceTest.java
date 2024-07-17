package com.gfa.tribesvibinandtribinotocyon.service;

import com.gfa.tribesvibinandtribinotocyon.models.roles.Role;
import com.gfa.tribesvibinandtribinotocyon.models.roles.RoleAdmin;
import com.gfa.tribesvibinandtribinotocyon.models.roles.RoleUser;
import com.gfa.tribesvibinandtribinotocyon.models.roles.RoleVisitor;
import com.gfa.tribesvibinandtribinotocyon.repositories.RoleRepository;
import com.gfa.tribesvibinandtribinotocyon.services.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    public RoleServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void onApplicationEvent_shouldInitializeRoles() {
        when(roleRepository.findByNameIgnoreCase("visitor")).thenReturn(Optional.empty());
        when(roleRepository.findByNameIgnoreCase("user")).thenReturn(Optional.empty());
        when(roleRepository.findByNameIgnoreCase("admin")).thenReturn(Optional.empty());

        roleService.onApplicationEvent(mock(ApplicationReadyEvent.class));

        verify(roleRepository, times(3)).save(any(Role.class));
    }

    @Test
    public void initializeRoles_shouldNotCreateRoleIfAlreadyExists() {
        when(roleRepository.findByNameIgnoreCase("visitor")).thenReturn(Optional.of(new RoleVisitor()));
        when(roleRepository.findByNameIgnoreCase("user")).thenReturn(Optional.of(new RoleUser()));
        when(roleRepository.findByNameIgnoreCase("admin")).thenReturn(Optional.of(new RoleAdmin()));

        roleService.initializeRoles();

        verify(roleRepository, never()).save(any(RoleVisitor.class));
        verify(roleRepository, never()).save(any(RoleUser.class));
        verify(roleRepository, never()).save(any(RoleAdmin.class));
    }

    @Test
    public void createRoleIfNotExists_shouldThrowExceptionForUnknownRoleType() {
        assertThrows(IllegalArgumentException.class, () -> roleService.createRoleIfNotExists("unknownRole"));
    }

    @Test
    public void createRoleIfNotExists_shouldCreateVisitorRoleIfNotExists() {
        when(roleRepository.findByNameIgnoreCase("visitor")).thenReturn(Optional.empty());

        roleService.createRoleIfNotExists("visitor");

        verify(roleRepository, times(1)).save(any(RoleVisitor.class));
    }

    @Test
    public void createRoleIfNotExists_shouldNotCreateVisitorRoleIfExists() {
        when(roleRepository.findByNameIgnoreCase("visitor")).thenReturn(Optional.of(new RoleVisitor()));

        roleService.createRoleIfNotExists("visitor");

        verify(roleRepository, never()).save(any(RoleVisitor.class));
    }
}
