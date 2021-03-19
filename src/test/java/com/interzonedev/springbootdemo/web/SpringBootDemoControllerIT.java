package com.interzonedev.springbootdemo.web;

import com.interzonedev.respondr.response.HttpResponse;
import com.interzonedev.zankou.dataset.DataSet;
import com.jayway.jsonassert.JsonAsserter;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for the {@link SpringBootDemoController#handleException(Throwable)} method using a "live" web
 * application context using the {@link FailController#fail()} method.
 */
public class SpringBootDemoControllerIT extends SpringBootDemoWebIT {

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testHandleException() {
        String failUrl = getFullTestURL("/fail");
        ResponseEntity<String> responseEntity = template.getForEntity(failUrl, String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertIsJsonContentType(responseEntity);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertThat("$." + HttpResponse.PROCESSING_ERROR_KEY, is(FailController.FAIL_MESSAGE));
        jsonAsserter.assertNotDefined("$." + HttpResponse.VALIDATION_ERRORS_KEY);

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);
    }

}
