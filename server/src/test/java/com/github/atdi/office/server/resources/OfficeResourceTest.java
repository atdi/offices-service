package com.github.atdi.office.server.resources;

import com.github.atdi.office.model.Office;
import com.github.atdi.office.model.OfficeBuilder;
import com.github.atdi.office.server.Bootstrap;
import com.github.atdi.office.server.config.DefaultConfigTest;
import com.github.atdi.office.server.exceptions.DuplicateOfficeException;
import com.github.atdi.office.server.exceptions.OfficeNotFoundException;
import com.github.atdi.office.server.services.OfficeService;
import com.github.atdi.office.server.services.repositories.OfficeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by aurelavramescu on 14/07/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Bootstrap.class, DefaultConfigTest.class})
public class OfficeResourceTest {

    @Autowired
    OfficeResource officeResource;

    @Autowired
    OfficeRepository officeRepository;

    @Autowired
    OfficeService officeService;

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
                .build();
        Response response = officeResource.addOffice(office);
        Office result = (Office)response.getEntity();
        assertNotNull(result.getId());
        assertEquals("Europe/Berlin", result.getTimeZone());
        assertEquals(100d, result.getLatitude(), 0d);
        assertEquals(100d, result.getLongitude(), 0d);
    }

    @Test(expected = TransactionSystemException.class)
    public void testAddOfficeValidationFailed() throws Exception {
        Office office = new OfficeBuilder()
                .withCountry("Germany")
                .withCity("Berlin")
                .withName("Berlin")
                .build();
        Response response = officeResource.addOffice(office);
    }

    @Test(expected = DuplicateOfficeException.class)
    public void testAddOfficeConflict() throws Exception {
        Office office = new OfficeBuilder()
                .withCountry("Germany")
                .withCity("Berlin")
                .withName("Berlin")
                .withOpenFrom(LocalTime.of(8, 0))
                .withOpenUntil(LocalTime.of(18, 0))
                .build();
        officeService.addOffice(office);
        officeResource.addOffice(office);
    }

    @Test
    public void testUpdateOffice() throws Exception {
        Office office = new OfficeBuilder()
                .withCountry("Germany")
                .withCity("Berlin")
                .withName("Berlin")
                .withOpenFrom(LocalTime.of(8, 0))
                .withOpenUntil(LocalTime.of(18, 0))
                .build();
        Office result = officeService.addOffice(office);
        Office officeUpdated = new OfficeBuilder().copy(result).withName("Paris")
                .withCountry("France")
                .withCity("Paris")
                .build();
        Response response = officeResource.updateOffice(result.getId(), officeUpdated);
        Office result1 = (Office)response.getEntity();
        assertEquals(result.getId(), result1.getId());
        assertEquals("Europe/Paris", result1.getTimeZone());
        assertEquals(200d, result1.getLatitude(), 0d);
        assertEquals(200d, result1.getLongitude(), 0d);
    }

    @Test(expected = OfficeNotFoundException.class)
    public void testUpdateOfficeNotFound() throws Exception {
        String id = UUID.randomUUID().toString();
        Office office = new OfficeBuilder()
                .withId(id)
                .withCountry("Germany")
                .withCity("Berlin")
                .withName("Berlin")
                .withOpenFrom(LocalTime.of(8, 0))
                .withOpenUntil(LocalTime.of(18, 0))
                .build();
        officeResource.updateOffice(office.getId(), office);
    }

    @Test(expected = DuplicateOfficeException.class)
    public void testUpdateOfficeWithConflict() throws Exception {
        Office office = new OfficeBuilder()
                .withCountry("Germany")
                .withCity("Berlin")
                .withName("Berlin")
                .withOpenFrom(LocalTime.of(8, 0))
                .withOpenUntil(LocalTime.of(18, 0))
                .build();
        Office result = officeService.addOffice(office);
        officeService.addOffice(new OfficeBuilder()
                .withCountry("France")
                .withCity("Paris")
                .withName("Paris")
                .withOpenFrom(LocalTime.of(8, 0))
                .withOpenUntil(LocalTime.of(18, 0))
                .build());
        Office officeUpdated = new OfficeBuilder().copy(result).withName("Paris")
                .withCountry("France")
                .withCity("Paris")
                .build();
        Response response = officeResource.updateOffice(result.getId(), officeUpdated);
    }

    @Test
    public void testGetOffice() throws Exception {
        Office office = new OfficeBuilder()
                .withCountry("Germany")
                .withCity("Berlin")
                .withName("Berlin")
                .withOpenFrom(LocalTime.of(8, 0))
                .withOpenUntil(LocalTime.of(18, 0))
                .withTimeZone("Europe/Berlin")
                .withLatitude(100d)
                .withLongitude(100d)
                .build();
        Office res = officeService.addOffice(office);
        Response response = officeResource.getOffice(res.getId());
        Office result = (Office)response.getEntity();
        assertEquals("Europe/Berlin", result.getTimeZone());
        assertEquals(100d, result.getLatitude(), 0d);
        assertEquals(100d, result.getLongitude(), 0d);
    }

    @Test
    public void testGetOffices() throws Exception {
        officeService.addOffice(new OfficeBuilder()
                .withCountry("Germany")
                .withCity("Berlin")
                .withName("Berlin")
                .withOpenFrom(LocalTime.of(8, 0))
                .withOpenUntil(LocalTime.of(18, 0))
                .withTimeZone("Europe/Berlin")
                .withLatitude(100d)
                .withLongitude(100d)
                .build());
        officeService.addOffice(new OfficeBuilder()
                .withCountry("France")
                .withCity("Paris")
                .withName("Paris")
                .withOpenFrom(LocalTime.of(8, 0))
                .withOpenUntil(LocalTime.of(18, 0))
                .withTimeZone("Europe/Paris")
                .withLatitude(100d)
                .withLongitude(100d)
                .build());
        Response response = officeResource.getOffices();
        Iterable<Office> officeList = (Iterable<Office>)response.getEntity();
        Iterator<Office> iterator = officeList.iterator();
        int count = 0;

        while(iterator.hasNext()) {
            Office o = iterator.next();
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testGetOpenOffices() throws Exception {
        officeService.addOffice(new OfficeBuilder()
                .withCountry("Germany")
                .withCity("Berlin")
                .withName("Berlin")
                .withOpenFrom(LocalTime.of(8, 0))
                .withOpenUntil(LocalTime.of(17, 0))
                .withTimeZone("Europe/Berlin")
                .withLatitude(100d)
                .withLongitude(100d)
                .build());
        officeService.addOffice(new OfficeBuilder()
                .withCountry("France")
                .withCity("Paris")
                .withName("Paris")
                .withOpenFrom(LocalTime.of(8, 0))
                .withOpenUntil(LocalTime.of(17, 0))
                .withTimeZone("Europe/Paris")
                .withLatitude(200d)
                .withLongitude(200d)
                .build());
        officeService.addOffice(new OfficeBuilder()
                .withCountry("USA")
                .withCity("Los Angeles")
                .withName("Los Angeles")
                .withOpenFrom(LocalTime.of(8, 0))
                .withOpenUntil(LocalTime.of(23, 0))
                .withTimeZone("America/Los_Angeles")
                .withLatitude(300d)
                .withLongitude(300d)
                .build());
        officeService.addOffice(new OfficeBuilder()
                .withCountry("USA")
                .withCity("San Francisco")
                .withName("San Francisco")
                .withOpenFrom(LocalTime.of(8, 0))
                .withOpenUntil(LocalTime.of(23, 0))
                .withTimeZone("America/Los_Angeles")
                .withLatitude(300d)
                .withLongitude(300d)
                .build());
        Response response = officeResource.getOpenOffices();
        List<Office> officeList = (List<Office>) response.getEntity();
        assertEquals(2, officeList.size());
    }
}