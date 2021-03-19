package com.interzonedev.springbootdemo.web;

import com.interzonedev.springbootdemo.Application;
import com.interzonedev.springbootdemo.SpringBootDemoIT;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

/**
 * Abstract subclass of {@link SpringBootDemoIT} specifically for integration tests that require a mock web application
 * context.  Injects a {@link MockMvc} instance for use by implementing classes.
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
public abstract class SpringBootDemoMockWebIT extends SpringBootDemoIT {

    @Inject
    protected MockMvc mvc;

}
