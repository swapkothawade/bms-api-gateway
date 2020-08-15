package com.mybank.api.security;


import com.mybank.api.dao.UserDao;
import com.mybank.api.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.getUser(email);
        if (user == null || user.getRole() == null || user.getRole().isEmpty()) {
            throw new CustomException("Invalid username or password.", HttpStatus.UNAUTHORIZED);
        }


       List<String> roles = user.getRole().stream().map(role->"ROLE_"+role).collect(Collectors.toList());
        String [] authorities = new String[roles.size()];
        roles.toArray(authorities);
        BmsUserDetails userDetails = new BmsUserDetails(user.getEmail(),user.getPassword(),user.getActive(),
        user.isLocked(), user.isExpired(),user.isEnabled(),authorities);
        return userDetails;
    }



}
