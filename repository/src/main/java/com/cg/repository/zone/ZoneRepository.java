package com.cg.repository.zone;

import com.cg.domain.dto.zone.ZoneDTO;
import com.cg.domain.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    Optional<Zone> findById(Long id);

    @Query("SELECT NEW com.cg.domain.dto.zone.ZoneDTO (" +
            "z.id, " +
            "z.title) " +
            "FROM Zone AS z ")
    List<ZoneDTO> findAllZoneDTO();

}
