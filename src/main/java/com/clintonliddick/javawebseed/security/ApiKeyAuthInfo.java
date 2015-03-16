package com.clintonliddick.javawebseed.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

/**
 * {@link org.apache.shiro.authc.AuthenticationInfo} representing an authentication of an API Key using Subject
 * <p/>
 * An API Key contains only a Principal (representing a Subject) and never any Credentials (passwords). API keys must
 * only be used in this form over TLS/SSL
 */
public class ApiKeyAuthInfo implements AuthenticationInfo {
    private String apiKey;

    public ApiKeyAuthInfo(final String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return new SimplePrincipalCollection(this.apiKey, "ApiKeyRealm");
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
