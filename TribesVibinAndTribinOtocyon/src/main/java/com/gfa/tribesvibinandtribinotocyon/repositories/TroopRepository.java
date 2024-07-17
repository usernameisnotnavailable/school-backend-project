package com.gfa.tribesvibinandtribinotocyon.repositories;

import com.gfa.tribesvibinandtribinotocyon.models.Kingdom;
import com.gfa.tribesvibinandtribinotocyon.models.Troop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TroopRepository extends JpaRepository<Troop, Long> {
    Optional<Troop> findByNameAndKingdom_Id(String name, Long id);

    List<Troop> findAllByKingdom(Kingdom kingdom);

    @Query("SELECT s FROM Troop s INNER JOIN s.kingdom k INNER JOIN k.user u WHERE u.email = :email")
    List<Troop> findTroopsByEmail(@Param("email") String email);

    List<Troop> findTroopsByKingdomId(Long kingdomId);
}