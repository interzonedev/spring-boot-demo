package com.interzonedev.springbootdemo.command.user;

import com.interzonedev.blundr.ValidationException;
import com.interzonedev.commandr.IZCommand;
import com.interzonedev.springbootdemo.model.User;
import com.interzonedev.springbootdemo.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

/**
 * {@link IZCommand} implementation that gets a {@link User} by ID.
 */
@Named("springbootdemo.command.getUserCommand")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetUserCommand implements IZCommand<UserResponse> {

    private static final Logger log = LoggerFactory.getLogger(GetUserCommand.class);

    @Inject
    @Named("springbootdemo.persistence.userRepository")
    private UserRepository userRepository;

    private final Long id;

    public GetUserCommand(Long id) {
        this.id = id;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserResponse execute() throws Exception {
        try {
            log.debug("execute: Start - id = " + id);

            if (null == id) {
                throw new ValidationException(User.MODEL_NAME, "The user ID must be set");
            }

            if (id <= 0L) {
                throw new ValidationException(User.MODEL_NAME, "The user id must be a positive integer");
            }

            Optional<User> user = userRepository.findById(id);
            return new UserResponse(null, null, UserAction.GET, user.orElse(null));
        } finally {
            log.debug("execute: End");
        }
    }
}
