package com.gangnam.portal.repository;

import com.gangnam.portal.domain.Ranks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RanksRepository extends JpaRepository<Ranks, Long> {
    Optional<Ranks> findById(Long rankId);
}
