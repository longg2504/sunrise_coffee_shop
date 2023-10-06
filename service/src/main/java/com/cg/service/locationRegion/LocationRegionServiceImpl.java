package com.cg.service.locationRegion;

import com.cg.domain.entity.LocationRegion;
import com.cg.repository.locationRegion.LocationRegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class LocationRegionServiceImpl implements ILocationRegionService {
    @Autowired
    private LocationRegionRepository locationRegionRepository;
    @Override
    public List<LocationRegion> findAll() {
        return locationRegionRepository.findAll();
    }

    @Override
    public Optional<LocationRegion> findById(Long id) {
        return locationRegionRepository.findById(id);
    }

    @Override
    public LocationRegion save(LocationRegion locationRegion) {
        return locationRegionRepository.save(locationRegion);
    }

    @Override
    public void delete(LocationRegion locationRegion) {
         locationRegionRepository.delete(locationRegion);
    }

    @Override
    public void deleteById(Long id) {
        locationRegionRepository.deleteById(id);
    }
}
