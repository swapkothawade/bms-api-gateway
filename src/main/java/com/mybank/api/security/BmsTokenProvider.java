package com.mybank.api.security;


import com.mybank.api.config.util.BmsJwttokenUtil;

import com.mybank.api.dao.UserSessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;


@Component
public class BmsTokenProvider {


  @Autowired
  private UserService userService;

  @Autowired
  private UserSessionDao userSessionDao;
//@Autowired
  private BmsJwttokenUtil bmsJwttokenUtil;

  @Autowired
  public BmsTokenProvider(BmsJwttokenUtil bmsJwttokenUtil) {

    this.bmsJwttokenUtil = bmsJwttokenUtil;
  }

  public String createToken(String username, List<String> roles) {
    return bmsJwttokenUtil.createToken (username,roles);
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userService.loadUserByUsername (bmsJwttokenUtil.getUsername(token));
    if(Objects.nonNull(userDetails))
      return new UsernamePasswordAuthenticationToken(userDetails.getUsername (),"",userDetails.getAuthorities());
    return null;
  }


  public String getUsername(String token) {
    return bmsJwttokenUtil.getUsername (token);
  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token) throws Exception {
    return bmsJwttokenUtil.validateToken (token);
  }

  public boolean isTokenPresentInDB(String token){
    return userSessionDao.isTokenPresentInDB(token);
  }

}
