package com.github.atdi.office.server.exceptions;

import com.github.atdi.office.model.ErrorType;
import com.github.atdi.office.model.ResponseErrorBuilder;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by aurelavramescu on 15/07/15.
 */
public class OfficeNotFoundException extends WebApplicationException {

    public OfficeNotFoundException(String message) {
        super(Response.status(Response.Status.NOT_FOUND).
                entity(new ResponseErrorBuilder()
                        .withException(OfficeNotFoundException.class.getSimpleName())
                        .withMessage(message)
                        .withErrorType(ErrorType.NOT_FOUND_ERROR)
                        .build()).type(MediaType.APPLICATION_JSON_TYPE)
                .build());
    }
}
