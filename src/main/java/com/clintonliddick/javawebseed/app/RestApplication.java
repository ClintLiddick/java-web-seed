package com.clintonliddick.javawebseed.app;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Defines my {@link javax.ws.rs.core.Application}
 */
public class RestApplication extends ResourceConfig {
    public RestApplication() {
        packages("com.clintonliddick.javawebseed.rest"); // package to scan for Jersey resources
    }
}
