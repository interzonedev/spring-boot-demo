package com.interzonedev.springbootdemo.command.user;

import com.interzonedev.commandr.IZCommand;
import com.interzonedev.springbootdemo.model.User;
import com.interzonedev.springbootdemo.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * {@link IZCommand} implementation that gets all {@link User}s.
 */
@Named("springbootdemo.command.getUsersCommand")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetUsersCommand implements IZCommand<UserResponse> {
    private static final Logger log = LoggerFactory.getLogger(GetUsersCommand.class);

    @Inject
    @Named("springbootdemo.persistence.userRepository")
    private UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserResponse execute() throws Exception {
        try {
            log.debug("execute: Start");

            List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

            return new UserResponse(null, null, UserAction.GET_ALL, users);
        } finally {
            log.debug("execute: End");
        }
    }
}
