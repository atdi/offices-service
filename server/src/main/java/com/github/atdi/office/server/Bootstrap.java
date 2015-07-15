package com.github.atdi.office.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;

/**
 * Main class, required by spring boot.
 *
 * Created by aurelavramescu on 14/07/15.
 */
@EntityScan(basePackages = { "com.github.atdi.office.model" })
@SpringBootApplication
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

}
