package com.interzonedev.springbootdemo.web.user;

import com.interzonedev.springbootdemo.web.SpringBootDemoMockWebIT;
import com.interzonedev.zankou.dataset.DataSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link UserController#getUsers()} method using a mock web application context.
 */
public class UserControllerGetUsersMockIT extends SpringBootDemoMockWebIT {

    private static final Logger log = LoggerFactory.getLogger(UserControllerGetUsersMockIT.class);

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testGetUsersNonEmpty() throws Exception {
        log.debug("testGetUsersNonEmpty: Start");

        RequestBuilder request = MockMvcRequestBuilders
                .get("/user")
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.users", hasSize(3)))
                .andExpect(jsonPath("$.users..id", hasItems(1, 2, 3)));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);

        log.debug("testGetUsersNonEmpty: End");
    }

    @Test
    @DataSet(filename = "dataset/user/empty.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testGetUsersEmpty() throws Exception {
        log.debug("testGetUsersEmpty: Start");

        RequestBuilder request = MockMvcRequestBuilders
                .get("/user")
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.users", empty()));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/empty.xml",
                "sbd_user", null);

        log.debug("testGetUsersEmpty: End");
    }
}
