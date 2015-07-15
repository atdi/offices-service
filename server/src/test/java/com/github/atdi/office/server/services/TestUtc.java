package com.github.atdi.office.server.services;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Created by aurelavramescu on 14/07/15.
 */
public class TestUtc {

    @Test
    public void testUtc() {
        LocalTime time = LocalTime.of(8, 0);
        System.out.println(time);
        ZoneId berlin = ZoneId.of("Europe/Berlin");

        ZonedDateTime dateTime = ZonedDateTime.of(LocalDate.now(), time, berlin);
        System.out.println(dateTime);
        System.out.println(dateTime.withZoneSameInstant(ZoneId.of("UTC")));
    }
}
