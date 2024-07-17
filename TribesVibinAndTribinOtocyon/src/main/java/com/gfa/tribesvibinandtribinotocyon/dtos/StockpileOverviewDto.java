package com.gfa.tribesvibinandtribinotocyon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockpileOverviewDto {
    private Integer food;
    private Integer wood;
    private Integer stone;
    private Integer iron;
}