package com.gfa.tribesvibinandtribinotocyon.controllers;

import com.gfa.tribesvibinandtribinotocyon.dtos.StockpileOverviewDto;
import com.gfa.tribesvibinandtribinotocyon.exceptions.TribesNotFoundException;
import com.gfa.tribesvibinandtribinotocyon.services.StockpileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class StockpileController {

    private final StockpileService stockpileService;

    @GetMapping("/kingdom/resources")
    public ResponseEntity<?> displayResources()
            throws TribesNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        StockpileOverviewDto stockpileOverviewDto = stockpileService.getStockpileOverviewByEmail(authentication.getName());
        return ResponseEntity.ok(stockpileOverviewDto);
    }

}