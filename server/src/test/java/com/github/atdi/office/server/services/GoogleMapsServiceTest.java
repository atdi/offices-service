package com.github.atdi.office.server.services;

import com.github.atdi.office.server.Bootstrap;
import com.github.atdi.office.server.config.DefaultConfig;
import com.google.maps.GeoApiContext;
import com.google.maps.model.LatLng;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by aurelavramescu on 15/07/15.
 */
public class GoogleMapsServiceTest {

    private GoogleMapsService googleMapsService;

    @Before
    public void setUp() throws Exception {
        GeoApiContext context = new GeoApiContext()
                .setApiKey("AIzaSyB3uNpCJa3pZf42iZ32VsLBfWjprjuhbZ8")
                .setConnectTimeout(3, TimeUnit.SECONDS)
                .setReadTimeout(3, TimeUnit.SECONDS)
                .setWriteTimeout(3, TimeUnit.SECONDS);
        googleMapsService = new GoogleMapsService(context);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetTimeZone() throws Exception {
        LatLng geoPosition = googleMapsService.getGeoPosition("Germany", "Berlin");
        String timeZone = googleMapsService.getTimeZone(geoPosition);
        assertEquals("Europe/Berlin", timeZone);
    }
}