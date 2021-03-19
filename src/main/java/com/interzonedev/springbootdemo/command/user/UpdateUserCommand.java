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
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import java.util.Optional;

/**
 * {@link IZCommand} implementation that updates an existing {@link User}.
 */
@Named("springbootdemo.command.updateUserCommand")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UpdateUserCommand implements IZCommand<UserResponse> {

    private static final Logger log = LoggerFactory.getLogger(UpdateUserCommand.class);

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

    public UpdateUserCommand(User userTemplate) {
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

            validateUserTemplate(nullifiedUserTemplate);

            User updatedUser = transactionTemplate.execute(status -> {
                // Don't attempt to upsert the User template used for the duplicate user validation.
                entityManager.setFlushMode(FlushModeType.COMMIT);

                // Get the current User from the database.
                User userToUpdate = userRepository.findById(nullifiedUserTemplate.getId()).orElse(null);
                if (null == userToUpdate) {
                    throw new ValidationException(User.MODEL_NAME, "The user to update doesn't exist");
                }

                userToUpdate.setFirstName(nullifiedUserTemplate.getFirstName());
                userToUpdate.setLastName(nullifiedUserTemplate.getLastName());
                userToUpdate.setEmail(nullifiedUserTemplate.getEmail());

                validateUser(userToUpdate);

                return userRepository.save(userToUpdate);
            });

            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    entityManager.refresh(updatedUser);
                }
            });

            return new UserResponse(null, null, UserAction.UPDATE, updatedUser);
        } finally {
            log.debug("execute: End");
        }
    }

    /**
     * Validates the specified {@link User} meant to be used as the template for the update.
     *
     * @param user The {@link User} to validate.
     * @throws ValidationException Thrown if the specified {@link User} is null or {@link User#getId()} returns null.
     */
    private void validateUserTemplate(User user) {
        if (null == user) {
            throw new ValidationException(User.MODEL_NAME, "The user must be set");
        }

        if (null == user.getId()) {
            throw new ValidationException(User.MODEL_NAME, "The user ID must be set");
        }
    }

    /**
     * Validates the specified {@link User}. If creating the ID is allowed to be null.
     *
     * @param user The {@link User} to validate.
     * @throws ValidationException Thrown if the specified {@link User} is invalid.
     */
    private void validateUser(User user) {
        validateUserTemplate(user);

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
        if (userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(user.getId())) {
            throw new ValidationException(User.MODEL_NAME, "A user with the email address " + user.getEmail()
                    + " already exists.");
        }
    }
}
