package com.github.atdi.office.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Created by aurelavramescu on 15/07/15.
 */
@Getter
@GeneratePojoBuilder(withCopyMethod = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ResponseError {

    String exception;

    String message;

    ErrorType errorType;

}
