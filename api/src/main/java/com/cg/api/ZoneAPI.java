package com.cg.api;

import com.cg.domain.dto.zone.ZoneDTO;
import com.cg.repository.zone.ZoneRepository;
import com.cg.service.zone.IZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
public class ZoneAPI {
    @Autowired
    private IZoneService zoneService;
    @GetMapping
    public ResponseEntity<?> getAllZone(){
        List<ZoneDTO> zoneDTOList = zoneService.findAllZoneDTO();
        if(zoneDTOList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(zoneDTOList, HttpStatus.OK);
    }

}
