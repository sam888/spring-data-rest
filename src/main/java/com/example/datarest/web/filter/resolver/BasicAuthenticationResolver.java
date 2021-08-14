package com.example.datarest.web.filter.resolver;

import com.example.datarest.annotation.BasicAuthentication;
import com.example.datarest.exception.BasicAuthenticationException;
import com.example.datarest.web.handler.BasicAuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class BasicAuthenticationResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private BasicAuthenticationHandler basicAccessAuthenticationHandler;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterAnnotation( BasicAuthentication.class ) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
        NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        if (methodParameter.getParameterType() == BasicAuthentication.class) {

            // Not throwing exception means successful Basic Authentication here
            basicAccessAuthenticationHandler.getPerson( nativeWebRequest );

            return null; // Return anything other than null can cause IllegalArgumentException
        }
        throw new BasicAuthenticationException();
    }
}
