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
 * Integration tests for {@link CreateUserCommand}.
 */
public class CreateUserCommandIT extends SpringBootDemoIT {

    private static final Logger log = LoggerFactory.getLogger(CreateUserCommandIT.class);

    private static final String TEST_EMAIL = "testy.testerson@test.com";
    private static final String TEST_FIRST_NAME = "Testy";
    private static final String TEST_LAST_NAME = "Testerson";

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserNullUser() throws Exception {
        log.debug("testCreateUserNullUser: Start");

        CreateUserCommand createUserCommand = (CreateUserCommand) applicationContext.getBean(
                "springbootdemo.command.createUserCommand", (User) null);

        try {
            createUserCommand.execute();
        } catch (ValidationException ve) {
            BindingResult errors = ve.getErrors();
            Assert.assertFalse(errors.hasFieldErrors());
            Assert.assertEquals(1, errors.getGlobalErrorCount());
            Assert.assertEquals("user", errors.getGlobalError().getObjectName());
        }

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserNullUser: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserNullFirstName() throws Exception {
        log.debug("testCreateUserNullFirstName: Start");

        long now = System.currentTimeMillis();

        User userIn = new User();
        userIn.setFirstName(null);
        userIn.setLastName(TEST_LAST_NAME);
        userIn.setEmail(TEST_EMAIL);

        CreateUserCommand createUserCommand = (CreateUserCommand) applicationContext.getBean(
                "springbootdemo.command.createUserCommand", userIn);

        UserResponse userResponse = createUserCommand.execute();
        User user = userResponse.getUser();

        assertSuccessfulCreation(userResponse, now);
        Assert.assertNull(user.getFirstName());
        Assert.assertEquals(TEST_LAST_NAME, user.getLastName());
        Assert.assertEquals(TEST_EMAIL, user.getEmail());

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

        User userIn = new User();
        userIn.setFirstName(" ");
        userIn.setLastName(TEST_LAST_NAME);
        userIn.setEmail(TEST_EMAIL);

        CreateUserCommand createUserCommand = (CreateUserCommand) applicationContext.getBean(
                "springbootdemo.command.createUserCommand", userIn);

        UserResponse userResponse = createUserCommand.execute();
        User user = userResponse.getUser();

        assertSuccessfulCreation(userResponse, now);
        Assert.assertNull(user.getFirstName());
        Assert.assertEquals(TEST_LAST_NAME, user.getLastName());
        Assert.assertEquals(TEST_EMAIL, user.getEmail());

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterNullFirstNameCreate.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserBlankFirstName: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserFirstNameTooLong() throws Exception {
        log.debug("testCreateUserFirstNameTooLong: Start");

        User userIn = new User();
        userIn.setFirstName(Strings.repeat("a", 256));
        userIn.setLastName(TEST_LAST_NAME);
        userIn.setEmail(TEST_EMAIL);

        CreateUserCommand createUserCommand = (CreateUserCommand) applicationContext.getBean(
                "springbootdemo.command.createUserCommand", userIn);

        try {
            createUserCommand.execute();
        } catch (ValidationException ve) {
            BindingResult errors = ve.getErrors();
            String errorPropertyName = getSinglePropertyNameFromErrors(errors);
            Assert.assertFalse(errors.hasGlobalErrors());
            Assert.assertEquals(1, errors.getFieldErrorCount());
            Assert.assertEquals("firstName", errorPropertyName);
        }

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserFirstNameTooLong: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserNullLastName() throws Exception {
        log.debug("testCreateUserNullLastName: Start");

        long now = System.currentTimeMillis();

        User userIn = new User();
        userIn.setFirstName(TEST_FIRST_NAME);
        userIn.setLastName(null);
        userIn.setEmail(TEST_EMAIL);

        CreateUserCommand createUserCommand = (CreateUserCommand) applicationContext.getBean(
                "springbootdemo.command.createUserCommand", userIn);

        UserResponse userResponse = createUserCommand.execute();
        User user = userResponse.getUser();

        assertSuccessfulCreation(userResponse, now);
        Assert.assertEquals(TEST_FIRST_NAME, user.getFirstName());
        Assert.assertNull(user.getLastName());
        Assert.assertEquals(TEST_EMAIL, user.getEmail());

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

        User userIn = new User();
        userIn.setFirstName(TEST_FIRST_NAME);
        userIn.setLastName(" ");
        userIn.setEmail(TEST_EMAIL);

        CreateUserCommand createUserCommand = (CreateUserCommand) applicationContext.getBean(
                "springbootdemo.command.createUserCommand", userIn);

        UserResponse userResponse = createUserCommand.execute();
        User user = userResponse.getUser();

        assertSuccessfulCreation(userResponse, now);
        Assert.assertEquals(TEST_FIRST_NAME, user.getFirstName());
        Assert.assertNull(user.getLastName());
        Assert.assertEquals(TEST_EMAIL, user.getEmail());

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterNullLastNameCreate.xml", "sbd_user",
                USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserBlankLastName: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserLastNameTooLong() throws Exception {
        log.debug("testCreateUserLastNameTooLong: Start");

        User userIn = new User();
        userIn.setFirstName(TEST_FIRST_NAME);
        userIn.setLastName(Strings.repeat("a", 256));
        userIn.setEmail(TEST_EMAIL);

        CreateUserCommand createUserCommand = (CreateUserCommand) applicationContext.getBean(
                "springbootdemo.command.createUserCommand", userIn);

        try {
            createUserCommand.execute();
        } catch (ValidationException ve) {
            BindingResult errors = ve.getErrors();
            String errorPropertyName = getSinglePropertyNameFromErrors(errors);
            Assert.assertFalse(errors.hasGlobalErrors());
            Assert.assertEquals(1, errors.getFieldErrorCount());
            Assert.assertEquals("lastName", errorPropertyName);
        }

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserLastNameTooLong: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserNullEmail() throws Exception {
        log.debug("testCreateUserNullEmail: Start");

        User userIn = new User();
        userIn.setFirstName(TEST_FIRST_NAME);
        userIn.setLastName(TEST_LAST_NAME);
        userIn.setEmail(null);

        CreateUserCommand createUserCommand = (CreateUserCommand) applicationContext.getBean(
                "springbootdemo.command.createUserCommand", userIn);

        try {
            createUserCommand.execute();
        } catch (ValidationException ve) {
            BindingResult errors = ve.getErrors();
            String errorPropertyName = getSinglePropertyNameFromErrors(errors);
            Assert.assertFalse(errors.hasGlobalErrors());
            Assert.assertEquals(1, errors.getFieldErrorCount());
            Assert.assertEquals("email", errorPropertyName);
        }

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserNullEmail: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserBlankEmail() throws Exception {
        log.debug("testCreateUserBlankEmail: Start");

        User userIn = new User();
        userIn.setFirstName(TEST_FIRST_NAME);
        userIn.setLastName(TEST_LAST_NAME);
        userIn.setEmail(" ");

        CreateUserCommand createUserCommand = (CreateUserCommand) applicationContext.getBean(
                "springbootdemo.command.createUserCommand", userIn);

        try {
            createUserCommand.execute();
        } catch (ValidationException ve) {
            BindingResult errors = ve.getErrors();
            String errorPropertyName = getSinglePropertyNameFromErrors(errors);
            Assert.assertFalse(errors.hasGlobalErrors());
            Assert.assertEquals(1, errors.getFieldErrorCount());
            Assert.assertEquals("email", errorPropertyName);
        }

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserBlankEmail: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserEmailTooLong() throws Exception {
        log.debug("testCreateUserEmailTooLong: Start");

        User userIn = new User();
        userIn.setFirstName(TEST_FIRST_NAME);
        userIn.setLastName(TEST_LAST_NAME);
        userIn.setEmail(Strings.repeat("a", 256));

        CreateUserCommand createUserCommand = (CreateUserCommand) applicationContext.getBean(
                "springbootdemo.command.createUserCommand", userIn);

        try {
            createUserCommand.execute();
        } catch (ValidationException ve) {
            BindingResult errors = ve.getErrors();
            String errorPropertyName = getSinglePropertyNameFromErrors(errors);
            Assert.assertFalse(errors.hasGlobalErrors());
            Assert.assertEquals(1, errors.getFieldErrorCount());
            Assert.assertEquals("email", errorPropertyName);
        }

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserEmailTooLong: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserDuplicateEmail() throws Exception {
        log.debug("testCreateUserDuplicateEmail: Start");

        User userIn = new User();
        userIn.setFirstName(TEST_FIRST_NAME);
        userIn.setLastName(TEST_LAST_NAME);
        userIn.setEmail("gern@blanston.com");

        CreateUserCommand createUserCommand = (CreateUserCommand) applicationContext.getBean(
                "springbootdemo.command.createUserCommand", userIn);

        try {
            createUserCommand.execute();
        } catch (ValidationException ve) {
            BindingResult errors = ve.getErrors();
            Assert.assertFalse(errors.hasFieldErrors());
            Assert.assertEquals(1, errors.getGlobalErrorCount());
            Assert.assertEquals("user", errors.getGlobalError().getObjectName());
        }

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserDuplicateEmail: End");
    }

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void testCreateUserValid() throws Exception {
        log.debug("testCreateUserValid: Start");

        long now = System.currentTimeMillis();

        User userIn = new User();
        userIn.setFirstName(TEST_FIRST_NAME);
        userIn.setLastName(TEST_LAST_NAME);
        userIn.setEmail(TEST_EMAIL);

        CreateUserCommand createUserCommand = (CreateUserCommand) applicationContext.getBean(
                "springbootdemo.command.createUserCommand", userIn);

        UserResponse userResponse = createUserCommand.execute();
        User user = userResponse.getUser();

        assertSuccessfulCreation(userResponse, now);
        Assert.assertEquals(TEST_FIRST_NAME, user.getFirstName());
        Assert.assertEquals(TEST_LAST_NAME, user.getLastName());
        Assert.assertEquals(TEST_EMAIL, user.getEmail());

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource,
                "dataset/user/afterCreate.xml", "sbd_user", USER_IGNORE_COLUMN_NAMES);

        log.debug("testCreateUserValid: End");
    }

    private void assertSuccessfulCreation(UserResponse userResponse, long now) {
        User user = userResponse.getUser();
        Assert.assertNull(userResponse.getValidationError());
        Assert.assertNull(userResponse.getProcessingError());
        Assert.assertTrue(user.getId() > 0L);
        Assert.assertTrue(user.getTimeCreated().getTime() >= now);
        Assert.assertTrue(user.getTimeUpdated().getTime() >= now);
    }

}
