package com.gangnam.portal.repository;

import com.gangnam.portal.domain.Commute;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CommuteRepository extends JpaRepository<Commute, Long> {

    @Modifying
    @Query("UPDATE Commute c SET c.endDate=:endDate WHERE c.id=:commuteId")
    void updateCommuteEnd(@Param("commuteId") Long commuteId, @Param("endDate") Date endDate);


}
