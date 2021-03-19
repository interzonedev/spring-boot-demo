package com.interzonedev.springbootdemo.web;

import com.interzonedev.blundr.ValidationHelper;
import com.interzonedev.respondr.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Test controller with a single method ({@link #fail()}) that always throws a {@link RuntimeException}.  Used for
 * testing uncaught exception handling.
 */
@RestController(value = "springbootdemo.web.failController")
@RequestMapping("/fail")
public class FailController extends SpringBootDemoController {

    private static final Logger log = LoggerFactory.getLogger(FailController.class);

    public static final String FAIL_MESSAGE = "Fail on purpose";

    @Inject
    public FailController(ApplicationContext applicationContext,
                          @Named("springbootdemo.web.jsonSerializer") Serializer serializer,
                          @Named("springbootdemo.service.validationHelper") ValidationHelper validationHelper,
                          @Named("springbootdemo.web.messageSource") MessageSource messageSource) {
        super(applicationContext, serializer, validationHelper, messageSource);
    }

    /**
     * Always throws {@link RuntimeException}.
     * 
     * @throws RuntimeException Always thrown.
     */
    @GetMapping
    public void fail() {
        try {
            log.debug("fail: Start");
            throw new RuntimeException(FAIL_MESSAGE);
        } finally {
            log.debug("fail: End");
        }
    }
}
