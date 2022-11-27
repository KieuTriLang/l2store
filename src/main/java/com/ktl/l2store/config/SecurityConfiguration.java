package com.ktl.l2store.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

                http.cors(Customizer.withDefaults());
                http.csrf().disable();
                http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

                http.authorizeRequests()
                                .antMatchers("/api/login/**", "/api/users/token/refresh/**",
                                                "/api/users/register/**", "/api/super-admin/**")
                                .permitAll();
                // User
                http.authorizeRequests()
                                .antMatchers("/api/users/**").permitAll()
                                .antMatchers("/api/users").hasAnyAuthority("ROLE_MANAGER")
                                .antMatchers("/api/users/total-user").hasAnyAuthority("ROLE_MANAGER")
                                .antMatchers(HttpMethod.GET, "/api/users/profile").hasAnyAuthority("ROLE_USER")
                                .antMatchers(HttpMethod.POST, "/api/users/change-password").hasAnyAuthority("ROLE_USER")
                                .antMatchers(HttpMethod.PUT, "/api/users/update").hasAnyAuthority("ROLE_USER");

                // Product
                http.authorizeRequests()
                                .antMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                                .antMatchers(HttpMethod.POST, "/api/products/**/evaluates/**")
                                .hasAnyAuthority("ROLE_USER")
                                .antMatchers("/api/products/**").hasAnyAuthority("ROLE_MANAGER");

                // Category
                http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/categories").permitAll()
                                .antMatchers("/api/categories/**").hasAnyAuthority("ROLE_MANAGER");
                // Order
                http.authorizeRequests()
                                .antMatchers(HttpMethod.GET, "/api/orders").hasAnyAuthority("ROLE_MANAGER")
                                .antMatchers("/api/orders/order-state").hasAnyAuthority("ROLE_MANAGER")
                                .antMatchers("/api/orders/data-order-statistic").hasAnyAuthority("ROLE_MANAGER")
                                .antMatchers(HttpMethod.GET, "/api/orders/**")
                                .hasAnyAuthority("ROLE_MANAGER", "ROLE_USER")
                                .antMatchers(HttpMethod.GET, "/api/orders/my-orders/**").hasAnyAuthority("ROLE_USER")
                                .antMatchers("/api/orders/**").hasAnyAuthority("ROLE_USER")
                                .antMatchers("/api/orders/paypal/cancel-payment/**").hasAnyAuthority("ROLE_USER");
                // ComboProduct
                http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/combos/**").permitAll()
                                .antMatchers("/api/combos/**").hasAnyAuthority("ROLE_MANAGER");
                // Evauate
                http.authorizeRequests().antMatchers("/api/evaluates/**")
                                .hasAnyAuthority("ROLE_MANAGER");
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

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("*"));
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

}
