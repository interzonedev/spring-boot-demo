package com.interzonedev.springbootdemo.web;

import com.interzonedev.respondr.response.HttpResponse;
import com.interzonedev.springbootdemo.Application;
import com.interzonedev.springbootdemo.SpringBootDemoIT;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Abstract subclass of {@link SpringBootDemoIT} specifically for integration tests that require a "live" web
 * application context.  Provides the base URL the test web application context responds to and injects a
 * {@link TestRestTemplate} instance for use by implementing classes.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public abstract class SpringBootDemoWebIT extends SpringBootDemoIT {

    @LocalServerPort
    private int port;

    @Inject
    protected TestRestTemplate template;

    private String testWebContextURL;

    @PostConstruct
    private void init() {
        testWebContextURL = "http://localhost:" + port;
    }

    protected String getFullTestURL(String path) {
        try {
            return new URL(testWebContextURL + path).toString();
        } catch (MalformedURLException mue) {
            throw new RuntimeException("Error forming full test URL", mue);
        }
    }

    protected void assertIsJsonContentType(ResponseEntity<String> responseEntity) {
        List<String> contentTypeHeaders = responseEntity.getHeaders().get(HttpResponse.CONTENT_TYPE_HEADER_NAME);
        assertEquals(1, contentTypeHeaders.size());
        assertEquals(HttpResponse.JSON_CONTENT_TYPE.toString(), contentTypeHeaders.get(0));
    }

    protected void assertIsHtmlContentType(ResponseEntity<String> responseEntity) {
        List<String> contentTypeHeaders = responseEntity.getHeaders().get(HttpResponse.CONTENT_TYPE_HEADER_NAME);
        assertEquals(1, contentTypeHeaders.size());
        assertEquals(HttpResponse.HTML_CONTENT_TYPE.toString(), contentTypeHeaders.get(0));
    }

    protected void assertNoSetCookieHeader(ResponseEntity<String> responseEntity) {
        List<String> setCookieHeaders = responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertNull(setCookieHeaders);
    }

    protected void assertLangSetCookieHeader(ResponseEntity<String> responseEntity, String lang) {
        List<String> setCookieHeaders = responseEntity.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertEquals(1, setCookieHeaders.size());
        assertThat(setCookieHeaders.get(0), containsString(WebConfiguration.LOCALE_RESOLVER_COOKIE_NAME + "=" + lang));
    }

    protected HttpHeaders getApplicationFormUrlencodedHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return requestHeaders;
    }

    protected HttpEntity getHttpEntityWithLangCookie(String lang) {
        HttpHeaders requestHeaders = new HttpHeaders();
        addLangCookieToHttpHeaders(requestHeaders, lang);
        return new HttpEntity(requestHeaders);
    }

    protected void addLangCookieToHttpHeaders(HttpHeaders requestHeaders, String lang) {
        requestHeaders.add(HttpHeaders.COOKIE, WebConfiguration.LOCALE_RESOLVER_COOKIE_NAME + "=" + lang);
    }
}
