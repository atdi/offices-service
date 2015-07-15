package com.github.atdi.office.server.exceptions;

import com.github.atdi.office.model.ErrorType;
import com.github.atdi.office.model.ResponseErrorBuilder;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by aurelavramescu on 15/07/15.
 */
public class GoogleServiceException extends WebApplicationException {

    public GoogleServiceException(String message) {
        super(Response.status(Response.Status.BAD_GATEWAY).
                entity(new ResponseErrorBuilder()
                        .withException(GoogleServiceException.class.getSimpleName())
                        .withMessage(message)
                        .withErrorType(ErrorType.GOOGLE_SERVICE_ERROR)
                        .build()).type(MediaType.APPLICATION_JSON_TYPE)
                .build());
    }
}
