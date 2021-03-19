package com.interzonedev.springbootdemo.web.user;

import com.google.common.base.Strings;
import com.interzonedev.respondr.response.HttpResponse;
import com.interzonedev.springbootdemo.model.User;
import com.interzonedev.springbootdemo.web.SpringBootDemoWebIT;
import com.interzonedev.zankou.dataset.DataSet;
import com.jayway.jsonassert.JsonAsserter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;

import javax.annotation.PostConstruct;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for the {@link UserController#updateUser(Long, UserForm, BindingResult)} method using a "live" web
 * application context.
 */
public class UserControllerUpdateUserIT extends SpringBootDemoWebIT {

    private static final Logger log = LoggerFactory.getLogger(UserControllerUpdateUserIT.class);

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_EMAIL = "testy.testerson@test.com";
    private static final String TEST_FIRST_NAME = "Testy";
    private static final String TEST_LAST_NAME = "Testerson";

    private String updateUserBaseUrl;
    private String updateTestUserUrl;

    @PostConstruct
    private void init() {
        updateUserBaseUrl = getFullTestURL("/user");
        updateTestUserUrl = updateUserBaseUrl + "/" + TEST_USER_ID;
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserNullFirstName() throws Exception {
        log.debug("testUpdateUserNullFirstName: Start");

        long now = System.currentTimeMillis();

        User userBeforeUpdate = getTestUser(TEST_USER_ID);

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.exchange(updateTestUserUrl, HttpMethod.PUT, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        assertSuccessfulUpdateJson(userBeforeUpdate, jsonAsserter, now);
        jsonAsserter.assertNull("$.user.firstName");
        jsonAsserter.assertEquals("$.user.lastName", TEST_LAST_NAME);
        jsonAsserter.assertEquals("$.user.email", TEST_EMAIL);

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterNullFirstNameUpdate.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserNullFirstName: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserBlankFirstName() throws Exception {
        log.debug("testUpdateUserBlankFirstName: Start");

        long now = System.currentTimeMillis();

        User userBeforeUpdate = getTestUser(TEST_USER_ID);

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", " ");
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.exchange(updateTestUserUrl, HttpMethod.PUT, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        assertSuccessfulUpdateJson(userBeforeUpdate, jsonAsserter, now);
        jsonAsserter.assertNull("$.user.firstName");
        jsonAsserter.assertEquals("$.user.lastName", TEST_LAST_NAME);
        jsonAsserter.assertEquals("$.user.email", TEST_EMAIL);

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterNullFirstNameUpdate.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserBlankFirstName: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserFirstNameTooLong() throws Exception {
        log.debug("testUpdateUserFirstNameTooLong: Start");

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", Strings.repeat("a", 256));
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.exchange(updateTestUserUrl, HttpMethod.PUT, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertIsJsonContentType(responseEntity);
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".firstName", hasSize(1));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/before.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserFirstNameTooLong: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserNullLastName() throws Exception {
        log.debug("testUpdateUserNullLastName: Start");

        long now = System.currentTimeMillis();

        User userBeforeUpdate = getTestUser(TEST_USER_ID);

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.exchange(updateTestUserUrl, HttpMethod.PUT, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        assertSuccessfulUpdateJson(userBeforeUpdate, jsonAsserter, now);
        jsonAsserter.assertEquals("$.user.firstName", TEST_FIRST_NAME);
        jsonAsserter.assertNull("$.user.lastName");
        jsonAsserter.assertEquals("$.user.email", TEST_EMAIL);

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterNullLastNameUpdate.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserNullLastName: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserBlankLastName() throws Exception {
        log.debug("testUpdateUserBlankLastName: Start");

        long now = System.currentTimeMillis();

        User userBeforeUpdate = getTestUser(TEST_USER_ID);

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("lastName", " ");
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.exchange(updateTestUserUrl, HttpMethod.PUT, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        assertSuccessfulUpdateJson(userBeforeUpdate, jsonAsserter, now);
        jsonAsserter.assertEquals("$.user.firstName", TEST_FIRST_NAME);
        jsonAsserter.assertNull("$.user.lastName");
        jsonAsserter.assertEquals("$.user.email", TEST_EMAIL);

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterNullLastNameUpdate.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserBlankLastName: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserLastNameTooLong() throws Exception {
        log.debug("testUpdateUserLastNameTooLong: Start");

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("lastName", Strings.repeat("a", 256));
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.exchange(updateTestUserUrl, HttpMethod.PUT, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertIsJsonContentType(responseEntity);
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".lastName", hasSize(1));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/before.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserLastNameTooLong: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserNullEmail() throws Exception {
        log.debug("testUpdateUserNullEmail: Start");

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("lastName", TEST_LAST_NAME);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.exchange(updateTestUserUrl, HttpMethod.PUT, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertIsJsonContentType(responseEntity);
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".email", hasSize(1));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserNullEmail: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserBlankEmail() throws Exception {
        log.debug("testUpdateUserBlankEmail: Start");

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", " ");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.exchange(updateTestUserUrl, HttpMethod.PUT, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertIsJsonContentType(responseEntity);
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".email", hasSize(1));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserBlankEmail: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserEmailTooLong() throws Exception {
        log.debug("testUpdateUserEmailTooLong: Start");

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", Strings.repeat("a", 256));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.exchange(updateTestUserUrl, HttpMethod.PUT, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertIsJsonContentType(responseEntity);
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".email", hasSize(1));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserEmailTooLong: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserDuplicateEmail() throws Exception {
        log.debug("testUpdateUserDuplicateEmail: Start");

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", "uncle@fester.com");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.exchange(updateTestUserUrl, HttpMethod.PUT, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertIsJsonContentType(responseEntity);
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".global", hasSize(1));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserDuplicateEmail: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserValid() throws Exception {
        log.debug("testUpdateUserValid: Start");

        long now = System.currentTimeMillis();

        User userBeforeUpdate = getTestUser(TEST_USER_ID);

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.exchange(updateTestUserUrl, HttpMethod.PUT, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        assertSuccessfulUpdateJson(userBeforeUpdate, jsonAsserter, now);
        jsonAsserter.assertEquals("$.user.firstName", TEST_FIRST_NAME);
        jsonAsserter.assertEquals("$.user.lastName", TEST_LAST_NAME);
        jsonAsserter.assertEquals("$.user.email", TEST_EMAIL);

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterUpdate.xml", "sbd_user", TIME_UPDATED_COLUMN_NAME);

        log.debug("testUpdateUserValid: End");
    }

    private void assertSuccessfulUpdateJson(User userBeforeUpdate, JsonAsserter jsonAsserter, long now) {
        jsonAsserter.assertNotDefined("$." + HttpResponse.ERROR_KEY);
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertNotDefined("$." + HttpResponse.VALIDATION_ERRORS_KEY);
        jsonAsserter.assertThat("$.user.id", is(userBeforeUpdate.getId().intValue()));
        jsonAsserter.assertThat("$.user.timeCreated", is(userBeforeUpdate.getTimeCreated().getTime()));
        jsonAsserter.assertThat("$.user.timeUpdated", greaterThan(now));
    }
}
