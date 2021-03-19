package com.interzonedev.springbootdemo.command.user;

import com.interzonedev.springbootdemo.SpringBootDemoIT;
import com.interzonedev.springbootdemo.model.User;
import com.interzonedev.zankou.dataset.DataSet;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Integration tests for {@link GetUsersCommand}.
 */
public class GetUsersCommandIT extends SpringBootDemoIT {

    private static final Logger log = LoggerFactory.getLogger(GetUsersCommandIT.class);

    @Test
    @DataSet(filename = "dataset/user/empty.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testGetAllUsersEmpty() throws Exception {
        log.debug("testGetAllUsersEmpty: Start");

        GetUsersCommand getUsersCommand = (GetUsersCommand) applicationContext.getBean(
                "springbootdemo.command.getUsersCommand");

        UserResponse userResponse = getUsersCommand.execute();
        List<User> users = userResponse.getUsers();

        Assert.assertNull(userResponse.getValidationError());
        Assert.assertNull(userResponse.getProcessingError());
        Assert.assertTrue(users.isEmpty());

        log.debug("testGetAllUsersEmpty: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testGetAllUsersNonEmpty() throws Exception {
        log.debug("testGetAllUsersNonEmpty: Start");

        GetUsersCommand getUsersCommand = (GetUsersCommand) applicationContext.getBean(
                "springbootdemo.command.getUsersCommand");

        UserResponse userResponse = getUsersCommand.execute();
        List<User> users = userResponse.getUsers();

        Assert.assertNull(userResponse.getValidationError());
        Assert.assertNull(userResponse.getProcessingError());
        Assert.assertEquals(3, users.size());
        Assert.assertEquals(Arrays.asList(1L, 2L, 3L), users.stream().map(User::getId).collect(Collectors.toList()));

        log.debug("testGetAllUsersNonEmpty: End");
    }
}
