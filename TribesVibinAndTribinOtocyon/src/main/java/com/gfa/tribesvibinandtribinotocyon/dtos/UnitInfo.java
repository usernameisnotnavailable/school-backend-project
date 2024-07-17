package com.gfa.tribesvibinandtribinotocyon.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitInfo {
    String name;
    Integer HP;
    Integer attack;
    Integer defense;
    Integer foodCost;
    Integer woodCost;
    Integer stoneCost;
    Integer ironCost;
    Integer speed;
    Integer carryCapacity;
    Integer trainTime;
    Integer barrackLvlNeeded;

    public UnitInfo(String[] line) {
        this.name = line[0];
        this.HP = Integer.parseInt(line[1]);
        this.attack = Integer.parseInt(line[2]);
        this.defense = Integer.parseInt(line[3]);
        this.foodCost = Integer.parseInt(line[4]);
        this.woodCost = Integer.parseInt(line[5]);
        this.stoneCost = Integer.parseInt(line[6]);
        this.ironCost = Integer.parseInt(line[7]);
        this.speed = Integer.parseInt(line[8]);
        this.carryCapacity = Integer.parseInt(line[9]);
        this.trainTime = Integer.parseInt(line[10]);
        this.barrackLvlNeeded = Integer.parseInt(line[11]);
    }
}
