package com.interzonedev.springbootdemo.command.user;

import com.interzonedev.springbootdemo.SpringBootDemoIT;
import com.interzonedev.springbootdemo.model.User;
import com.interzonedev.zankou.dataset.DataSet;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.Optional;

/**
 * Integration tests for {@link UserUtils}.
 */
public class UserUtilsTest extends SpringBootDemoIT {

    private static final Logger log = LoggerFactory.getLogger(UserUtilsTest.class);

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_FIRST_NAME = "Testy";
    private static final String TEST_LAST_NAME = "Testerson";
    private static final String TEST_EMAIL = "testy.testerson@test.com";
    private static final Date TEST_TIME_CREATED = new Date(System.currentTimeMillis() - 20000L);
    private static final Date TEST_TIME_UPDATED = new Date(System.currentTimeMillis() - 10000L);

    @Inject
    @Named("springbootdemo.command.userUtils")
    private UserUtils userUtils;

    @Test(expected = IllegalArgumentException.class)
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testGetUserWithEmailNullEmail() {
        log.debug("testGetUserWithEmailNullEmail: Start");

        userUtils.getUserWithEmail(null);
    }

    @Test(expected = IllegalArgumentException.class)
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testGetUserWithEmailEmptyEmail() {
        log.debug("testGetUserWithEmailEmptyEmail: Start");

        userUtils.getUserWithEmail("");
    }

    @Test(expected = IllegalArgumentException.class)
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testGetUserWithEmailBlankEmail() {
        log.debug("testGetUserWithEmailBlankEmail: Start");

        userUtils.getUserWithEmail(" ");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testGetUserWithEmailNonExistent() {
        log.debug("testGetUserWithEmailNonExistent: Start");

        Optional<User> user = userUtils.getUserWithEmail("foo@bar.com");

        Assert.assertFalse(user.isPresent());

        log.debug("testGetUserWithEmailNonExistent: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testGetUserWithEmailUnique() {
        log.debug("testGetUserWithEmailUnique: Start");

        String email = "gern@blanston.com";

        Optional<User> user = userUtils.getUserWithEmail(email);

        Assert.assertEquals(email, user.get().getEmail());

        log.debug("testGetUserWithEmailUnique: End");
    }

    @Test
    public void testGetNullifiedUserNullUser() {
        log.debug("testGetNullifiedUserNullUser: Start");

        User user = userUtils.getNullifiedUser(null);

        Assert.assertNull(user);

        log.debug("testGetNullifiedUserNullUser: End");
    }

    @Test
    public void testGetNullifiedUserAllPropertiesSet() {
        log.debug("testGetNullifiedUserAllPropertiesSet: Start");

        User userIn = new User();
        userIn.setId(TEST_USER_ID);
        userIn.setFirstName(TEST_FIRST_NAME);
        userIn.setLastName(TEST_LAST_NAME);
        userIn.setEmail(TEST_EMAIL);
        userIn.setTimeCreated(TEST_TIME_CREATED);
        userIn.setTimeUpdated(TEST_TIME_UPDATED);

        User user = userUtils.getNullifiedUser(userIn);

        Assert.assertEquals(TEST_USER_ID, user.getId());
        Assert.assertEquals(TEST_FIRST_NAME, user.getFirstName());
        Assert.assertEquals(TEST_LAST_NAME, user.getLastName());
        Assert.assertEquals(TEST_EMAIL, user.getEmail());
        Assert.assertEquals(TEST_TIME_CREATED, user.getTimeCreated());
        Assert.assertEquals(TEST_TIME_UPDATED, user.getTimeUpdated());

        log.debug("testGetNullifiedUserAllPropertiesSet: End");
    }

    @Test
    public void testGetNullifiedUserAllPropertiesNull() {
        log.debug("testGetNullifiedUserAllPropertiesNull: Start");

        User userIn = new User();
        userIn.setId(null);
        userIn.setFirstName(null);
        userIn.setLastName(null);
        userIn.setEmail(null);
        userIn.setTimeCreated(null);
        userIn.setTimeUpdated(null);

        User user = userUtils.getNullifiedUser(userIn);

        Assert.assertNull(user.getId());
        Assert.assertNull(user.getFirstName());
        Assert.assertNull(user.getLastName());
        Assert.assertNull(user.getEmail());
        Assert.assertNull(user.getTimeCreated());
        Assert.assertNull(user.getTimeUpdated());

        log.debug("testGetNullifiedUserAllPropertiesNull: End");
    }

    @Test
    public void testGetNullifiedUserStringPropertiesNull() {
        log.debug("testGetNullifiedUserStringPropertiesNull: Start");

        User userIn = new User();
        userIn.setId(TEST_USER_ID);
        userIn.setFirstName(null);
        userIn.setLastName(null);
        userIn.setEmail(null);
        userIn.setTimeCreated(TEST_TIME_CREATED);
        userIn.setTimeUpdated(TEST_TIME_UPDATED);

        User user = userUtils.getNullifiedUser(userIn);

        Assert.assertEquals(TEST_USER_ID, user.getId());
        Assert.assertNull(user.getFirstName());
        Assert.assertNull(user.getLastName());
        Assert.assertNull(user.getEmail());
        Assert.assertEquals(TEST_TIME_CREATED, user.getTimeCreated());
        Assert.assertEquals(TEST_TIME_UPDATED, user.getTimeUpdated());

        log.debug("testGetNullifiedUserStringPropertiesNull: End");
    }

    @Test
    public void testGetNullifiedUserStringPropertiesEmpty() {
        log.debug("testGetNullifiedUserStringPropertiesEmpty: Start");

        User userIn = new User();
        userIn.setId(TEST_USER_ID);
        userIn.setFirstName("");
        userIn.setLastName("");
        userIn.setEmail("");
        userIn.setTimeCreated(TEST_TIME_CREATED);
        userIn.setTimeUpdated(TEST_TIME_UPDATED);

        User user = userUtils.getNullifiedUser(userIn);

        Assert.assertEquals(TEST_USER_ID, user.getId());
        Assert.assertNull(user.getFirstName());
        Assert.assertNull(user.getLastName());
        Assert.assertNull(user.getEmail());
        Assert.assertEquals(TEST_TIME_CREATED, user.getTimeCreated());
        Assert.assertEquals(TEST_TIME_UPDATED, user.getTimeUpdated());

        log.debug("testGetNullifiedUserStringPropertiesEmpty: End");
    }

    @Test
    public void testGetNullifiedUserStringPropertiesBlank() {
        log.debug("testGetNullifiedUserStringPropertiesBlank: Start");

        User userIn = new User();
        userIn.setId(TEST_USER_ID);
        userIn.setFirstName(" ");
        userIn.setLastName(" ");
        userIn.setEmail(" ");
        userIn.setTimeCreated(TEST_TIME_CREATED);
        userIn.setTimeUpdated(TEST_TIME_UPDATED);

        User user = userUtils.getNullifiedUser(userIn);

        Assert.assertEquals(TEST_USER_ID, user.getId());
        Assert.assertNull(user.getFirstName());
        Assert.assertNull(user.getLastName());
        Assert.assertNull(user.getEmail());
        Assert.assertEquals(TEST_TIME_CREATED, user.getTimeCreated());
        Assert.assertEquals(TEST_TIME_UPDATED, user.getTimeUpdated());

        log.debug("testGetNullifiedUserStringPropertiesBlank: End");
    }
}
