package com.gfa.tribesvibinandtribinotocyon.repositories;

import com.gfa.tribesvibinandtribinotocyon.models.Kingdom;
import com.gfa.tribesvibinandtribinotocyon.models.Stockpile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockpileRepository extends JpaRepository<Stockpile, Long> {

    @Query("SELECT s FROM Stockpile s INNER JOIN s.kingdom k INNER JOIN k.user u WHERE u.email = :email")
    Stockpile findStockpileByEmail(@Param("email") String email);

    @Query("SELECT s.id FROM Stockpile s WHERE s.kingdom.id = :kingdomId")
    Long findStockpileIdByKingdomId(@Param("kingdomId") Long kingdomId);

    Optional<Stockpile> findByKingdom(Kingdom kingdom);
}