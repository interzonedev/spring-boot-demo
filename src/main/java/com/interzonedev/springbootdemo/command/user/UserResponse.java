package com.interzonedev.springbootdemo.command.user;

import com.google.common.collect.ImmutableList;
import com.interzonedev.blundr.ValidationException;
import com.interzonedev.commandr.DefaultIZCommandResponse;
import com.interzonedev.springbootdemo.model.User;

import java.util.List;

/**
 * Immutable {@link DefaultIZCommandResponse} implementation for responses from commands performed on the {@link User}
 * model.
 */
public class UserResponse extends DefaultIZCommandResponse {
    private final UserAction userAction;

    private final User user;

    private final List<User> users;

    public UserResponse(ValidationException validationError, Exception processingError, UserAction userAction, User user) {
        super(validationError, processingError);
        this.userAction = userAction;
        this.user = user;
        this.users = ImmutableList.of();
    }

    public UserResponse(ValidationException validationError, Exception processingError, UserAction userAction, List<User> users) {
        super(validationError, processingError);
        this.userAction = userAction;
        this.user = null;
        if (null != users) {
            this.users = ImmutableList.copyOf(users);
        } else {
            this.users = ImmutableList.of();
        }
    }

    public UserAction getUserAction() {
        return userAction;
    }

    public User getUser() {
        return user;
    }

    public List<User> getUsers() {
        return users;
    }
}
