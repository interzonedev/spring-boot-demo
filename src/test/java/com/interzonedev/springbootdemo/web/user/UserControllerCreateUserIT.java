package com.interzonedev.springbootdemo.web.user;

import com.google.common.base.Strings;
import com.interzonedev.respondr.response.HttpResponse;
import com.interzonedev.springbootdemo.web.SpringBootDemoWebIT;
import com.interzonedev.zankou.dataset.DataSet;
import com.jayway.jsonassert.JsonAsserter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
 * Integration tests for the {@link UserController#createUser(UserForm, BindingResult)} method using a "live" web
 * application context.
 */
public class UserControllerCreateUserIT extends SpringBootDemoWebIT {

    private static final Logger log = LoggerFactory.getLogger(UserControllerCreateUserIT.class);

    private static final String TEST_EMAIL = "testy.testerson@test.com";
    private static final String TEST_FIRST_NAME = "Testy";
    private static final String TEST_LAST_NAME = "Testerson";

    private String createUserUrl;

    @PostConstruct
    private void init() {
        createUserUrl = getFullTestURL("/user");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserNullFirstName() throws Exception {
        log.debug("testCreateUserNullFirstName: Start");

        long now = System.currentTimeMillis();

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrl, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        assertSuccessfulUserJson(jsonAsserter, now);
        jsonAsserter.assertNull("$.user.firstName");
        jsonAsserter.assertEquals("$.user.lastName", TEST_LAST_NAME);
        jsonAsserter.assertEquals("$.user.email", TEST_EMAIL);

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterNullFirstNameCreate.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserNullFirstName: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserBlankFirstName() throws Exception {
        log.debug("testCreateUserBlankFirstName: Start");

        long now = System.currentTimeMillis();

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", " ");
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrl, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        assertSuccessfulUserJson(jsonAsserter, now);
        jsonAsserter.assertNull("$.user.firstName");
        jsonAsserter.assertEquals("$.user.lastName", TEST_LAST_NAME);
        jsonAsserter.assertEquals("$.user.email", TEST_EMAIL);

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterNullFirstNameCreate.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserBlankFirstName: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserFirstNameTooLong() throws Exception {
        log.debug("testCreateUserFirstNameTooLong: Start");

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", Strings.repeat("a", 256));
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrl, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertIsJsonContentType(responseEntity);
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".firstName", hasSize(1));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserFirstNameTooLong: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserNullLastName() throws Exception {
        log.debug("testCreateUserNullLastName: Start");

        long now = System.currentTimeMillis();

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrl, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        assertSuccessfulUserJson(jsonAsserter, now);
        jsonAsserter.assertEquals("$.user.firstName", TEST_FIRST_NAME);
        jsonAsserter.assertNull("$.user.lastName");
        jsonAsserter.assertEquals("$.user.email", TEST_EMAIL);

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterNullLastNameCreate.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserNullLastName: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserBlankLastName() throws Exception {
        log.debug("testCreateUserBlankLastName: Start");

        long now = System.currentTimeMillis();

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("lastName", " ");
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrl, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        assertSuccessfulUserJson(jsonAsserter, now);
        jsonAsserter.assertEquals("$.user.firstName", TEST_FIRST_NAME);
        jsonAsserter.assertNull("$.user.lastName");
        jsonAsserter.assertEquals("$.user.email", TEST_EMAIL);

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterNullLastNameCreate.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserBlankLastName: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserLastNameTooLong() throws Exception {
        log.debug("testCreateUserLastNameTooLong: Start");

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("lastName", Strings.repeat("a", 256));
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrl, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertIsJsonContentType(responseEntity);
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".lastName", hasSize(1));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserLastNameTooLong: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserNullEmail() throws Exception {
        log.debug("testCreateUserNullEmail: Start");

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("lastName", TEST_LAST_NAME);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrl, requestEntity, String.class);

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

        log.debug("testCreateUserNullEmail: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserBlankEmail() throws Exception {
        log.debug("testCreateUserBlankEmail: Start");

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", " ");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrl, requestEntity, String.class);

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

        log.debug("testCreateUserBlankEmail: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserEmailTooLong() throws Exception {
        log.debug("testCreateUserEmailTooLong: Start");

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", Strings.repeat("a", 256));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrl, requestEntity, String.class);

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

        log.debug("testCreateUserEmailTooLong: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserDuplicateEmail() throws Exception {
        log.debug("testCreateUserDuplicateEmail: Start");

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", "gern@blanston.com");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrl, requestEntity, String.class);

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

        log.debug("testCreateUserDuplicateEmail: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserValid() throws Exception {
        log.debug("testCreateUserValid: Start");

        long now = System.currentTimeMillis();

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", TEST_FIRST_NAME);
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrl, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        assertSuccessfulUserJson(jsonAsserter, now);
        jsonAsserter.assertEquals("$.user.firstName", TEST_FIRST_NAME);
        jsonAsserter.assertEquals("$.user.lastName", TEST_LAST_NAME);
        jsonAsserter.assertEquals("$.user.email", TEST_EMAIL);

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterCreate.xml", "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserValid: End");
    }

    private void assertSuccessfulUserJson(JsonAsserter jsonAsserter, long now) {
        jsonAsserter.assertNotDefined("$." + HttpResponse.ERROR_KEY);
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertNotDefined("$." + HttpResponse.VALIDATION_ERRORS_KEY);
        jsonAsserter.assertThat("$.user.id", greaterThan(0));
        jsonAsserter.assertThat("$.user.timeCreated", greaterThan(now));
        jsonAsserter.assertThat("$.user.timeUpdated", greaterThan(now));
    }
}
