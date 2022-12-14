package com.example.spring_rest_api_session_java7.security.config;

import com.example.spring_rest_api_session_java7.security.jwt.JwtFilter;
import com.example.spring_rest_api_session_java7.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * author: Ulansky
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final JwtFilter jwtFilter;

    @Autowired
    PasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, JwtFilter jwtFilter) {
        this.userService = userService;
        this.jwtFilter = jwtFilter;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/api/jwt/**").permitAll()
//                .antMatchers("/company/getAll").hasAnyAuthority("Admin", "Instructor", "Student")
//                .antMatchers("/company/save").hasAuthority("Admin")
//                .antMatchers("/company/update").hasAnyAuthority("Admin", "Instructor")
//                .antMatchers("/company/delete").hasAuthority("Admin")
                .anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


//        http.authorizeRequests()
//                .antMatchers("/getAllCompanies").hasAnyAuthority("Admin", "Instructor", "Student")
//                .antMatchers("/addCompany").hasAuthority("Admin")
//                .antMatchers("/updateCompany").hasAnyAuthority("Admin", "Instructor")
//                .antMatchers("/deleteCompany").hasAuthority("Admin")
//
//
//
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .defaultSuccessUrl("/getAllCompanies")
//                .permitAll()
//                .and()
//                .logout().permitAll();
    }
}
