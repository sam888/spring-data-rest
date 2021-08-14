package com.example.datarest.web.config;

import com.example.datarest.web.filter.resolver.BasicAuthenticationResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add( authWebArgumentResolverFactory() );
    }

    @Bean
    public HandlerMethodArgumentResolver authWebArgumentResolverFactory() {
        return new BasicAuthenticationResolver();
    }

}
