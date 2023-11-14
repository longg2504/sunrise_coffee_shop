package com.cg.service.zone;

import com.cg.domain.dto.zone.ZoneDTO;
import com.cg.domain.entity.Zone;
import com.cg.repository.zone.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ZoneServiceImpl implements IZoneService{

    @Autowired
    private ZoneRepository zoneRepository;
    @Override
    public List<Zone> findAll() {
        return zoneRepository.findAll();
    }

    @Override
    public Optional<Zone> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Zone save(Zone zone) {
        return null;
    }

    @Override
    public void delete(Zone zone) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<ZoneDTO> findAllZoneDTO() {
        return zoneRepository.findAllZoneDTO();
    }

    @Override
    public Optional<Zone> findByIdAndDeletedFalse(Long id) {
        return zoneRepository.findById(id);
    }

}
