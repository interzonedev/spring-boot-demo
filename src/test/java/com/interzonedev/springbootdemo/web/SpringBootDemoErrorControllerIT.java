package com.interzonedev.springbootdemo.web;

import com.interzonedev.respondr.response.HttpResponse;
import com.interzonedev.zankou.dataset.DataSet;
import com.jayway.jsonassert.JsonAsserter;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletResponse;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

/**
 * Integration tests for the {@link SpringBootDemoErrorController#handleError(HttpServletResponse)} method using a
 * "live" web application context.
 */
public class SpringBootDemoErrorControllerIT extends SpringBootDemoWebIT {

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testHandleErrorNotFound() {
        String notFoundUrl = getFullTestURL("/foo");
        ResponseEntity<String> responseEntity = template.getForEntity(notFoundUrl, String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertIsJsonContentType(responseEntity);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertThat("$." + HttpResponse.PROCESSING_ERROR_KEY, is(HttpStatus.NOT_FOUND.getReasonPhrase()));
        jsonAsserter.assertNotDefined("$." + HttpResponse.VALIDATION_ERRORS_KEY);

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testHandleErrorMethodNotAllowed() throws Exception {
        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", "Testy");
        requestBody.add("lastName", "Testerson");
        requestBody.add("email", "testy.testerson@test.com");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(getFullTestURL("/user/1"), requestEntity, String.class);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertThat("$." + HttpResponse.PROCESSING_ERROR_KEY, is(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase()));
        jsonAsserter.assertNotDefined("$." + HttpResponse.VALIDATION_ERRORS_KEY);

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);
    }
}
