package com.github.atdi.office.server.commands;

import com.github.atdi.office.model.CommandResponse;
import com.github.atdi.office.model.CommandResponseBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.TimeZoneApi;
import com.google.maps.model.LatLng;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;

import java.util.TimeZone;

/**
 * Created by aurelavramescu on 15/07/15.
 */
public class GetTimeZoneCommand extends HystrixCommand<CommandResponse<String>> {

    private final GeoApiContext context;

    private final LatLng geoPosition;

    public GetTimeZoneCommand(GeoApiContext context, LatLng geoPosition) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GoogleMaps"))
                .andCommandKey(HystrixCommandKey.Factory.asKey(GetTimeZoneCommand.class.getSimpleName()))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("RemoteService"
                        + GetTimeZoneCommand.class.getSimpleName() + "Fallback"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(4000)));

        this.context = context;
        this.geoPosition = geoPosition;
    }

    @Override
    protected CommandResponse<String> run() throws Exception {
        TimeZone tz = TimeZoneApi.getTimeZone(context, geoPosition).await();
        return new CommandResponseBuilder<String>()
                .withEntity(tz.getID())
                .withStatus(200)
                .build();
    }

    protected CommandResponse<String> getFallback() {
        return new CommandResponseBuilder<String>()
                .withError("Time zone service is not available.")
                .withStatus(502).build();
    }
}
