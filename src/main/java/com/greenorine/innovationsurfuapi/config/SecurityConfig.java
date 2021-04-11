package com.greenorine.innovationsurfuapi.config;

import com.greenorine.innovationsurfuapi.security.jwt.JwtConfigurer;
import com.greenorine.innovationsurfuapi.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    private static final String PUBLIC_ENDPOINT = "/api/v1/public/**";
    private static final String USER_ENDPOINT = "/api/v1/user/**";
    private static final String EXPERT_ENDPOINT = "/api/v1/expert/**";
    private static final String MODERATOR_ENDPOINT = "/api/v1/moderator/**";
    private static final String ADMIN_ENDPOINT = "/api/v1/admin/**";

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(PUBLIC_ENDPOINT).permitAll()
                .antMatchers(USER_ENDPOINT).authenticated()
                .antMatchers(EXPERT_ENDPOINT).hasAnyAuthority("EXPERT", "SUPERUSER")
                .antMatchers(MODERATOR_ENDPOINT).hasAnyAuthority("MODERATOR", "SUPERUSER")
                .antMatchers(ADMIN_ENDPOINT).hasAuthority("SUPERUSER")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
