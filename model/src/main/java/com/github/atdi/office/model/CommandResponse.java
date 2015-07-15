package com.github.atdi.office.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Created by aurelavramescu on 14/07/15.
 */
@Getter
@GeneratePojoBuilder(withCopyMethod = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CommandResponse<T> {

    T entity;

    int status;
}
