package com.github.atdi.office.server.commands;

import com.github.atdi.office.model.CommandResponse;
import com.github.atdi.office.model.CommandResponseBuilder;
import com.github.atdi.office.model.Office;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by aurelavramescu on 15/07/15.
 */
public class GetBestRouteCommand extends HystrixCommand<CommandResponse<Set<String>>> {

    private final GeoApiContext context;

    private final Iterable<Office> offices;

    public GetBestRouteCommand(GeoApiContext context, Iterable<Office> offices) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GoogleMaps"))
                .andCommandKey(HystrixCommandKey.Factory.asKey(GetBestRouteCommand.class.getSimpleName()))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("RemoteService"
                        + GetBestRouteCommand.class.getSimpleName() + "Fallback"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(4000)));

        this.context = context;
        this.offices = offices;
    }

    @Override
    protected CommandResponse<Set<String>> run() throws Exception {
        Set<String> routeOrder = new HashSet<>();
        List<String> locations = new ArrayList<>();
        offices.forEach(office -> locations.add(office.getCity() + ", " + office.getCountry()));
        String startPoint = locations.get(0);
        locations.remove(0);
        String[] locationsArray = locations.toArray(new String[locations.size()]);
        DirectionsApiRequest request = DirectionsApi.getDirections(context, startPoint, startPoint)
                .optimizeWaypoints(true);
        if (locationsArray.length == 1) {
            request.waypoints(locationsArray[0]);
        } else if (locationsArray.length > 1) {
            request.waypoints(locationsArray);
        }
        DirectionsRoute[] routes = request.await();

        for (DirectionsRoute route : routes) {
            for (DirectionsLeg leg : route.legs) {
                routeOrder.add(leg.startAddress);
            }
        }
        return new CommandResponseBuilder<Set<String>>()
                .withEntity(routeOrder)
                .withStatus(200)
                .build();
    }

    protected CommandResponse<Set<String>> getFallback() {
        return new CommandResponseBuilder<Set<String>>()
                .withError("Geocode distance service is not available or are not enough offices defined in the system.")
                .withStatus(502).build();
    }
}