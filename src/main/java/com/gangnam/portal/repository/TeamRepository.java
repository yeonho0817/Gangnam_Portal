package com.gangnam.portal.repository;

import com.gangnam.portal.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
