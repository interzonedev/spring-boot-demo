package com.interzonedev.springbootdemo.web.user;

import com.google.common.base.Strings;
import com.interzonedev.respondr.response.HttpResponse;
import com.interzonedev.springbootdemo.web.SpringBootDemoWebIT;
import com.interzonedev.springbootdemo.web.WebConfiguration;
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

import java.util.Locale;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for the internationalization of the responses from the
 * {@link UserController#createUser(UserForm, BindingResult)} method using a "live" web application context.
 */
public class UserControllerCreateUserI18NIT extends SpringBootDemoWebIT {

    private static final Logger log = LoggerFactory.getLogger(UserControllerCreateUserI18NIT.class);

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
    public void testCreateUserFirstNameTooLongNoParamNoCookie() throws Exception {
        log.debug("testCreateUserFirstNameTooLongNoParamNoCookie: Start");

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
        assertNoSetCookieHeader(responseEntity);
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".firstName", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".firstName[0]", containsString("First name"));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserFirstNameTooLongNoParamNoCookie: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserFirstNameTooLongParamEnglish() throws Exception {
        log.debug("testCreateUserFirstNameTooLongParamEnglish: Start");

        String createUserUrlWithLangParam = createUserUrl + "/?" +
                WebConfiguration.LOCALE_CHANGE_INTERCEPTOR_PARAM_NAME+ "=" + Locale.ENGLISH;

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", Strings.repeat("a", 256));
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrlWithLangParam, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertIsJsonContentType(responseEntity);
        assertLangSetCookieHeader(responseEntity, Locale.ENGLISH.toString());
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".firstName", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".firstName[0]", containsString("First name"));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserFirstNameTooLongParamEnglish: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserFirstNameTooLongParamGerman() throws Exception {
        log.debug("testCreateUserFirstNameTooLongParamGerman: Start");

        String createUserUrlWithLangParam = createUserUrl + "/?" +
                WebConfiguration.LOCALE_CHANGE_INTERCEPTOR_PARAM_NAME+ "=" + Locale.GERMAN;

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", Strings.repeat("a", 256));
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrlWithLangParam, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertIsJsonContentType(responseEntity);
        assertLangSetCookieHeader(responseEntity, Locale.GERMAN.toString());
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".firstName", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".firstName[0]", containsString("Vorname"));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserFirstNameTooLongParamGerman: End");
    }


    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserFirstNameTooLongCookieEnglish() throws Exception {
        log.debug("testCreateUserFirstNameTooLongCookieEnglish: Start");

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();
        addLangCookieToHttpHeaders(requestHeaders, Locale.ENGLISH.toString());

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", Strings.repeat("a", 256));
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrl, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertIsJsonContentType(responseEntity);
        assertNoSetCookieHeader(responseEntity);
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".firstName", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".firstName[0]", containsString("First name"));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserFirstNameTooLongCookieEnglish: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserFirstNameTooLongCookieGerman() throws Exception {
        log.debug("testCreateUserFirstNameTooLongCookieGerman: Start");

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();
        addLangCookieToHttpHeaders(requestHeaders, Locale.GERMAN.toString());

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", Strings.repeat("a", 256));
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrl, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertIsJsonContentType(responseEntity);
        assertNoSetCookieHeader(responseEntity);
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".firstName", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".firstName[0]", containsString("Vorname"));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserFirstNameTooLongCookieGerman: End");
    }

    /*
     * The i18n query string parameter has precedence over the i18n cookie.
     */
    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserFirstNameTooLongParamAndCookieDifferent() throws Exception {
        log.debug("testCreateUserFirstNameTooLongParamAndCookieDifferent: Start");

        String createUserUrlWithLangParam = createUserUrl + "/?" +
                WebConfiguration.LOCALE_CHANGE_INTERCEPTOR_PARAM_NAME+ "=" + Locale.ENGLISH;

        HttpHeaders requestHeaders = getApplicationFormUrlencodedHeaders();
        addLangCookieToHttpHeaders(requestHeaders, Locale.GERMAN.toString());

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("firstName", Strings.repeat("a", 256));
        requestBody.add("lastName", TEST_LAST_NAME);
        requestBody.add("email", TEST_EMAIL);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

        ResponseEntity<String> responseEntity = template.postForEntity(createUserUrlWithLangParam, requestEntity, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertIsJsonContentType(responseEntity);
        assertLangSetCookieHeader(responseEntity, Locale.ENGLISH.toString());
        jsonAsserter.assertNotDefined("$.user");
        jsonAsserter.assertThat("$." + HttpResponse.ERROR_KEY, is(true));
        jsonAsserter.assertNotDefined("$." + HttpResponse.PROCESSING_ERROR_KEY);
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + "[*]]", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".firstName", hasSize(1));
        jsonAsserter.assertThat("$." + HttpResponse.VALIDATION_ERRORS_KEY + ".firstName[0]", containsString("First name"));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserFirstNameTooLongParamAndCookieDifferent: End");
    }
}
