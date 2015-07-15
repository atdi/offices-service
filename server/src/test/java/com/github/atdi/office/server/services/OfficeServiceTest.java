package com.github.atdi.office.server.services;

import com.github.atdi.office.model.Office;
import com.github.atdi.office.model.OfficeBuilder;
import com.github.atdi.office.server.Bootstrap;
import com.github.atdi.office.server.services.repositories.OfficeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalTime;

import static org.junit.Assert.*;

/**
 * Created by aurelavramescu on 14/07/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Bootstrap.class})
public class OfficeServiceTest {

    @Autowired
    private OfficeService officeService;

    @Autowired
    OfficeRepository officeRepository;


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        officeRepository.deleteAll();
    }

    @Test
    public void testAddOffice() throws Exception {
        Office office = new OfficeBuilder()
                .withCountry("Germany")
                .withCity("Berlin")
                .withName("Berlin")
                .withOpenFrom(LocalTime.of(8, 0))
                .withOpenUntil(LocalTime.of(18, 0))
                .withTimeZone("Europe/Berlin")
                .build();
        Office result = officeService.addOffice(office);
        assertNotNull(result.getId());
        assertEquals(LocalTime.of(8, 0), result.getOpenFrom());
    }

    @Test
    public void testUpdateOffice() throws Exception {
        Office office = new OfficeBuilder()
                .withCountry("Germany")
                .withCity("Berlin")
                .withName("Berlin")
                .withOpenFrom(LocalTime.of(8, 0))
                .withOpenUntil(LocalTime.of(18, 0))
                .withTimeZone("Europe/Berlin")
                .build();
        Office result = officeService.addOffice(office);
        office = new OfficeBuilder().copy(result).withName("Berlin2").build();
        result = officeService.updateOffice(office);
        assertEquals("Berlin2", result.getName());
    }

}