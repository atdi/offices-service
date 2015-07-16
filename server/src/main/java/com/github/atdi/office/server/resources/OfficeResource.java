package com.github.atdi.office.server.resources;

import com.github.atdi.office.model.Office;
import com.github.atdi.office.model.OfficeBuilder;
import com.github.atdi.office.server.exceptions.DuplicateOfficeException;
import com.github.atdi.office.server.exceptions.OfficeNotFoundException;
import com.github.atdi.office.server.services.GoogleMapsService;
import com.github.atdi.office.server.services.OfficeService;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aurelavramescu on 14/07/15.
 */
@Component
@Path("office")
public class OfficeResource {

    private final OfficeService officeService;

    private final GoogleMapsService googleMapsService;

    @Autowired
    public OfficeResource(OfficeService officeService, GoogleMapsService googleMapsService) {
        this.officeService = officeService;
        this.googleMapsService = googleMapsService;
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOffice(@Valid Office office) {
        try {
            Office result = officeService.addOffice(updateGeoData(office));
            return Response.created(URI.create("api/office/" + result.getId()))
                    .entity(result).build();
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateOfficeException(e.getMessage());
        }
    }


    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateOffice(
            @PathParam("id") String id,
            @Valid Office office) {
        Office result = officeService.findOffice(id);

        if (result == null) {
            throw new OfficeNotFoundException(String.format("Entity with id %s not found!", id));
        }

        try {
            result = officeService.updateOffice(updateGeoData(office));
            return Response.ok(result).build();
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateOfficeException(e.getMessage());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getOffice(
            @PathParam("id") String id) {
        Office result = officeService.findOffice(id);
        if (result == null) {
            throw new OfficeNotFoundException(String.format("Entity with id %s not found!", id));
        }
        return Response.ok(result).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getOffices() {
        return Response.ok(officeService.findAll()).build();
    }

    @GET
    @Path("open")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getOpenOffices() {
        Iterable<Office> offices = officeService.findAll();
        List<Office> openOffices = new ArrayList<>();
        offices.forEach(office -> {
            ZonedDateTime dateTimeNow = ZonedDateTime.now(ZoneId.of(office.getTimeZone()));
            ZonedDateTime openFrom = ZonedDateTime.of(dateTimeNow.toLocalDate(),
                    office.getOpenFrom(),
                    ZoneId.of(office.getTimeZone()));
            ZonedDateTime openUntil = ZonedDateTime.of(dateTimeNow.toLocalDate(),
                    office.getOpenUntil(),
                    ZoneId.of(office.getTimeZone()));
            if (dateTimeNow.compareTo(openFrom) > 0
                    && dateTimeNow.compareTo(openUntil) < 0) {
                openOffices.add(office);
            }
        });
        return Response.ok(openOffices).build();
    }

    @GET
    @Path("route")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getShortestRoute() {
        return Response.ok(googleMapsService.getShortesRoute()).build();
    }

    private Office updateGeoData(@Valid Office office) {
        LatLng geoPosition = googleMapsService.getGeoPosition(office.getCountry(),
                office.getCity());
        String timeZone = googleMapsService.getTimeZone(geoPosition);
        return new OfficeBuilder().copy(office)
                .withLatitude(geoPosition.lat)
                .withLongitude(geoPosition.lng)
                .withTimeZone(timeZone).build();
    }
}
