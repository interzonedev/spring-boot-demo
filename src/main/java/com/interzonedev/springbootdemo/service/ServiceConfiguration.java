package com.interzonedev.springbootdemo.service;

import com.interzonedev.blundr.ValidationHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Configuration class for beans used across the service layer.
 */
@Configuration("springbootdemo.service.config")
public class ServiceConfiguration {
    /**
     * Gets the {@link Validator} instance to be used across the service layer of the application.
     *
     * @return Returns a {@link LocalValidatorFactoryBean} instance.
     */
    @Bean(name = "springbootdemo.service.jsr303Validator")
    public Validator getLocalValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    /**
     * Gets the {@link ValidationHelper} instance to be used across the service layer of the application.
     *
     * @return Returns a {@link ValidationHelper} instance.
     */
    @Bean(name = "springbootdemo.service.validationHelper")
    public ValidationHelper getValidationHelper() {
        return new ValidationHelper();
    }
}
