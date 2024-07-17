package com.gfa.tribesvibinandtribinotocyon.dtos;

import com.gfa.tribesvibinandtribinotocyon.models.Troop;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TroopDTO {
    String name;
    Integer amount;

    public TroopDTO(Troop troop) {
        this.name = troop.getName();
        this.amount = troop.getAmount();
    }
}
