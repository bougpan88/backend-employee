package com.boug.employee.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class JwtRequestFilter extends OncePerRequestFilter
{

    private static final Logger LOGGER = getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${auth.entry.point}")
    private String authEntryPoint;

    @Value("#{'${swager.exclude.from.security}'.split(',')}")
    private List<String> swaggerExcludeFromSecurityUris;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        boolean mustBeExcludedForSwagger = swaggerExcludeFromSecurityUris.stream()
                                                                         .anyMatch(s -> request.getRequestURI().startsWith(s));
        if ( !mustBeExcludedForSwagger  && !request.getRequestURI().equals(authEntryPoint)) {
            final String requestTokenHeader = request.getHeader("authorization");

            String username = null;
            String jwtToken = null;

            // JWT Token is in the form "Bearer token".
            //Remove Bearer word and get only the Token
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                //remove Bearer
                String bearer = requestTokenHeader.substring(7);
                ObjectMapper objectMapper = new ObjectMapper();
                jwtToken = objectMapper.readValue(bearer, Token.class).getToken();
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken);

                } catch (IllegalArgumentException e) {
                    LOGGER.info("Unable to get JWT Token");
                } catch (ExpiredJwtException e) {
                    LOGGER.info("JWT Token has expired");
                }
            } else {
                LOGGER.info("JWT Token does not begin with Bearer String");
            }
            // Once we get the token validate it.
            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);
                // if token is valid configure Spring Security to manually set
                // authentication
                if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the
                    // Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
