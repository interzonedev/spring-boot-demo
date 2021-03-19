package com.interzonedev.springbootdemo.web;

import com.interzonedev.blundr.ValidationHelper;
import com.interzonedev.respondr.response.HttpResponse;
import com.interzonedev.respondr.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

/**
 * Abstract top level controller for all web controllers in the application. Provides common properties, helper methods
 * and error handling.
 */
public abstract class SpringBootDemoController {

    private static final Logger log = LoggerFactory.getLogger(SpringBootDemoController.class);

    protected final ApplicationContext applicationContext;

    protected final Serializer serializer;

    protected final ValidationHelper validationHelper;

    protected final MessageSource messageSource;

    public SpringBootDemoController(ApplicationContext applicationContext,
                                    Serializer serializer,
                                    ValidationHelper validationHelper,
                                    MessageSource messageSource) {
        this.applicationContext = applicationContext;
        this.serializer = serializer;
        this.validationHelper = validationHelper;
        this.messageSource = messageSource;
    }

    /**
     * Gets the {@link Locale} to be used on the current request.
     *
     * @return Returns {@link Locale#US}.
     */
    protected Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    /**
     * General exception handler for any {@link Throwable} that makes it out of any endpoint of any implementing
     * controller. Handles the {@link Throwable} as a processing error and set the response status to a 500 internal
     * service error.
     *
     * @param t The {@link Throwable} caught.
     *
     * @return Returns a {@link ResponseEntity} that contains a JSON response ({@link String}) with information about
     * the {@link Throwable} set as a processing error.
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleException(Throwable t) {
        log.error("handleException", t);

        try {
            HttpResponse errorHttpResponse = HttpResponse
                    .newBuilder()
                    .setThrowable(t)
                    .setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .setContentType(HttpResponse.JSON_CONTENT_TYPE)
                    .build();
            return errorHttpResponse.toResponseEntity(serializer);
        } catch (Throwable t2) {
            return HttpResponse.getDefaultJsonErrorResponseEntity();
        }
    }
}
