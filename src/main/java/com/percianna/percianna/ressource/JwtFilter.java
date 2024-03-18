package com.percianna.percianna.ressource;

import com.percianna.percianna.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
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
/*
@Component
public class JwtFilter extends OncePerRequestFilter {
/*
    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private UserServices userServices;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authorization = httpServletResponse.getHeader("Authorization");
        String token = null;
        String username = null;

 */

    /*to retrieve the information on the token we have to count after the bearer
    so the bearer length = 6 so we are going to begin count on 7

     */
    /*
        if (null != authorization && authorization.startsWith("bearer")) {
            token = authorization.substring(7);
            username = jwtUtility.getUsernameFromToken(token);
        }
        UserDetails userDetails;
        if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
            userDetails = userServices.loadUserByUsername(username);

            //validate token
            if (jwtUtility.validateToken(token, userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails,
                        null,userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }

    }
}

     */
