package com.interzonedev.springbootdemo.web;

import com.interzonedev.respondr.serialize.JsonSerializer;
import com.interzonedev.respondr.serialize.Serializer;
import com.interzonedev.springbootdemo.web.servlet.RequestResponseLoggingFilter;
import com.interzonedev.springbootdemo.web.servlet.SpringBootDemoServletRequestListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.ServletRequestListener;
import java.util.Locale;

/**
 * Configuration class for beans used across the web layer of the application.
 */
@Configuration("springbootdemo.web.config")
public class WebConfiguration implements WebMvcConfigurer {
    public static final String LOCALE_RESOLVER_COOKIE_NAME = "sbd_locale";

    public static final String LOCALE_CHANGE_INTERCEPTOR_PARAM_NAME = "lang";

    /**
     * Gets the {@link Serializer} instance to be used across the web layer of the application.
     *
     * @return Returns a {@link JsonSerializer} instance.
     */
    @Bean(name = "springbootdemo.web.jsonSerializer")
    public Serializer getSerializer() {
        return new JsonSerializer();
    }

    /**
     * Gets the {@link LocaleResolver} instance to be used across the web layer of the application.
     *
     * @return Returns a {@link CookieLocaleResolver} instance with the cookie name set to
     * {@link #LOCALE_RESOLVER_COOKIE_NAME}.
     */
    @Bean(name = {"localeResolver", "springbootdemo.web.localeResolver"})
    public LocaleResolver getLocaleResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setCookieName(LOCALE_RESOLVER_COOKIE_NAME);
        cookieLocaleResolver.setDefaultLocale(Locale.US);
        return cookieLocaleResolver;
    }

    /**
     * Gets the {@link LocaleChangeInterceptor} instance to be used across the web layer of the application.
     *
     * @return Returns a {@link LocaleChangeInterceptor} instance with the param name set to
     * {@link #LOCALE_CHANGE_INTERCEPTOR_PARAM_NAME}.
     */
    @Bean(name = "springbootdemo.web.localeChangeInterceptor")
    public LocaleChangeInterceptor getLocaleChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(LOCALE_CHANGE_INTERCEPTOR_PARAM_NAME);
        return localeChangeInterceptor;
    }

    /**
     * Gets the {@link MessageSource} instance to be used across the web layer of the application.
     *
     * @return Returns a {@link ReloadableResourceBundleMessageSource} instance set to cache messages for the lifetime
     * of the application.
     */
    @Bean(name = {"messageSource", "springbootdemo.web.messageSource"})
    public MessageSource getMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames("classpath:messages/errors/errors", "classpath:messages/views/views");
        messageSource.setCacheSeconds(-1);
        return messageSource;
    }

    /**
     * Register interceptors for the web application.
     *
     * @param registry The {@link InterceptorRegistry} for the application.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLocaleChangeInterceptor());
    }

    /**
     * Register mappings of URLs to views that do not require a controller.
     *
     * @param registry The {@link ViewControllerRegistry} for the application.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/users").setViewName("users");
    }

    @Bean(name = "springbootdemo.web.servletListenerRegistrationBean")
    public ServletListenerRegistrationBean<ServletRequestListener> registerServletListener() {
        ServletListenerRegistrationBean<ServletRequestListener> srb = new ServletListenerRegistrationBean<>();
        srb.setListener(new SpringBootDemoServletRequestListener());
        return srb;
    }

    @Bean(name = "springbootdemo.web.filterRegistrationBean")
    public FilterRegistrationBean<RequestResponseLoggingFilter> registerFilters() {
        FilterRegistrationBean<RequestResponseLoggingFilter> frb = new FilterRegistrationBean<>();
        frb.setFilter(new RequestResponseLoggingFilter());
        return frb;
    }

}
