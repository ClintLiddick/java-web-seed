package com.clintonliddick.javawebseed.rest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
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
    final Logger log = LoggerFactory.getLogger(Hello.class);

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
//        AuthenticationToken authToken = new AuthenticationToken() {
//            @Override
//            public Object getPrincipal() {
//                return null;
//            }
//
//            @Override
//            public Object getCredentials() {
//                return null;
//            }
//        };
        UsernamePasswordToken token = new UsernamePasswordToken("presidentskroob", "12345");
        Subject user = SecurityUtils.getSubject();
//        if (!user.isAuthenticated()) { // unneccessary for stateless/sessionless endpoint
        try {
            user.login(token);
        } catch (IncorrectCredentialsException ice) {
            //password didn't match, try again?
        } catch (LockedAccountException lae) {
            //account for that username is locked - can't login.  Show them a message?
        } catch (AuthenticationException ae) {
            //unexpected condition - error?
        }

        log.info("/hello called by user: {}", user.getPrincipal());

        if (user.hasRole("guest")) {
            log.info("returning \"Hello world!\"");
            user.logout();
            return "Hello world!";
        } else {
            throw new UnauthorizedException("User unauthorized to call /hello");
        }
    }

}
