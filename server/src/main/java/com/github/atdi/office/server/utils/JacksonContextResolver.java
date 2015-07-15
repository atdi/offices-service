package com.github.atdi.office.server.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import javax.ws.rs.ext.ContextResolver;

/**
 * Created by aurelavramescu on 15/07/15.
 */
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {
    private static final ObjectMapper om = init();

    @Override
    public ObjectMapper getContext(Class<?> objectType) {
        return om;
    }

    private static ObjectMapper init() {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JSR310Module());
        return om;
    }
}
