package edu.ucmo.fightingmongeese.pinapp.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Component to intercept requests as they come in.
 * Necessary for request logging as the request will never reach a controller if field validation fails
 */
@Component
public class RequestInterceptor
        extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

    /**
     * Method for handling all incoming requests.  The request must be wrapped before we interact with it
     * as the it tracks access and will be considered handled if we access it directly.
     * We simply log the incoming IP and the path the request came in on.
     *
     * @param request
     * @param response
     * @param handler
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpServletRequest requestCacheWrapperObject = new ContentCachingRequestWrapper(request);
        requestCacheWrapperObject.getParameterMap();
        logger.info("Request received from IP {} on URI: {}", requestCacheWrapperObject.getRemoteAddr(), requestCacheWrapperObject.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
    }
}
