package com.gfa.tribesvibinandtribinotocyon.dtos;

import com.gfa.tribesvibinandtribinotocyon.models.Stockpile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StockpileDTO {

    Integer food;
    Integer wood;
    Integer stone;
    Integer iron;

    public StockpileDTO(Stockpile stockpile) {
        this.food = stockpile.getFood();
        this.wood = stockpile.getWood();
        this.stone = stockpile.getStone();
        this.iron = stockpile.getIron();
    }
}
