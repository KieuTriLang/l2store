package com.ktl.l2store.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.ktl.l2store.utils.PagingResolver;

@Configuration
public class RegisterResolverConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // TODO Auto-generated method stub
        argumentResolvers.add(new PagingResolver());
    }

}
