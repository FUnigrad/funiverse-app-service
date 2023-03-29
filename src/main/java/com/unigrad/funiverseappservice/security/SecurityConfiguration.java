package com.unigrad.funiverseappservice.security;

import com.unigrad.funiverseappservice.entity.socialnetwork.Role;
import com.unigrad.funiverseappservice.security.filter.JwtAuthenticationFilter;
import com.unigrad.funiverseappservice.security.handler.CustomAccessDeniedHandler;
import com.unigrad.funiverseappservice.security.handler.RestAuthenticationEntryPoint;
import com.unigrad.funiverseappservice.service.IUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(CustomAccessDeniedHandler customAccessDeniedHandler, RestAuthenticationEntryPoint restAuthenticationEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                ;

        http
                .authorizeHttpRequests()
                .requestMatchers("subject/**").hasAuthority(Role.WORKSPACE_ADMIN.toString())
                .requestMatchers("syllabus/**").hasAuthority(Role.WORKSPACE_ADMIN.toString())
                .requestMatchers("curriculum/**").hasAuthority(Role.WORKSPACE_ADMIN.toString())
                .requestMatchers("combo/**").hasAuthority(Role.WORKSPACE_ADMIN.toString())
                .requestMatchers("major/**").hasAuthority(Role.WORKSPACE_ADMIN.toString())
                .requestMatchers("term/**").hasAuthority(Role.WORKSPACE_ADMIN.toString())
                .requestMatchers("specialization/**").hasAuthority(Role.WORKSPACE_ADMIN.toString())
                .requestMatchers("combo/**").hasAuthority(Role.WORKSPACE_ADMIN.toString())
                .requestMatchers("group/**").hasAnyAuthority(Role.WORKSPACE_ADMIN.toString(), Role.STUDENT.toString(), Role.TEACHER.toString(), Role.OFFICER.toString())
                .requestMatchers("post/**").hasAnyAuthority(Role.STUDENT.toString(), Role.TEACHER.toString(), Role.OFFICER.toString())
                .requestMatchers("user/**").hasAnyAuthority(Role.STUDENT.toString(), Role.TEACHER.toString(), Role.OFFICER.toString())
                .requestMatchers("search/**").hasAnyAuthority(Role.STUDENT.toString(), Role.TEACHER.toString(), Role.OFFICER.toString())
                .anyRequest().permitAll()
                ;

        return http.build();
    }

}