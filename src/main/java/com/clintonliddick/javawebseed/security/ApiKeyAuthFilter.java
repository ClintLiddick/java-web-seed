package com.clintonliddick.javawebseed.security;

import com.google.common.base.Strings;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Jersey {@link javax.ws.rs.container.ContainerRequestFilter} that authenticates and authorizes every request and
 * rejects on failure.
 *
 * @deprecated Custom filter is not necessary unless custom responses are desired. Use {@link
 * org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter} at the Servlet level to generate {@link
 * org.apache.shiro.authc.UsernamePasswordToken}s for more robust and standard behavior
 */
@Deprecated
public class ApiKeyAuthFilter implements ContainerRequestFilter {
    private static final transient Logger log = LoggerFactory.getLogger(ApiKeyAuthFilter.class);

    @Override
    public void filter(final ContainerRequestContext requestContext) throws IOException {
        log.debug("Entered request filter");

        String authHeader = requestContext.getHeaderString("Authorization");
        log.debug("Authorization header: \"{}\"", authHeader);

        if (Strings.isNullOrEmpty(authHeader)) {
            log.info("Request rejected 401 no Authorization header");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Authorization required").build());
        } else {
            String apiKey = getApiKeyFromAuthHeader(authHeader);
            log.debug("Subject used apiKey: {}", apiKey); // NOTE: don't log api keys in production

            AuthenticationToken token = new ApiKeyToken(apiKey);
            Subject user = SecurityUtils.getSubject();
            try {
                user.login(token);
                // login successful then nothing left to do
            } catch (UnauthorizedException uae) {
                log.info("Request rejected 403 not authorized");
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            } catch (AuthenticationException ae) {
                log.error("Unable to authenticate subject", ae);
                requestContext.abortWith(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Unable to authenticate subject").build());
            }
        }

    }

    /**
     * Converts Base64 encoded username:password HTTP Basic Authorization header into plaintext API Key.
     * <p/>
     * NOTE very fragile
     *
     * @param authHeader
     * @return
     */
    String getApiKeyFromAuthHeader(final String authHeader) {
        String base64AuthString = authHeader.replace("Basic ", "");
        String authString = Base64.decodeToString(base64AuthString);
        return authString.split(":")[0];
    }
}
