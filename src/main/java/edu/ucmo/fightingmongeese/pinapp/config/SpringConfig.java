package edu.ucmo.fightingmongeese.pinapp.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Configuration
public class SpringConfig {

    //    private final AutowireCapableBeanFactory autowireCapableBeanFactory;
//
//    @Autowired
//    public SpringConfig(AutowireCapableBeanFactory autowireCapableBeanFactory) {
//        this.autowireCapableBeanFactory = autowireCapableBeanFactory;
//    }
//
//    @Bean
//    public Validator validator(final AutowireCapableBeanFactory autowireCapableBeanFactory) {
//
//        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
//                .configure().constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
//                .buildValidatorFactory();
//
//
//        return validatorFactory.getValidator();
//    }
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();
        methodValidationPostProcessor.setValidator(validator());
        return methodValidationPostProcessor;
    }
}
