package com.github.atdi.office.server.services;

import com.github.atdi.office.model.CommandResponse;
import com.github.atdi.office.server.commands.GetBestRouteCommand;
import com.github.atdi.office.server.commands.GetLatitudeLongitudeCommand;
import com.github.atdi.office.server.commands.GetTimeZoneCommand;
import com.github.atdi.office.server.exceptions.GoogleServiceException;
import com.github.atdi.office.server.services.repositories.OfficeRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;


/**
 * Created by aurelavramescu on 15/07/15.
 */
public class GoogleMapsService {

    private final GeoApiContext geoApiContext;

    @Autowired
    private OfficeRepository officeRepository;

    public GoogleMapsService(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    /**
     * Get geo position.
     * @param country country
     * @param city city
     * @return geo position
     */
    public LatLng getGeoPosition(String country, String city) {
        GetLatitudeLongitudeCommand command = new GetLatitudeLongitudeCommand(geoApiContext, country, city);
        CommandResponse<LatLng> response = command.execute();
        if (response.getError() != null) {
            throw new GoogleServiceException(response.getError());
        }
        return response.getEntity();
    }

    /**
     * Get timezone.
     * @param geoPosition geo positiom
     * @return time zone string
     */
    public String getTimeZone(LatLng geoPosition) {
        GetTimeZoneCommand command = new GetTimeZoneCommand(geoApiContext, geoPosition);
        CommandResponse<String> response = command.execute();
        if (response.getError() != null) {
            throw new GoogleServiceException(response.getError());
        }
        return response.getEntity();
    }

    /**
     * Find the shortest route between offices.
     * @return route order
     */
    public Set<String> getShortesRoute() {
        GetBestRouteCommand command = new GetBestRouteCommand(geoApiContext, officeRepository.findAll());
        CommandResponse<Set<String>> response = command.execute();
        if (response.getError() != null) {
            throw new GoogleServiceException(response.getError());
        }
        return response.getEntity();
    }
}
