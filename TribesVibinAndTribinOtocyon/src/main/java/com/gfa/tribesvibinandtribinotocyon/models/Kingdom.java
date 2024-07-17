package com.gfa.tribesvibinandtribinotocyon.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Kingdom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer kingdomLevel;
    private Integer peasants;
    private Integer xCoordinates;
    private Integer yCoordinates;

    @OneToOne
    private UserEntity user;

    @OneToOne
    private Stockpile stockpile;

    @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL)
    private List<Building> buildings;

    @OneToMany(mappedBy = "kingdom", cascade = CascadeType.ALL)
    private List<Troop> troops;
}
