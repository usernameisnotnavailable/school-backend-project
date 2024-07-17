package com.gfa.tribesvibinandtribinotocyon.controllers;

import com.gfa.tribesvibinandtribinotocyon.dtos.AdminAccessKingdomDTO;
import com.gfa.tribesvibinandtribinotocyon.dtos.BuildingDTO;
import com.gfa.tribesvibinandtribinotocyon.dtos.StockpileDTO;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesBadRequestException;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesUnauthorizedException;
import com.gfa.tribesvibinandtribinotocyon.models.Kingdom;
import com.gfa.tribesvibinandtribinotocyon.models.UserEntity;
import com.gfa.tribesvibinandtribinotocyon.models.roles.RoleAdmin;
import com.gfa.tribesvibinandtribinotocyon.models.roles.RoleUser;
import com.gfa.tribesvibinandtribinotocyon.services.BuildingService;
import com.gfa.tribesvibinandtribinotocyon.services.KingdomService;
import com.gfa.tribesvibinandtribinotocyon.services.StockpileService;
import com.gfa.tribesvibinandtribinotocyon.services.TroopService;
import com.gfa.tribesvibinandtribinotocyon.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TribesControllerTest {

    @Mock
    private KingdomService kingdomService;

    @Mock
    private BuildingService buildingService;

    @Mock
    private UserService userService;

    @Mock
    private StockpileService stockpileService;

    @Mock
    private TroopService troopService;

    @InjectMocks
    private TribesController tribesController;

    @Test
    void findKingdomBuildingsList_ValidId_ReturnsBuildingList() throws TribesBadRequestException, TribesUnauthorizedException {
        long kingdomId = 1L;
        UserDetails userDetails = new User("user", "password", Collections.emptyList());
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user");
        userEntity.setPassword("password");
        userEntity.setRole(new RoleUser());
        when(userService.getUserByUsername("user")).thenReturn(Optional.of(userEntity));
        when(kingdomService.findKingdomById(kingdomId)).thenReturn(Optional.of(new Kingdom()));
        when(buildingService.getBuildingList(any())).thenReturn(List.of(new BuildingDTO()));

        ResponseEntity<?> response = tribesController.findKingdomBuildingsList(kingdomId, userDetails);

        assertEquals(200, response.getStatusCodeValue());
        verify(buildingService, times(1)).getBuildingList(any());
    }

    @Test
    void findKingdomBuildingsList_InvalidId_ThrowsTribesBadRequestException() {
        long invalidId = -1L;
        UserDetails userDetails = new User("user", "password", Collections.emptyList());
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user");
        userEntity.setPassword("password");
        userEntity.setRole(new RoleUser());
        when(userService.getUserByUsername("user")).thenReturn(Optional.of(userEntity));

        assertThrows(TribesBadRequestException.class, () ->
                tribesController.findKingdomBuildingsList(invalidId, userDetails));
    }

    @Test
    void adminKingdomAccess_AdminUser_ReturnsAdminAccessKingdomDTO() throws TribesUnauthorizedException, TribesBadRequestException {
        long kingdomId = 1L;
        UserDetails userDetails = new User("admin", "password", Collections.emptyList());
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("admin");
        userEntity.setPassword("password");
        userEntity.setRole(new RoleAdmin());
        when(userService.getUserByUsername("admin")).thenReturn(Optional.of(userEntity));
        when(kingdomService.findKingdomById(kingdomId)).thenReturn(Optional.of(new Kingdom()));
        when(buildingService.getBuildingList(any())).thenReturn(List.of(new BuildingDTO()));
        when(stockpileService.getStockpileOfKingdom(any())).thenReturn(new StockpileDTO());
        when(troopService.findTroopsOfKingdom(any())).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = tribesController.adminKingdomAccess(kingdomId, userDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(AdminAccessKingdomDTO.class, response.getBody().getClass());
        verify(buildingService, times(1)).getBuildingList(any());

    }

    @Test
    void adminKingdomAccess_NonAdminUser_ThrowsTribesUnauthorizedException() {
        long kingdomId = 1L;
        UserDetails userDetails = new User("user", "password", Collections.emptyList());

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user");
        userEntity.setPassword("password");
        userEntity.setRole(new RoleUser());

        when(userService.getUserByUsername("user")).thenReturn(Optional.of(userEntity));

        assertThrows(TribesUnauthorizedException.class, () ->
                tribesController.adminKingdomAccess(kingdomId, userDetails));

        verify(userService, times(1)).getUserByUsername("user");
    }

}
