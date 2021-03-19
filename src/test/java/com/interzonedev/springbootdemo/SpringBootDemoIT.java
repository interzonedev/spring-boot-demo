package com.interzonedev.springbootdemo;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.interzonedev.springbootdemo.command.user.GetUserCommand;
import com.interzonedev.springbootdemo.command.user.UserResponse;
import com.interzonedev.springbootdemo.model.User;
import com.interzonedev.zankou.AbstractIntegrationTest;
import com.interzonedev.zankou.dataset.dbunit.DbUnitDataSetTester;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.validation.BindingResult;

import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class for all application integration tests. Initiates the test Spring container and makes common beans and
 * methods for testing available.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = Application.class)
@TestPropertySource(properties = {"DATABASE_URL=postgres://springbootdemouser:springbootdemopass@localhost:5432/springbootdemo_test"})
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles("test")
public abstract class SpringBootDemoIT extends AbstractIntegrationTest {

    /**
     * Column names to ignore in comparing DbUnit datasets for all integration tests.
     */
    protected static final List<String> TIME_UPDATED_COLUMN_NAME = ImmutableList.copyOf(
            new String[]{"time_updated"}
    );

    /**
     * Column names to ignore in comparing DbUnit datasets for all integration tests.
     */
    protected static final List<String> TIMESTAMP_COLUMN_NAMES = ImmutableList.copyOf(Iterables.concat(
            Arrays.asList(new String[]{"time_created"}), TIME_UPDATED_COLUMN_NAME
    ));

    /**
     * Column names to ignore in comparing DbUnit datasets for tests on the sbd_user table.
     */
    protected static final List<String> USER_IGNORE_COLUMN_NAMES = ImmutableList.copyOf(Iterables.concat(
            Arrays.asList(new String[]{"sbd_user_id"}), TIMESTAMP_COLUMN_NAMES
    ));

    @Inject
    protected ApplicationContext applicationContext;

    /**
     * Used in implementing integration tests to insert, read and delete test data in the live schema.
     */
    @Inject
    @Named("springbootdemo.persistence.dataSource")
    protected DataSource dataSource;

    /**
     * Used in implementing integration tests to compare data against the live schema.
     */
    @Inject
    @Named("springbootdemo.test.dbUnitDataSetTester")
    protected DbUnitDataSetTester dbUnitDataSetTester;

    /**
     * Extract the name of the property that is in error from the specified {@link BindingResult}. Looks at the first
     * error only.
     *
     * @param errors The {@link BindingResult} that contains the error that occurred from validating a model.
     * @return Returns the name of the property that is in error from the specified {@link BindingResult}.
     */
    protected String getSinglePropertyNameFromErrors(BindingResult errors) {
        return errors.getFieldError().getField();
    }

    /**
     * Gets the {@link User} with the specified ID.
     *
     * @param userId The ID of the {@link User} to retrieve.
     * @return Returns the {@link User} with the specified ID if it exists, otherwise returns null.
     */
    protected User getTestUser(Long userId) throws Exception {

        GetUserCommand getUserCommand = (GetUserCommand) applicationContext.getBean(
                "springbootdemo.command.getUserCommand", userId);

        UserResponse userResponse = getUserCommand.execute();

        return userResponse.getUser();

    }
}
