package edu.ucmo.fightingmongeese.pinapp.config;

import edu.ucmo.fightingmongeese.pinapp.controllers.FieldErrorMethodProcessor;
import edu.ucmo.fightingmongeese.pinapp.controllers.PinController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@Configuration
public class ExceptionConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(PinController.class);


    public ExceptionConfiguration() {
        logger.info("Creating ExceptionConfiguration");
    }

    /**
     * Setup the classic SimpleMappingExceptionResolver. This provides useful
     * defaults for logging and handling exceptions. It has been part of Spring
     * MVC since Spring V2 and you will probably find most existing Spring MVC
     * applications are using it.
     * <p>
     * Only invoked if the "global" profile is active.
     *
     * @return The new resolver
     */
    @Bean(name = "FieldErrorMethodProcessor")
    public FieldErrorMethodProcessor create() {
        logger.info("Creating SimpleMappingExceptionResolver");
        FieldErrorMethodProcessor ex = new FieldErrorMethodProcessor();
        return ex;
    }

}
