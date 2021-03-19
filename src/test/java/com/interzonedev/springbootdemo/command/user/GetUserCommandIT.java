package com.interzonedev.springbootdemo.command.user;

import com.interzonedev.blundr.ValidationException;
import com.interzonedev.springbootdemo.SpringBootDemoIT;
import com.interzonedev.springbootdemo.model.User;
import com.interzonedev.zankou.dataset.DataSet;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Integration tests for {@link GetUserCommand}.
 */
public class GetUserCommandIT extends SpringBootDemoIT {

    private static final Logger log = LoggerFactory.getLogger(GetUserCommandIT.class);

    @Test(expected = ValidationException.class)
    public void testIdNull() throws Exception {
        log.debug("testIdNull: Start");

        GetUserCommand getUserCommand = (GetUserCommand) applicationContext.getBean(
                "springbootdemo.command.getUserCommand", (Long) null);

        getUserCommand.execute();
    }

    @Test(expected = ValidationException.class)
    public void testIdNotPositive() throws Exception {
        log.debug("testIdNotPositive: Start");

        GetUserCommand getUserCommand = (GetUserCommand) applicationContext.getBean(
                "springbootdemo.command.getUserCommand", 0L);

        getUserCommand.execute();
    }

    @Test
    public void testIdNonExistent() throws Exception {
        log.debug("testIdNonExistent: Start");

        GetUserCommand getUserCommand = (GetUserCommand) applicationContext.getBean(
                "springbootdemo.command.getUserCommand", 100L);

        UserResponse userResponse = getUserCommand.execute();

        Assert.assertNull(userResponse.getValidationError());
        Assert.assertNull(userResponse.getProcessingError());
        Assert.assertNull(userResponse.getUser());

        log.debug("testIdNonExistent: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testIdValid() throws Exception {
        log.debug("testIdValid: Start");

        Long userId = 1L;

        GetUserCommand getUserCommand = (GetUserCommand) applicationContext.getBean(
                "springbootdemo.command.getUserCommand", userId);

        UserResponse userResponse = getUserCommand.execute();
        User user = userResponse.getUser();

        Assert.assertNull(userResponse.getValidationError());
        Assert.assertNull(userResponse.getProcessingError());
        Assert.assertNotNull(user);
        Assert.assertEquals(userId, user.getId());

        log.debug("testIdValid: End");
    }
}
