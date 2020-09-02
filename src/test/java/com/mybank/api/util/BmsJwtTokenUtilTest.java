package com.mybank.api.util;


import com.mybank.api.config.util.BmsJwttokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class BmsJwtTokenUtilTest {


    @Test(expected = ExpiredJwtException.class)
    public void testUserName(){
        String token ="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzd2FwbmlsLmtvdGhhd2FkZUBnbWFpbC5jb20iLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJST0xFX1JPTEVfQURNSU4ifSx7ImF1dGhvcml0eSI6IlJPTEVfUk9MRV9VU0VSIn1dLCJpYXQiOjE1OTY5MTI5MzIsImV4cCI6MTU5NjkxNjUzMn0.42z2sN2DLgoud91tStpjWFjxFzFICaImJ5SGYMDimiM";
        BmsJwttokenUtil util = new BmsJwttokenUtil("secret-key",360000);
        String userName = util.getUsername(token);
      assertTrue("swapnil.kothawade@gmail.com".equals(userName));
    }
}
