package com.cg.service.zone;

import com.cg.domain.dto.zone.ZoneCreateReqDTO;
import com.cg.domain.dto.zone.ZoneDTO;
import com.cg.domain.entity.Zone;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IZoneService extends IGeneralService<Zone, Long> {
    List<ZoneDTO> findAllZoneDTO();

    Optional<Zone> findByIdAndDeletedFalse(Long id);

}
