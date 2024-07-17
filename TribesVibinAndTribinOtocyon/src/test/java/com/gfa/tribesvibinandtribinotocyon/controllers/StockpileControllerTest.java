package com.gfa.tribesvibinandtribinotocyon.controllers;

import com.gfa.tribesvibinandtribinotocyon.dtos.StockpileOverviewDto;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesNotFoundException;
import com.gfa.tribesvibinandtribinotocyon.services.StockpileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StockpileControllerTest {

    @InjectMocks
    StockpileController stockpileController;

    @Mock
    StockpileService stockpileServiceMock;

    @Test
    public void displayResources_ExistingStockpile_ReturnsStockpileDTO() throws TribesNotFoundException {
        String userEmail = "test@test.com";

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(userEmail);

        StockpileOverviewDto stockpileOverviewDto = new StockpileOverviewDto();
        when(stockpileServiceMock.getStockpileOverviewByEmail(userEmail)).thenReturn(stockpileOverviewDto);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<?> responseEntity = stockpileController.displayResources();

        assertEquals(ResponseEntity.ok(stockpileOverviewDto), responseEntity);
    }

    @Test
    public void displayResources_NonExistingStockpile_ThrowsException() throws TribesNotFoundException {
        String userEmail = "test@test.com";

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(userEmail);

        when(stockpileServiceMock.getStockpileOverviewByEmail(userEmail)).thenThrow(new TribesNotFoundException("Stockpile not found for this user"));

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(TribesNotFoundException.class, () -> stockpileController.displayResources());
    }

}