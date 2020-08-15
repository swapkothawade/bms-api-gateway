package com.mybank.api.config;

import com.mybank.api.security.BmsJwtTokenFilter;
import com.mybank.api.security.BmsTokenProvider;

import com.mybank.api.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class BmsWebConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BmsTokenProvider bmsTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.authenticationProvider (daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and ().csrf ().disable ();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        BmsJwtTokenFilter customFilter = new BmsJwtTokenFilter (this.bmsTokenProvider);
        http.addFilterBefore (customFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests ()
                .antMatchers("/**/public/**").permitAll ()
                .antMatchers ("/**/restricted/**").hasRole ("USER")
                .anyRequest ().authenticated ();
    }
        @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/**/public/**");

    }

 @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder (12);
    }

    @Bean
    public  DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider ();
       daoAuthenticationProvider.setPasswordEncoder (passwordEncoder ());
        daoAuthenticationProvider.setUserDetailsService (userDetailsService);
        return daoAuthenticationProvider;
    }


}
