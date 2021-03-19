package com.interzonedev.springbootdemo.command.user;

import com.interzonedev.blundr.ValidationException;
import com.interzonedev.blundr.ValidationHelper;
import com.interzonedev.commandr.IZCommand;
import com.interzonedev.springbootdemo.model.User;
import com.interzonedev.springbootdemo.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

/**
 * {@link IZCommand} implementation that creates a new {@link User}.
 */
@Named("springbootdemo.command.createUserCommand")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CreateUserCommand implements IZCommand<UserResponse> {

    private static final Logger log = LoggerFactory.getLogger(CreateUserCommand.class);

    @Inject
    @Named("springbootdemo.persistence.userRepository")
    private UserRepository userRepository;

    @Inject
    @Named("springbootdemo.service.jsr303Validator")
    private Validator jsr303Validator;

    @Inject
    @Named("springbootdemo.service.validationHelper")
    private ValidationHelper validationHelper;

    @Inject
    @Named("springbootdemo.command.userUtils")
    private UserUtils userUtils;

    @Inject
    @Named("transactionManager")
    private PlatformTransactionManager transactionManager;

    @PersistenceContext
    private EntityManager entityManager;

    private final TransactionTemplate transactionTemplate;

    private final User userTemplate;

    public CreateUserCommand(User userTemplate) {
        this.transactionTemplate = new TransactionTemplate();
        this.userTemplate = userTemplate;
    }

    @PostConstruct
    public void init() {
        transactionTemplate.setTransactionManager(transactionManager);
    }

    @Override
    public UserResponse execute() throws Exception {
        try {
            log.debug("execute: Start - userTemplate = " + userTemplate);

            User nullifiedUserTemplate = userUtils.getNullifiedUser(userTemplate);

            User createdUser = transactionTemplate.execute(status -> {
                validateUser(nullifiedUserTemplate);
                return userRepository.save(nullifiedUserTemplate);
            });

            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    entityManager.refresh(createdUser);
                }
            });

            return new UserResponse(null, null, UserAction.CREATE, createdUser);
        } finally {
            log.debug("execute: End");
        }
    }

    /**
     * Validates the specified {@link User}. If creating the ID is allowed to be null.
     *
     * @param user The {@link User} to validate.
     * @throws ValidationException Thrown if the specified {@link User} is invalid.
     */
    private void validateUser(User user) {
        if (null == user) {
            throw new ValidationException(User.MODEL_NAME, "The user must be set");
        }

        BindingResult errors = validationHelper.validate(jsr303Validator, user, User.MODEL_NAME);

        if (errors.hasErrors()) {
            throw new ValidationException(errors);
        }

        validateDuplicateUsers(user);
    }

    /**
     * Checks whether or not the specified {@link User} violates any uniqueness constraints.
     *
     * @param user The {@link User} to validate.
     * @throws ValidationException Thrown if the specified {@link User} violates any uniqueness constraints.
     */
    private void validateDuplicateUsers(User user) {
        Optional<User> userWithSameEmail = userUtils.getUserWithEmail(user.getEmail());
        if (userWithSameEmail.isPresent()) {
            throw new ValidationException(User.MODEL_NAME, "A user with the email address " + user.getEmail()
                    + " already exists.");
        }
    }
}
