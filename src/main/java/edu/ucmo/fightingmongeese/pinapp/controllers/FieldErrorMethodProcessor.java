package edu.ucmo.fightingmongeese.pinapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Primary
@Component
public class FieldErrorMethodProcessor extends ExceptionHandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(PinController.class);

    public FieldErrorMethodProcessor() {
        setOrder(LOWEST_PRECEDENCE - 1);
    }

    @Override
    protected void logException(Exception ex, HttpServletRequest request) {
        logger.warn("Received request from {} that threw exception. {}", request.getRemoteAddr(), ex.getLocalizedMessage());
    }

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request,
                                                           HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {

        return super.doResolveHandlerMethodException(request, response, handlerMethod, exception);
    }

}
