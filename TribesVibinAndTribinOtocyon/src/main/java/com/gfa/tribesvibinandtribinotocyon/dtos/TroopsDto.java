package com.gfa.tribesvibinandtribinotocyon.dtos;

import com.gfa.tribesvibinandtribinotocyon.models.Troop;
import lombok.Data;

import java.util.List;

@Data
public class TroopsDto {
    private List<Troop> troops;
}
