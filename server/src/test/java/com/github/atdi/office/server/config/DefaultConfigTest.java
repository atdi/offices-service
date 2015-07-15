package com.github.atdi.office.server.config;

import com.github.atdi.office.server.services.GoogleMapsService;
import static org.mockito.Mockito.*;

import com.google.maps.model.LatLng;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Created by aurelavramescu on 15/07/15.
 */
@Configuration("test-default-config")
public class DefaultConfigTest {

    @Primary
    @Bean
    public GoogleMapsService googleMapsService() {
        GoogleMapsService googleMapsService = mock(GoogleMapsService.class);
        LatLng geoPositionBerlin = new LatLng(100d, 100d);
        when(googleMapsService.getGeoPosition("Germany", "Berlin")).thenReturn(geoPositionBerlin);
        when(googleMapsService.getTimeZone(geoPositionBerlin)).thenReturn("Europe/Berlin");

        LatLng geoPositionParis = new LatLng(200d, 200d);
        when(googleMapsService.getGeoPosition("France", "Paris")).thenReturn(geoPositionParis);
        when(googleMapsService.getTimeZone(geoPositionParis)).thenReturn("Europe/Paris");
        return googleMapsService;
    }
}
