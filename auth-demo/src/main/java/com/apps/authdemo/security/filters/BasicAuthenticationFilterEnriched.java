package com.apps.authdemo.security.filters;

import java.io.IOException;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.apps.authdemo.socketio.model.BasicAuthMessage;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

public class BasicAuthenticationFilterEnriched extends OncePerRequestFilter {


    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private AuthenticationEntryPoint authenticationEntryPoint;
    private AuthenticationManager authenticationManager;
    private RememberMeServices rememberMeServices = new NullRememberMeServices();
    private boolean ignoreFailure = false;
    private String credentialsCharset = "UTF-8";
    private SocketIOServer server;

    public BasicAuthenticationFilterEnriched(AuthenticationManager authenticationManager, SocketIOServer server) {
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        this.authenticationManager = authenticationManager;
        this.ignoreFailure = true;
        this.server = server;
        UsernamePasswordAuthenticationFilter f;
    }

    public BasicAuthenticationFilterEnriched(AuthenticationManager authenticationManager,
                                     AuthenticationEntryPoint authenticationEntryPoint) {
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        Assert.notNull(authenticationEntryPoint,
                "authenticationEntryPoint cannot be null");
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    // ~ Methods
    // ========================================================================================================

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.authenticationManager,
                "An AuthenticationManager is required");

        if (!isIgnoreFailure()) {
            Assert.notNull(this.authenticationEntryPoint,
                    "An AuthenticationEntryPoint is required");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final boolean debug = this.logger.isDebugEnabled();

        String header = request.getHeader("Authorization");

        if (header == null || !header.toLowerCase().startsWith("basic ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String[] tokens = extractAndDecodeHeader(header, request);
            assert tokens.length == 2;

            String username = tokens[0];

            if (debug) {
                this.logger
                        .debug("Basic Authentication Authorization header found for user '"
                                + username + "'");
            }

            if (authenticationIsRequired(username)) {
                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                        username, tokens[1]);
                authRequest.setDetails(
                        this.authenticationDetailsSource.buildDetails(request));
                Authentication authResult = this.authenticationManager
                        .authenticate(authRequest);

                if (debug) {
                    this.logger.debug("Authentication success: " + authResult);
                }

                SecurityContextHolder.getContext().setAuthentication(authResult);

                this.rememberMeServices.loginSuccess(request, response, authResult);
                notifySocketIo(header, tokens[0] +" " + tokens[1], authResult);
                onSuccessfulAuthentication(request, response, authResult);
            }

        }
        catch (AuthenticationException failed) {
            SecurityContextHolder.clearContext();

            if (debug) {
                this.logger.debug("Authentication request for failed: " + failed);
            }

            this.rememberMeServices.loginFail(request, response);

            onUnsuccessfulAuthentication(request, response, failed);

            if (this.ignoreFailure) {
                chain.doFilter(request, response);
            }
            else {
                this.authenticationEntryPoint.commence(request, response, failed);
            }

            return;
        }

        chain.doFilter(request, response);
    }

    private void notifySocketIo (String header, String credentials, Authentication authResult) {
        String message = "Header: <br>&emsp;" + " credentials " + credentials;
        server.getBroadcastOperations().sendEvent("basicAuthFilter", new BasicAuthMessage(message));
        User user = (User)authResult.getPrincipal();
        StringBuilder sb = new StringBuilder("<br>&emsp;User{");
            sb.append("password='").append(user.getPassword()).append('\'');
            sb.append(", username='").append(user.getUsername()).append('\'');
            sb.append(", authorities=").append(user.getAuthorities());
            sb.append(", accountNonExpired=").append(user.isAccountNonExpired());
            sb.append(", accountNonLocked=").append(user.isAccountNonLocked());
            sb.append(", credentialsNonExpired=").append(user.isCredentialsNonExpired());
            sb.append(", enabled=").append(user.isEnabled());
            sb.append("}");
        message = "Associating with user: " + sb.toString();
        server.getBroadcastOperations().sendEvent("basicAuthFilter", new BasicAuthMessage(message));
    }

    /**
     * Decodes the header into a username and password.
     *
     * @throws BadCredentialsException if the Basic header is not present or is not valid
     * Base64
     */
    private String[] extractAndDecodeHeader(String header, HttpServletRequest request)
            throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        }
        catch (IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }

        String token = new String(decoded, getCredentialsCharset(request));

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[] { token.substring(0, delim), token.substring(delim + 1) };
    }

    private boolean authenticationIsRequired(String username) {
        // Only reauthenticate if username doesn't match SecurityContextHolder and user
        // isn't authenticated
        // (see SEC-53)
        Authentication existingAuth = SecurityContextHolder.getContext()
                .getAuthentication();

        if (existingAuth == null || !existingAuth.isAuthenticated()) {
            return true;
        }

        // Limit username comparison to providers which use usernames (ie
        // UsernamePasswordAuthenticationToken)
        // (see SEC-348)

        if (existingAuth instanceof UsernamePasswordAuthenticationToken
                && !existingAuth.getName().equals(username)) {
            return true;
        }

        // Handle unusual condition where an AnonymousAuthenticationToken is already
        // present
        // This shouldn't happen very often, as BasicProcessingFitler is meant to be
        // earlier in the filter
        // chain than AnonymousAuthenticationFilter. Nevertheless, presence of both an
        // AnonymousAuthenticationToken
        // together with a BASIC authentication request header should indicate
        // reauthentication using the
        // BASIC protocol is desirable. This behaviour is also consistent with that
        // provided by form and digest,
        // both of which force re-authentication if the respective header is detected (and
        // in doing so replace
        // any existing AnonymousAuthenticationToken). See SEC-610.
        if (existingAuth instanceof AnonymousAuthenticationToken) {
            return true;
        }

        return false;
    }

    protected void onSuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, Authentication authResult) throws IOException {
    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request,
                                                HttpServletResponse response, AuthenticationException failed)
            throws IOException {
    }

    protected AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return this.authenticationEntryPoint;
    }

    protected AuthenticationManager getAuthenticationManager() {
        return this.authenticationManager;
    }

    protected boolean isIgnoreFailure() {
        return this.ignoreFailure;
    }

    public void setAuthenticationDetailsSource(
            AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        Assert.notNull(authenticationDetailsSource,
                "AuthenticationDetailsSource required");
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

    public void setRememberMeServices(RememberMeServices rememberMeServices) {
        Assert.notNull(rememberMeServices, "rememberMeServices cannot be null");
        this.rememberMeServices = rememberMeServices;
    }

    public void setCredentialsCharset(String credentialsCharset) {
        Assert.hasText(credentialsCharset, "credentialsCharset cannot be null or empty");
        this.credentialsCharset = credentialsCharset;
    }

    protected String getCredentialsCharset(HttpServletRequest httpRequest) {
        return this.credentialsCharset;
    }
}
