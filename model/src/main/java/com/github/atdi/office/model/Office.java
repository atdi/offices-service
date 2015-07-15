package com.github.atdi.office.model;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.karneim.pojobuilder.GeneratePojoBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

/**
 * Created by aurelavramescu on 14/07/15.
 */
@Getter
@GeneratePojoBuilder(withCopyMethod = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "offices")
public class Office {

    @Id
    String id;

    @NotNull
    @Column(unique = true)
    String name;

    @NotNull
    String country;

    @NotNull
    String city;

    @NotNull
    @Column(name = "open_from")
    LocalTime openFrom;

    @NotNull
    @Column(name = "open_until")
    LocalTime openUntil;

    @Column(name = "time_zone")
    String timeZone;

    double latitude;

    double longitude;

}
