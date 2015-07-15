package com.github.atdi.office.server.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.validation.ValidationError;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

/**
 * Created by aurelavramescu on 14/07/15.
 */
@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

    /**
     * Register jersey resources.
     */
    public JerseyConfig() {
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
        register(ValidationError.class);
        packages("com.github.atdi.office.server.resources");
    }
}