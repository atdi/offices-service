package com.github.atdi.office.server.commands;

import com.github.atdi.office.model.CommandResponse;
import com.github.atdi.office.model.CommandResponseBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;

/**
 * Created by aurelavramescu on 15/07/15.
 */
public class GetLatitudeLongitudeCommand extends HystrixCommand<CommandResponse<LatLng>> {

    private final GeoApiContext context;

    private final String country;

    private final String city;

    public GetLatitudeLongitudeCommand(GeoApiContext context, String country, String city) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GoogleMaps"))
                .andCommandKey(HystrixCommandKey.Factory.asKey(GetLatitudeLongitudeCommand.class.getSimpleName()))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("RemoteService"
                        + GetLatitudeLongitudeCommand.class.getSimpleName() + "Fallback"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(4000)));

        this.context = context;
        this.country = country;
        this.city = city;
    }

    @Override
    protected CommandResponse<LatLng> run() throws Exception {
        GeocodingResult[] results = GeocodingApi.geocode(context,
                country + " " + city).await();
        LatLng geoPosition = results[0].geometry.location;
        return new CommandResponseBuilder<LatLng>()
                .withEntity(geoPosition)
                .withStatus(200)
                .build();
    }

    protected CommandResponse<LatLng> getFallback() {
        return new CommandResponseBuilder<LatLng>()
                .withError("Geocode service is not available.")
                .withStatus(502).build();
    }
}
