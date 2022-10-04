package com.ktl.l2store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ktl.l2store.filter.CustomAuthenticaionFilter;
import com.ktl.l2store.filter.CustomAuthorizationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final UserDetailsService userDetailsService;

        private final BCryptPasswordEncoder bCryptPasswordEncoder;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {

                auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

                CustomAuthenticaionFilter customAuthenticaionFilter = new CustomAuthenticaionFilter(
                                authenticationManagerBean());
                customAuthenticaionFilter.setFilterProcessesUrl("/api/login");

                http.csrf().disable();
                http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

                http.authorizeRequests()
                                .antMatchers("/api/login/**", "/api/user/token/refresh/**", "/api/user/register/**")
                                .permitAll();
                // User
                http.authorizeRequests()
                                .antMatchers("/api/users/").permitAll()
                                // .antMatchers("/api/users/").hasAnyAuthority("ROLE_SUPER_ADMIN")
                                .antMatchers("/api/users/role/**").hasAnyAuthority("ROLE_SUPER_ADMIN")
                                .antMatchers(HttpMethod.GET, "/api/users/**").permitAll();

                // Product
                http.authorizeRequests()
                                .antMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                                .antMatchers(HttpMethod.POST, "/api/products/**/evaluates/**").hasAnyAuthority(
                                                "ROLE_SUPER_ADMIN")
                                .antMatchers("/api/products/**").hasAnyAuthority("ROLE_SUPER_ADMIN");

                // Image
                http.authorizeRequests()
                                .antMatchers("/api/file/**").permitAll()
                                .antMatchers("/api/file/download/**").authenticated();

                http.authorizeRequests().anyRequest().authenticated();

                http.addFilter(customAuthenticaionFilter);
                http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {

                return super.authenticationManagerBean();
        }

}
