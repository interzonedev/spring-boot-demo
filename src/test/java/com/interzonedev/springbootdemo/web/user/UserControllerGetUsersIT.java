package com.interzonedev.springbootdemo.web.user;

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
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;

/**
 * Integration tests for the {@link UserController#getUsers()} method using a "live" web application context.
 */
public class UserControllerGetUsersIT extends SpringBootDemoWebIT {

    private static final Logger log = LoggerFactory.getLogger(UserControllerGetUsersIT.class);

    private String getUsersUrl;

    @PostConstruct
    private void init() {
        getUsersUrl = getFullTestURL("/user");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testGetUsersNonEmpty() throws Exception {
        log.debug("testGetUsersNonEmpty: Start");

        ResponseEntity<String> responseEntity = template.getForEntity(getUsersUrl, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        jsonAsserter.assertThat("$.users", hasSize(3));
        jsonAsserter.assertThat("$.users..id", hasItems(1, 2, 3));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);

        log.debug("testGetUsersNonEmpty: End");
    }

    @Test
    @DataSet(filename = "dataset/user/empty.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testGetUsersEmpty() throws Exception {
        log.debug("testGetUsersEmpty: Start");

        ResponseEntity<String> responseEntity = template.getForEntity(getUsersUrl, String.class);

        JsonAsserter jsonAsserter = with(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertIsJsonContentType(responseEntity);
        jsonAsserter.assertThat("$.users", empty());

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/empty.xml",
                "sbd_user", null);

        log.debug("testGetUsersEmpty: End");
    }
}
