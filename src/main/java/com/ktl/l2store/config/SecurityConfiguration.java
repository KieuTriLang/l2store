package com.ktl.l2store.config;

import java.util.Arrays;
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

                // http.authorizeRequests()
                // .antMatchers("/api/login/**", "/api/users/token/refresh/**",
                // "/api/users/register/**")
                // .permitAll();
                // // User
                // http.authorizeRequests()
                // .antMatchers("/api/users/").permitAll()
                // // .antMatchers("/api/users/").hasAnyAuthority("ROLE_SUPER_ADMIN")
                // .antMatchers("/api/users/role/**").hasAnyAuthority("ROLE_SUPER_ADMIN")
                // .antMatchers(HttpMethod.GET, "/api/users/**").permitAll();

                // // Product
                // http.authorizeRequests()
                // .antMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                // .antMatchers(HttpMethod.POST,
                // "/api/products/**/evaluates/**").hasAnyAuthority(
                // "ROLE_SUPER_ADMIN")
                // .antMatchers("/api/products/**").hasAnyAuthority("ROLE_SUPER_ADMIN");

                // // Category
                // http.authorizeRequests().antMatchers("/api/categories/**").permitAll();
                // // Image
                // http.authorizeRequests()
                // .antMatchers("/api/file/**").permitAll()
                // .antMatchers("/api/file/download/**").authenticated();

                http.authorizeRequests().anyRequest().permitAll();

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
                configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

}
