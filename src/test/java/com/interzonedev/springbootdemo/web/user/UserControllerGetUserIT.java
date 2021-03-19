package com.interzonedev.springbootdemo.web.user;

import com.interzonedev.respondr.response.HttpResponse;
import com.interzonedev.springbootdemo.web.SpringBootDemoWebIT;
import com.interzonedev.zankou.dataset.DataSet;
import com.jayway.jsonassert.JsonAsserter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;

/**
 * Integration tests for the {@link UserController#getUser(Long)} method using a "live" web application context.
 */
public class UserControllerGetUserIT extends SpringBootDemoWebIT {

    private static final Logger log = LoggerFactory.getLogger(UserControllerGetUserIT.class);

    private String getUserBaseUrl;

    @PostConstruct
    private void init() {
        getUserBaseUrl = getFullTestURL("/user");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testGetUserByIdNotPositive() throws Exception {
        log.debug("testGetUserByIdNotPositive: Start");

        int testUserId = 0;

        String getTestUserUrl = getUserBaseUrl + "/" + testUserId;

        ResponseEntity<String> responseEntity = template.getForEntity(getTestUserUrl, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        jsonAsserter.assertNotDefined("$.users");
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".global", hasSize(1));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);

        log.debug("testGetUserByIdNotPositive: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testGetUserByIdNonExistent() throws Exception {
        log.debug("testGetUserByIdNonExistent: Start");

        int testUserId = 100;

        String getTestUserUrl = getUserBaseUrl + "/" + testUserId;

        ResponseEntity<String> responseEntity = template.getForEntity(getTestUserUrl, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        jsonAsserter.assertNotDefined("$.users");
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotNull("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertNotDefined("$." + HttpResponse.VALIDATION_ERRORS_KEY);

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);

        log.debug("testGetUserByIdNonExistent: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testGetUserByIdValid() throws Exception {
        log.debug("testGetUserByIdValid: Start");

        int testUserId = 1;

        String getTestUserUrl = getUserBaseUrl + "/" + testUserId;

        ResponseEntity<String> responseEntity = template.getForEntity(getTestUserUrl, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        jsonAsserter.assertNotDefined("$.users");
        jsonAsserter.assertEquals("$.user.id", testUserId);
        jsonAsserter.assertEquals("$.user.firstName", "Gern");
        jsonAsserter.assertEquals("$.user.lastName", "Blanston");
        jsonAsserter.assertEquals("$.user.email", "gern@blanston.com");
        jsonAsserter.assertThat("$.user.timeCreated", greaterThan(0L));
        jsonAsserter.assertThat("$.user.timeUpdated", greaterThan(0L));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);

        log.debug("testGetUserByIdValid: End");
    }
}
