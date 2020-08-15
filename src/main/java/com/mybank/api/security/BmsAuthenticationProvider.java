package com.mybank.api.security;


import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;

@Component
public class BmsAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;
    public BmsAuthenticationProvider(UserService userService){
        this.userService=userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName ();
        String password = authentication.getCredentials ().toString ();
        UserDetails user = userService.loadUserByUsername (username);
        return new UsernamePasswordAuthenticationToken (username,"",user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom (authentication));

    }
}
