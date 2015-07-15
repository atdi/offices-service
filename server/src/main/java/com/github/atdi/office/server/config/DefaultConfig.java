package com.github.atdi.office.server.config;

import com.github.atdi.office.server.services.GoogleMapsService;
import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Created by aurelavramescu on 15/07/15.
 */
@Configuration
public class DefaultConfig {

    @Value("${google.maps.api.key}")
    private String apiKey;

    @Bean
    public GoogleMapsService googleMapsService() {
        GeoApiContext context = new GeoApiContext()
                .setApiKey(apiKey)
                .setConnectTimeout(3, TimeUnit.SECONDS)
                .setReadTimeout(3, TimeUnit.SECONDS)
                .setWriteTimeout(3, TimeUnit.SECONDS);
        GoogleMapsService googleMapsService = new GoogleMapsService(context);
        return googleMapsService;
    }
}
