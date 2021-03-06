package com.clintonliddick.javawebseed.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Hello world! and other simple text endpoints
 */
@Path("/hello")
public class Hello {
    final Logger logger = LoggerFactory.getLogger(Hello.class);

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        logger.info("returning \"Hello world!\"");
        return "Hello world!";
    }

}
