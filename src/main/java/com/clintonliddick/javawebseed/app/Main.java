package com.clintonliddick.javawebseed.app;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Server start-up class
 */
public final class Main {
    private static final transient Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        // Setup Jetty/Jersey server
        Server jettyServer = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/rest");

        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = new ServletHolder(new ServletContainer(new RestApplication()));
        context.addServlet(jerseyServlet, "/*");

        // Setup Shiro security
        // For standard .war Web Apps see https://shiro.apache.org/web.html for web.xml configuration
        Factory<SecurityManager> securityManagerFactory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = securityManagerFactory.getInstance();
        // TODO instead of singleton, make this part of the container configuration (web.xml) ?
        SecurityUtils.setSecurityManager(securityManager);

        // Include ShiroFilter so other standard included filters will work in shiro.ini (e.g. authcBasic)
        context.addEventListener(new EnvironmentLoaderListener());

        context.addFilter(ShiroFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }
}
