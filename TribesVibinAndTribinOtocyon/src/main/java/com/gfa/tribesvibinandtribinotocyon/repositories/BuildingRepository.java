package com.gfa.tribesvibinandtribinotocyon.repositories;

import com.gfa.tribesvibinandtribinotocyon.models.Building;
import com.gfa.tribesvibinandtribinotocyon.models.Kingdom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {

    public List<Building> findAllByKingdom(Kingdom kingdom);

    Optional<Building> getBuildingByKingdomIdAndName(Long kingdomId, String buildingName);
}