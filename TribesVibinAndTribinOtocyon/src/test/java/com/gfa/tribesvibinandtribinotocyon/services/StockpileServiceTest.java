package com.gfa.tribesvibinandtribinotocyon.services;

import com.gfa.tribesvibinandtribinotocyon.dtos.StockpileDTO;
import com.gfa.tribesvibinandtribinotocyon.dtos.StockpileOverviewDto;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesNotFoundException;
import com.gfa.tribesvibinandtribinotocyon.models.Kingdom;
import com.gfa.tribesvibinandtribinotocyon.models.Stockpile;
import com.gfa.tribesvibinandtribinotocyon.repositories.StockpileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StockpileServiceTest {

    @InjectMocks
    StockpileService stockpileService;

    @Mock
    StockpileRepository stockpileRepositoryMock;

    @Test
    void getStockpileOverviewByEmail_IncorrectEmail_ThrowsTribesNotFound() {
        String userEmail = "incorrect@test.com";

        when(stockpileRepositoryMock.findStockpileByEmail(userEmail)).thenReturn(null);

        assertThrows(TribesNotFoundException.class, () -> stockpileService.getStockpileOverviewByEmail(userEmail));
    }

    @Test
    void getStockpileOverviewByEmail_CorrectEmail_ReturnsOverview() throws TribesNotFoundException {
        String userEmail = "correct@test.com";

        Stockpile stockpile = new Stockpile();
        stockpile.setFood(1);
        stockpile.setWood(1);
        stockpile.setStone(1);
        stockpile.setIron(1);
        when(stockpileRepositoryMock.findStockpileByEmail(userEmail)).thenReturn(stockpile);

        StockpileOverviewDto stockpileOverviewDto = stockpileService.getStockpileOverviewByEmail(userEmail);

        assertEquals(stockpile.getFood(), stockpileOverviewDto.getFood());
        assertEquals(stockpile.getWood(), stockpileOverviewDto.getWood());
        assertEquals(stockpile.getStone(), stockpileOverviewDto.getStone());
        assertEquals(stockpile.getIron(), stockpileOverviewDto.getIron());
    }

    @Test
    void consumeResources_EnoughResources_ReturnsTrue() throws TribesNotFoundException {
        Long stockpileId = 1L;
        Stockpile stockpile = new Stockpile();
        stockpile.setFood(100);
        stockpile.setWood(100);
        stockpile.setStone(100);
        stockpile.setIron(100);

        when(stockpileRepositoryMock.findById(stockpileId)).thenReturn(Optional.of(stockpile));

        boolean result = stockpileService.consumeResources(stockpileId, 50, 50, 50, 50);

        assertTrue(result);
        assertTrue(stockpileService.hasEnoughResources(stockpile, 50, 50, 50, 50));
        verify(stockpileRepositoryMock, times(1)).save(stockpile);
    }

    @Test
    void consumeResources_NotEnoughResources_ReturnsFalse() throws TribesNotFoundException {
        Long stockpileId = 2L;
        Stockpile stockpile = new Stockpile();
        stockpile.setFood(1);
        stockpile.setWood(1);
        stockpile.setStone(1);
        stockpile.setIron(1);

        when(stockpileRepositoryMock.findById(stockpileId)).thenReturn(Optional.of(stockpile));

        boolean result = stockpileService.consumeResources(stockpileId, 50, 50, 50, 50);

        assertFalse(result);
        assertFalse(stockpileService.hasEnoughResources(stockpile, 50, 50, 50, 50));
        verify(stockpileRepositoryMock, never()).save(stockpile);
    }

    @Test
    void gainResources_StockpileDoesntExists_ThrowsError() throws TribesNotFoundException {
        StockpileService stockpileServiceSpy = spy(new StockpileService(stockpileRepositoryMock));

        doThrow(new TribesNotFoundException()).when(stockpileServiceSpy).checkIfStockpileExists(1L);

        assertThrows(TribesNotFoundException.class, () -> stockpileServiceSpy.gainResources(1L, 10, 20, 30, 40));
        verify(stockpileServiceSpy, times(1)).checkIfStockpileExists(1L);
        verify(stockpileServiceSpy, never()).getCurrentStockpileCapacity(1L);
        verify(stockpileRepositoryMock, never()).save(any());
    }

    @Test
    void gainResources_ValidInput_IncreasesResourcesWithinCapacity() throws TribesNotFoundException {
        Stockpile stockpile = new Stockpile();
        stockpile.setFood(50);
        stockpile.setWood(60);
        stockpile.setStone(70);
        stockpile.setIron(80);
        stockpile.setStockpileLevel(1);
        stockpile.setId(1L);

        StockpileService stockpileServiceSpy = spy(new StockpileService(stockpileRepositoryMock));

        when(stockpileRepositoryMock.save(any())).thenReturn(stockpile);
        doReturn(stockpile).when(stockpileServiceSpy).checkIfStockpileExists(1L);
        doReturn(100).when(stockpileServiceSpy).getCurrentStockpileCapacity(1L);

        stockpileServiceSpy.gainResources(1L, 10, 20, 30, 40);

        verify(stockpileServiceSpy, times(1)).checkIfStockpileExists(1L);
        verify(stockpileServiceSpy, times(1)).getCurrentStockpileCapacity(1L);
        verify(stockpileRepositoryMock, times(1)).save(any());

        assertEquals(60, stockpile.getFood());
        assertEquals(80, stockpile.getWood());
        assertEquals(100, stockpile.getStone());
        assertNotEquals(120, stockpile.getIron());
        assertEquals(100, stockpile.getIron());
    }

    @Test
    void hasEnoughResources_NotEnough_ReturnsFalse() {
        Stockpile stockpile = new Stockpile();
        stockpile.setWood(5);
        stockpile.setFood(10);
        stockpile.setStone(10);
        stockpile.setIron(10);

        boolean result = stockpileService.hasEnoughResources(stockpile, 10, 10, 10, 10);

        assertFalse(result);
    }

    @Test
    void hasEnoughResources_Enough_ReturnsTrue() {
        Stockpile stockpile = new Stockpile();
        stockpile.setWood(10);
        stockpile.setFood(10);
        stockpile.setStone(10);
        stockpile.setIron(10);

        boolean result = stockpileService.hasEnoughResources(stockpile, 10, 5, 0, 0);

        assertTrue(result);
    }

    @Test
    void checkIfStockpileExists_CorrectId_ReturnsStockpile() throws TribesNotFoundException {
        Stockpile existingStockpile = new Stockpile();
        existingStockpile.setId(1L);

        when(stockpileRepositoryMock.findById(1L)).thenReturn(Optional.of(existingStockpile));

        Stockpile resultStockpile = stockpileService.checkIfStockpileExists(1L);

        verify(stockpileRepositoryMock).findById(1L);
        assertEquals(existingStockpile, resultStockpile);
    }

    @Test
    void checkIfStockpileExists_IncorrectId_ThrowsTribesNotFound() throws TribesNotFoundException {
        when(stockpileRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TribesNotFoundException.class, () -> stockpileService.checkIfStockpileExists(1L));

        verify(stockpileRepositoryMock).findById(1L);
    }

    @Test
    void getStockpileOfKingdom_KingdomExists_ReturnsStockpile() {
        Kingdom kingdom = new Kingdom();
        Stockpile stockpile = new Stockpile();
        stockpile.setFood(10);
        stockpile.setId(1L);

        when(stockpileRepositoryMock.findByKingdom(kingdom)).thenReturn(Optional.of(stockpile));

        StockpileDTO stockpileDTO = stockpileService.getStockpileOfKingdom(kingdom);

        assertEquals(stockpile.getFood(), stockpileDTO.getFood());
        assertEquals(stockpile.getWood(), stockpileDTO.getWood());
    }

    @Test
    void getStockpileOfKingdom_KingdomNoStockpile_ReturnsNull() {
        Kingdom kingdom = new Kingdom();
        kingdom.setId(1L);

        when(stockpileRepositoryMock.findByKingdom(kingdom)).thenReturn(Optional.empty());

        StockpileDTO stockpileDTO = stockpileService.getStockpileOfKingdom(kingdom);

        verify(stockpileRepositoryMock).findByKingdom(kingdom);
        assertNull(stockpileDTO);
    }
    
}