package com.interzonedev.springbootdemo.command.user;

import com.google.common.base.Strings;
import com.interzonedev.blundr.ValidationException;
import com.interzonedev.springbootdemo.SpringBootDemoIT;
import com.interzonedev.springbootdemo.model.User;
import com.interzonedev.zankou.dataset.DataSet;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

/**
 * Integration tests for {@link UpdateUserCommand}.
 */
public class UpdateUserCommandIT extends SpringBootDemoIT {

    private static final Logger log = LoggerFactory.getLogger(UpdateUserCommandIT.class);

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_EMAIL = "testy.testerson@test.com";
    private static final String TEST_FIRST_NAME = "Testy";
    private static final String TEST_LAST_NAME = "Testerson";

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserNullUser() throws Exception {
        log.debug("testUpdateUserNullUser: Start");

        UpdateUserCommand updateUserCommand = (UpdateUserCommand) applicationContext.getBean(
                "springbootdemo.command.updateUserCommand", (User) null);

        try {
            updateUserCommand.execute();
        } catch (ValidationException ve) {
            BindingResult errors = ve.getErrors();
            Assert.assertFalse(errors.hasFieldErrors());
            Assert.assertEquals(1, errors.getGlobalErrorCount());
            Assert.assertEquals("user", errors.getGlobalError().getObjectName());
        }

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserNullUser: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserNullFirstName() throws Exception {
        log.debug("testUpdateUserNullFirstName: Start");

        long now = System.currentTimeMillis();

        User userBeforeUpdate = getTestUser(TEST_USER_ID);
        userBeforeUpdate.setFirstName(null);
        userBeforeUpdate.setLastName(TEST_LAST_NAME);
        userBeforeUpdate.setEmail(TEST_EMAIL);

        UpdateUserCommand updateUserCommand = (UpdateUserCommand) applicationContext.getBean(
                "springbootdemo.command.updateUserCommand", userBeforeUpdate);

        UserResponse userResponse = updateUserCommand.execute();
        User userAfterUpdate = userResponse.getUser();

        assertSuccessfulUpdate(userBeforeUpdate, userResponse, now);
        Assert.assertNull(userAfterUpdate.getFirstName());
        Assert.assertEquals(TEST_LAST_NAME, userAfterUpdate.getLastName());
        Assert.assertEquals(TEST_EMAIL, userAfterUpdate.getEmail());

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
        userBeforeUpdate.setFirstName(" ");
        userBeforeUpdate.setLastName(TEST_LAST_NAME);
        userBeforeUpdate.setEmail(TEST_EMAIL);

        UpdateUserCommand updateUserCommand = (UpdateUserCommand) applicationContext.getBean(
                "springbootdemo.command.updateUserCommand", userBeforeUpdate);

        UserResponse userResponse = updateUserCommand.execute();
        User userAfterUpdate = userResponse.getUser();

        assertSuccessfulUpdate(userBeforeUpdate, userResponse, now);
        Assert.assertNull(userAfterUpdate.getFirstName());
        Assert.assertEquals(TEST_LAST_NAME, userAfterUpdate.getLastName());
        Assert.assertEquals(TEST_EMAIL, userAfterUpdate.getEmail());

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterNullFirstNameUpdate.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserBlankFirstName: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserFirstNameTooLong() throws Exception {
        log.debug("testUpdateUserFirstNameTooLong: Start");

        User userBeforeUpdate = getTestUser(TEST_USER_ID);
        userBeforeUpdate.setFirstName(Strings.repeat("a", 256));
        userBeforeUpdate.setLastName(TEST_LAST_NAME);
        userBeforeUpdate.setEmail(TEST_EMAIL);

        UpdateUserCommand updateUserCommand = (UpdateUserCommand) applicationContext.getBean(
                "springbootdemo.command.updateUserCommand", userBeforeUpdate);

        try {
            updateUserCommand.execute();
        } catch (ValidationException ve) {
            BindingResult errors = ve.getErrors();
            String errorPropertyName = getSinglePropertyNameFromErrors(errors);
            Assert.assertFalse(errors.hasGlobalErrors());
            Assert.assertEquals(1, errors.getFieldErrorCount());
            Assert.assertEquals("firstName", errorPropertyName);
        }

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
        userBeforeUpdate.setFirstName(TEST_FIRST_NAME);
        userBeforeUpdate.setLastName(null);
        userBeforeUpdate.setEmail(TEST_EMAIL);

        UpdateUserCommand updateUserCommand = (UpdateUserCommand) applicationContext.getBean(
                "springbootdemo.command.updateUserCommand", userBeforeUpdate);

        UserResponse userResponse = updateUserCommand.execute();
        User userAfterUpdate = userResponse.getUser();

        assertSuccessfulUpdate(userBeforeUpdate, userResponse, now);
        Assert.assertEquals(TEST_FIRST_NAME, userAfterUpdate.getFirstName());
        Assert.assertNull(userAfterUpdate.getLastName());
        Assert.assertEquals(TEST_EMAIL, userAfterUpdate.getEmail());

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
        userBeforeUpdate.setFirstName(TEST_FIRST_NAME);
        userBeforeUpdate.setLastName(" ");
        userBeforeUpdate.setEmail(TEST_EMAIL);

        UpdateUserCommand updateUserCommand = (UpdateUserCommand) applicationContext.getBean(
                "springbootdemo.command.updateUserCommand", userBeforeUpdate);

        UserResponse userResponse = updateUserCommand.execute();
        User userAfterUpdate = userResponse.getUser();

        assertSuccessfulUpdate(userBeforeUpdate, userResponse, now);
        Assert.assertEquals(TEST_FIRST_NAME, userAfterUpdate.getFirstName());
        Assert.assertNull(userAfterUpdate.getLastName());
        Assert.assertEquals(TEST_EMAIL, userAfterUpdate.getEmail());

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterNullLastNameUpdate.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserBlankLastName: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserLastNameTooLong() throws Exception {
        log.debug("testUpdateUserLastNameTooLong: Start");

        User userBeforeUpdate = getTestUser(TEST_USER_ID);
        userBeforeUpdate.setFirstName(TEST_FIRST_NAME);
        userBeforeUpdate.setLastName(Strings.repeat("a", 256));
        userBeforeUpdate.setEmail(TEST_EMAIL);

        UpdateUserCommand updateUserCommand = (UpdateUserCommand) applicationContext.getBean(
                "springbootdemo.command.updateUserCommand", userBeforeUpdate);

        try {
            updateUserCommand.execute();
        } catch (ValidationException ve) {
            BindingResult errors = ve.getErrors();
            String errorPropertyName = getSinglePropertyNameFromErrors(errors);
            Assert.assertFalse(errors.hasGlobalErrors());
            Assert.assertEquals(1, errors.getFieldErrorCount());
            Assert.assertEquals("lastName", errorPropertyName);
        }

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/before.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserLastNameTooLong: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserNullEmail() throws Exception {
        log.debug("testUpdateUserNullEmail: Start");

        User userBeforeUpdate = getTestUser(TEST_USER_ID);
        userBeforeUpdate.setFirstName(TEST_FIRST_NAME);
        userBeforeUpdate.setLastName(TEST_LAST_NAME);
        userBeforeUpdate.setEmail(null);

        UpdateUserCommand updateUserCommand = (UpdateUserCommand) applicationContext.getBean(
                "springbootdemo.command.updateUserCommand", userBeforeUpdate);

        try {
            updateUserCommand.execute();
        } catch (ValidationException ve) {
            BindingResult errors = ve.getErrors();
            String errorPropertyName = getSinglePropertyNameFromErrors(errors);
            Assert.assertFalse(errors.hasGlobalErrors());
            Assert.assertEquals(1, errors.getFieldErrorCount());
            Assert.assertEquals("email", errorPropertyName);
        }

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserNullEmail: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserBlankEmail() throws Exception {
        log.debug("testUpdateUserBlankEmail: Start");

        User userBeforeUpdate = getTestUser(TEST_USER_ID);
        userBeforeUpdate.setFirstName(TEST_FIRST_NAME);
        userBeforeUpdate.setLastName(TEST_LAST_NAME);
        userBeforeUpdate.setEmail(" ");

        UpdateUserCommand updateUserCommand = (UpdateUserCommand) applicationContext.getBean(
                "springbootdemo.command.updateUserCommand", userBeforeUpdate);

        try {
            updateUserCommand.execute();
        } catch (ValidationException ve) {
            BindingResult errors = ve.getErrors();
            String errorPropertyName = getSinglePropertyNameFromErrors(errors);
            Assert.assertFalse(errors.hasGlobalErrors());
            Assert.assertEquals(1, errors.getFieldErrorCount());
            Assert.assertEquals("email", errorPropertyName);
        }

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserBlankEmail: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserEmailTooLong() throws Exception {
        log.debug("testUpdateUserEmailTooLong: Start");

        User userBeforeUpdate = getTestUser(TEST_USER_ID);
        userBeforeUpdate.setFirstName(TEST_FIRST_NAME);
        userBeforeUpdate.setLastName(TEST_LAST_NAME);
        userBeforeUpdate.setEmail(Strings.repeat("a", 256));

        UpdateUserCommand updateUserCommand = (UpdateUserCommand) applicationContext.getBean(
                "springbootdemo.command.updateUserCommand", userBeforeUpdate);

        try {
            updateUserCommand.execute();
        } catch (ValidationException ve) {
            BindingResult errors = ve.getErrors();
            String errorPropertyName = getSinglePropertyNameFromErrors(errors);
            Assert.assertFalse(errors.hasGlobalErrors());
            Assert.assertEquals(1, errors.getFieldErrorCount());
            Assert.assertEquals("email", errorPropertyName);
        }

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testUpdateUserEmailTooLong: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testUpdateUserDuplicateEmail() throws Exception {
        log.debug("testUpdateUserDuplicateEmail: Start");

        User userBeforeUpdate = getTestUser(TEST_USER_ID);
        userBeforeUpdate.setFirstName(TEST_FIRST_NAME);
        userBeforeUpdate.setLastName(TEST_LAST_NAME);
        userBeforeUpdate.setEmail("uncle@fester.com");

        UpdateUserCommand updateUserCommand = (UpdateUserCommand) applicationContext.getBean(
                "springbootdemo.command.updateUserCommand", userBeforeUpdate);

        try {
            updateUserCommand.execute();
        } catch (ValidationException ve) {
            BindingResult errors = ve.getErrors();
            Assert.assertFalse(errors.hasFieldErrors());
            Assert.assertEquals(1, errors.getGlobalErrorCount());
            Assert.assertEquals("user", errors.getGlobalError().getObjectName());
        }

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
        userBeforeUpdate.setFirstName(TEST_FIRST_NAME);
        userBeforeUpdate.setLastName(TEST_LAST_NAME);
        userBeforeUpdate.setEmail(TEST_EMAIL);

        UpdateUserCommand updateUserCommand = (UpdateUserCommand) applicationContext.getBean(
                "springbootdemo.command.updateUserCommand", userBeforeUpdate);

        UserResponse userResponse = updateUserCommand.execute();
        User userAfterUpdate = userResponse.getUser();

        assertSuccessfulUpdate(userBeforeUpdate, userResponse, now);
        Assert.assertEquals(TEST_FIRST_NAME, userAfterUpdate.getFirstName());
        Assert.assertEquals(TEST_LAST_NAME, userAfterUpdate.getLastName());
        Assert.assertEquals(TEST_EMAIL, userAfterUpdate.getEmail());

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterUpdate.xml", "sbd_user", TIME_UPDATED_COLUMN_NAME);

        log.debug("testUpdateUserValid: End");
    }

    private void assertSuccessfulUpdate(User userBeforeUpdate, UserResponse userResponse, long now) {
        User userAfterUpdate = userResponse.getUser();
        Assert.assertNull(userResponse.getValidationError());
        Assert.assertNull(userResponse.getProcessingError());
        Assert.assertEquals(userBeforeUpdate.getId(), userAfterUpdate.getId());
        Assert.assertEquals(userBeforeUpdate.getTimeCreated(), userAfterUpdate.getTimeCreated());
        Assert.assertTrue(userAfterUpdate.getTimeUpdated().getTime() >= now);
    }
}
