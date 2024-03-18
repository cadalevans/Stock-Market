package com.percianna.percianna.Security;

import com.percianna.percianna.Services.UserServices;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServices userServices;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
   // private JwtFilter jwtFilter;

    @Override
    protected  void configure(AuthenticationManagerBuilder auth) throws Exception{
        //auth.userDetailsService(userServices);
        auth.userDetailsService(userServices).passwordEncoder(passwordEncoder);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/authenticate","/adduser","/getuser","/sell","/addstock",
                        "/update","/login","/delete/{id}","/api/stock-data/fetch","/getallstock",
                        "/user-portfolios/{userId}","/addsicav","/assign","/assignSicav/{sicavId}",
                        "/getsicav","/assignSicavToUser","/definition/{word}","/users/{userId}/total-amount",
                        "/users/{userId}/total-benefit","/users/{userId}/total-value",
                        "/api/stock-data","/payments/{userId}","/api/stock-data","/assignUser/{userId}",
                        "/reset-password/request","/api/stock-data/name","/update-order/{userId}/{orderId}"
                        ,"/reset-password/confirm","/getUserId","/user-orders/{userId}","/updatestock/{symbol}",
                        "/user/{name}","/sendingEmail")
                .permitAll()
                .anyRequest()
                .authenticated();
        //http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
               /* .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                */

    }

}
