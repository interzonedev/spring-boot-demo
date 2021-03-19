package com.interzonedev.springbootdemo.web.user;

import com.google.common.collect.ImmutableMap;
import com.interzonedev.blundr.ValidationHelper;
import com.interzonedev.commandr.IZCommandResponse;
import com.interzonedev.commandr.http.IZCommandExecutor;
import com.interzonedev.respondr.serialize.Serializer;
import com.interzonedev.springbootdemo.command.user.UserAction;
import com.interzonedev.springbootdemo.command.user.UserResponse;
import com.interzonedev.springbootdemo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link IZCommandExecutor} implementation specific to commands performed on the {@link User} model.
 */
@Named("springbootdemo.web.userCommandExecutor")
public class UserCommandExecutor extends IZCommandExecutor {

    private static final Logger log = LoggerFactory.getLogger(UserCommandExecutor.class);

    @Inject
    public UserCommandExecutor(@Named("springbootdemo.web.jsonSerializer") Serializer serializer,
                               @Named("springbootdemo.service.validationHelper") ValidationHelper validationHelper,
                               @Named("springbootdemo.web.messageSource") MessageSource messageSource) {
        super(serializer, validationHelper, messageSource);
    }

    /**
     * Creates a response map of values for the successful execution of commands performed on the {@link User} model
     * depending on the {@link UserAction} set in the specified response.
     *
     * @param izCommandResponse The {@link UserResponse} returned by commands performed on the {@link User} model.
     *
     * @return Returns a response map of values for the successful execution of commands performed on the {@link User}
     * model.
     */
    @Override
    protected Map<String, Object> onSuccessMap(IZCommandResponse izCommandResponse) {
        log.debug("onSuccessMap: Start");

        UserResponse userResponse = (UserResponse) izCommandResponse;

        Map<String, Object> responseStructure = new HashMap<>();

        UserAction userAction = userResponse.getUserAction();

        switch (userAction) {
            case GET_ALL:
                responseStructure.put("users", userResponse.getUsers());
                break;
            case GET:
            case CREATE:
            case UPDATE:
                responseStructure.put("user", userResponse.getUser());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported user action " + userAction);
        }


        log.debug("onSuccessMap: End");

        return ImmutableMap.copyOf(responseStructure);
    }
}
