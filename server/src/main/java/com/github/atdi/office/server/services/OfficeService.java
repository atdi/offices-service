package com.github.atdi.office.server.services;

import com.github.atdi.office.model.Office;
import com.github.atdi.office.model.OfficeBuilder;
import com.github.atdi.office.server.services.repositories.OfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by aurelavramescu on 14/07/15.
 */
@Service
public class OfficeService {

    private final OfficeRepository officeRepository;

    @Autowired
    public OfficeService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    @Transactional
    public Office addOffice(Office office) {
        Office tempOffice = new OfficeBuilder()
                .copy(office)
                .withId(UUID.randomUUID().toString())
                .build();
        return officeRepository.save(tempOffice);
    }

    @Transactional
    public Office updateOffice(Office office) {
        return officeRepository.save(office);
    }


    @Transactional(readOnly = true)
    public Office findOffice(String id) {
        return officeRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Iterable<Office> findAll() {
        return officeRepository.findAll();
    }

    @Transactional
    public void deleteOffice(Office office) {
        officeRepository.delete(office);
    }

    @Transactional
    public void deleteOffice(String id) {
        officeRepository.delete(id);
    }



}
