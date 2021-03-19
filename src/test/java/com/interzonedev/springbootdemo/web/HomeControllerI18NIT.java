package com.interzonedev.springbootdemo.web;

import com.interzonedev.zankou.dataset.DataSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HomeControllerI18NIT extends SpringBootDemoWebIT {

    private static final Logger log = LoggerFactory.getLogger(HomeControllerI18NIT.class);

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void getHomeNoParamNoCookie() throws Exception {
        log.debug("getHomeNoParamNoCookie: Start");

        String homeUrl = getFullTestURL("/");

        ResponseEntity<String> responseEntity = template.getForEntity(homeUrl, String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertIsHtmlContentType(responseEntity);
        assertNoSetCookieHeader(responseEntity);
        assertThat(responseEntity.getBody(), containsString("Spring Boot Demo - Welcome"));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);

        log.debug("getHomeNoParamNoCookie: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void getHomeParamEnglish() throws Exception {
        log.debug("getHomeParamEnglish: Start");

        String homeUrl = getFullTestURL("/?" + WebConfiguration.LOCALE_CHANGE_INTERCEPTOR_PARAM_NAME + "=" + Locale.ENGLISH);

        ResponseEntity<String> responseEntity = template.getForEntity(homeUrl, String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertIsHtmlContentType(responseEntity);
        assertLangSetCookieHeader(responseEntity, Locale.ENGLISH.toString());
        assertThat(responseEntity.getBody(), containsString("Spring Boot Demo - Welcome"));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);

        log.debug("getHomeParamEnglish: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void getHomeParamGerman() throws Exception {
        log.debug("getHomeParamGerman: Start");

        String homeUrl = getFullTestURL("/?" + WebConfiguration.LOCALE_CHANGE_INTERCEPTOR_PARAM_NAME + "=" + Locale.GERMAN);

        ResponseEntity<String> responseEntity = template.getForEntity(homeUrl, String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertIsHtmlContentType(responseEntity);
        assertLangSetCookieHeader(responseEntity, Locale.GERMAN.toString());
        assertThat(responseEntity.getBody(), containsString("Spring Boot Demo - Willkommen"));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);

        log.debug("getHomeParamGerman: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void getHomeCookieEnglish() throws Exception {
        log.debug("getHomeCookieEnglish: Start");

        String homeUrl = getFullTestURL("/");

        HttpEntity requestEntity = getHttpEntityWithLangCookie(Locale.ENGLISH.toString());

        ResponseEntity<String> responseEntity = template.exchange(homeUrl, HttpMethod.GET, requestEntity, String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertIsHtmlContentType(responseEntity);
        assertNoSetCookieHeader(responseEntity);
        assertThat(responseEntity.getBody(), containsString("Spring Boot Demo - Welcome"));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);

        log.debug("getHomeCookieEnglish: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void getHomeCookieGerman() throws Exception {
        log.debug("getHomeCookieGerman: Start");

        String homeUrl = getFullTestURL("/");

        HttpEntity requestEntity = getHttpEntityWithLangCookie(Locale.GERMAN.toString());

        ResponseEntity<String> responseEntity = template.exchange(homeUrl, HttpMethod.GET, requestEntity, String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertIsHtmlContentType(responseEntity);
        assertNoSetCookieHeader(responseEntity);
        assertThat(responseEntity.getBody(), containsString("Spring Boot Demo - Willkommen"));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);

        log.debug("getHomeCookieGerman: End");
    }

    /*
     * The i18n query string parameter has precedence over the i18n cookie.
     */
    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void getHomeParamAndCookieDifferent() throws Exception {
        log.debug("getHomeParamAndCookieDifferent: Start");

        String homeUrl = getFullTestURL("/?" + WebConfiguration.LOCALE_CHANGE_INTERCEPTOR_PARAM_NAME + "=" + Locale.ENGLISH);

        HttpEntity requestEntity = getHttpEntityWithLangCookie(Locale.GERMAN.toString());

        ResponseEntity<String> responseEntity = template.exchange(homeUrl, HttpMethod.GET, requestEntity, String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertIsHtmlContentType(responseEntity);
        assertLangSetCookieHeader(responseEntity, Locale.ENGLISH.toString());
        assertThat(responseEntity.getBody(), containsString("Spring Boot Demo - Welcome"));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);

        log.debug("getHomeParamAndCookieDifferent: End");
    }
}
