package com.gfa.tribesvibinandtribinotocyon.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Stockpile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer stockpileLevel;
    private Integer iron;
    private Integer wood;
    private Integer food;
    private Integer stone;

    @OneToOne
    private Kingdom kingdom;
}
