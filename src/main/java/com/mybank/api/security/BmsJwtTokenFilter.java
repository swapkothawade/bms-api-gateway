package com.mybank.api.security;

import com.mybank.api.exception.CustomException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BmsJwtTokenFilter extends OncePerRequestFilter {

    private BmsTokenProvider bmsTokenprovider;
    public BmsJwtTokenFilter(BmsTokenProvider bmsTokenprovider) {
        this.bmsTokenprovider = bmsTokenprovider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {

            HttpServletResponse response = (HttpServletResponse) httpServletResponse;
            String token = bmsTokenprovider.resolveToken (httpServletRequest);
            if (token != null) {
                if (!bmsTokenprovider.isTokenPresentInDB(token)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid JWT token");
                    throw new CustomException("Invalid JWT token", HttpStatus.UNAUTHORIZED);
                }
                try {
                    bmsTokenprovider.validateToken(token) ;
                } catch (JwtException | IllegalArgumentException e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid JWT token");
                    throw new CustomException("Invalid JWT token",HttpStatus.UNAUTHORIZED);
                }
                Authentication auth = token != null ? bmsTokenprovider.getAuthentication(token) : null;
                //setting auth in the context.
                SecurityContextHolder.getContext().setAuthentication(auth);
            }


        }catch(Exception ex) {
            //this is very important, since it guarantees the user is not authenticated at all
            SecurityContextHolder.clearContext ();
            ex.printStackTrace ();
           // httpServletResponse.sendError (ex.getHttpStatus ().value (), ex.getMessage ());
        }
        String token = httpServletRequest.getHeader("Authorization");
        System.out.println(String.format("**** Request URL %s , token %s",httpServletRequest.getRequestURI(),token));
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        //return;
    }

   }
