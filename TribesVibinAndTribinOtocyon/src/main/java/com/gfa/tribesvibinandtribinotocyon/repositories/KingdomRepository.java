package com.gfa.tribesvibinandtribinotocyon.repositories;

import com.gfa.tribesvibinandtribinotocyon.models.Kingdom;
import io.micrometer.common.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KingdomRepository extends JpaRepository<Kingdom,Long> {
    Optional<Kingdom> findByName(String name);

    Optional<Kingdom> findById(@NonNull Long id);
}