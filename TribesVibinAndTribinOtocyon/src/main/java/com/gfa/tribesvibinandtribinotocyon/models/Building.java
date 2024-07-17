package com.gfa.tribesvibinandtribinotocyon.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer buildingLevel;
    private Integer workers;
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "kingdom_id")
    private Kingdom kingdom;
}