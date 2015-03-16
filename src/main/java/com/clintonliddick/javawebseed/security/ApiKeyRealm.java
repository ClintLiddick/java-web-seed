package com.clintonliddick.javawebseed.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link org.apache.shiro.realm.Realm} for REST service Api Keys. Supports {@link
 * org.apache.shiro.authc.UsernamePasswordToken} only.
 */
public class ApiKeyRealm implements Realm {
    private static final transient Logger log = LoggerFactory.getLogger(ApiKeyRealm.class);

    @Override
    public String getName() {
        return "ApiKeyRealm";
    }

    @Override
    public boolean supports(final AuthenticationToken token) {
        boolean supports = token instanceof UsernamePasswordToken;
        log.info("supports token? {}", supports);
        return supports;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {
        log.info("Authenticating token");
        // do database check here: select * from api_keys where api_key = token.getPrincipal()
        String api_key = "myapikey";

        String principal = (String) token.getPrincipal();
        if (api_key.equals(principal)) { // if (apiKey exists in database) ...
            return new ApiKeyAuthInfo(principal);
        } else {
            throw new AuthenticationException();
        }
    }
}
