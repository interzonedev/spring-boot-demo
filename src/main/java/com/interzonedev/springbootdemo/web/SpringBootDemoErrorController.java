package com.interzonedev.springbootdemo.web;

import com.interzonedev.blundr.ValidationHelper;
import com.interzonedev.respondr.response.HttpResponse;
import com.interzonedev.respondr.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

/**
 * Web controller that allows the return of a JSON response body with the proper HTTP response status for errors that
 * would ordinarily generate an HTML response straight from the servlet container.
 *
 * By default, if an incoming request generates a 404 (not found) or 405 (bad request) the request will never reach
 * any of the other controllers in this application and will instead be processed by the servlet container.  With
 * {@code server.error.whitelabel.enabled=false} set in {@code application.properties} this controller handles all
 * errors unable to be handled by other application web controllers via the {@link #handleError(HttpServletResponse)}
 * method.
 */
@RestController(value = "springbootdemo.web.errorController")
public class SpringBootDemoErrorController extends SpringBootDemoController implements ErrorController {

    private static final Logger log = LoggerFactory.getLogger(SpringBootDemoErrorController.class);

    public static final String ERROR_PATH = "/error";

    @Inject
    public SpringBootDemoErrorController(ApplicationContext applicationContext,
                                         @Named("springbootdemo.web.jsonSerializer") Serializer serializer,
                                         @Named("springbootdemo.service.validationHelper") ValidationHelper validationHelper,
                                         @Named("springbootdemo.web.messageSource") MessageSource messageSource) {
        super(applicationContext, serializer, validationHelper, messageSource);
    }

    /**
     * Gets the URL path for handling all errors unable to be handled by any of the other controllers in this
     * application.
     *
     * @return Returns {@link #ERROR_PATH}.
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    /**
     * Handles all errors unable to be handled by any of the other controllers in this application allowed by the
     * URL mapping to {@link #ERROR_PATH}.
     *
     * @param response The {@link HttpServletResponse} being returned by the servlet container for the current request.
     *
     * @return Returns a JSON error response body with the HTTP response status set from the incoming
     * {@link HttpServletResponse}.
     */
    @RequestMapping(value = ERROR_PATH)
    public ResponseEntity<String> handleError(HttpServletResponse response) {
        int httpStatusCode = response.getStatus();
        HttpStatus httpStatus = HttpStatus.valueOf(httpStatusCode);
        log.error("handleError: httpStatus = " + httpStatus);
        return HttpResponse.getHttpStatusJsonResponseEntity(httpStatus, serializer);
    }
}
