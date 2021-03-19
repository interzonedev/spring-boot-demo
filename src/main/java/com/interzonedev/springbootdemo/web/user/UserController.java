package com.interzonedev.springbootdemo.web.user;

import com.interzonedev.blundr.ValidationHelper;
import com.interzonedev.commandr.http.IZCommandResult;
import com.interzonedev.respondr.response.HttpResponse;
import com.interzonedev.respondr.serialize.Serializer;
import com.interzonedev.springbootdemo.command.user.CreateUserCommand;
import com.interzonedev.springbootdemo.command.user.GetUserCommand;
import com.interzonedev.springbootdemo.command.user.GetUsersCommand;
import com.interzonedev.springbootdemo.command.user.UpdateUserCommand;
import com.interzonedev.springbootdemo.command.user.UserResponse;
import com.interzonedev.springbootdemo.model.User;
import com.interzonedev.springbootdemo.web.SpringBootDemoController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import java.util.Locale;

/**
 * RESTful web controller that handles requests for performing basic CRUD operations on the {@link User} model.
 *
 * TODO - Add method level comments.
 */
@RestController(value = "springbootdemo.web.userController")
@RequestMapping("/user")
public class UserController extends SpringBootDemoController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserCommandExecutor userCommandExecutor;

    @Inject
    public UserController(ApplicationContext applicationContext,
                          @Named("springbootdemo.web.jsonSerializer") Serializer serializer,
                          @Named("springbootdemo.service.validationHelper") ValidationHelper validationHelper,
                          @Named("springbootdemo.web.messageSource") MessageSource messageSource,
                          @Named("springbootdemo.web.userCommandExecutor") UserCommandExecutor userCommandExecutor) {
        super(applicationContext, serializer, validationHelper, messageSource);
        this.userCommandExecutor = userCommandExecutor;
    }

    @GetMapping
    public ResponseEntity<String> getUsers() {
        try {
            log.debug("getUsers: Start");

            GetUsersCommand getUsersCommand = (GetUsersCommand) applicationContext.getBean(
                    "springbootdemo.command.getUsersCommand");

            return userCommandExecutor
                    .execute(getUsersCommand, getLocale())
                    .getResponseEntity();
        } finally {
            log.debug("getUsers: End");
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<String> getUser(@PathVariable("id") Long id) {
        try {
            log.debug("getUser: Start - id = " + id);

            GetUserCommand getUserCommand = (GetUserCommand) applicationContext.getBean(
                    "springbootdemo.command.getUserCommand", id);

            IZCommandResult izCommandExecutorResponse = userCommandExecutor.execute(getUserCommand, getLocale());
            ResponseEntity<String> responseEntity = izCommandExecutorResponse.getResponseEntity();
            UserResponse userResponse = (UserResponse) izCommandExecutorResponse.getIzCommandResponse();

            if ((null == userResponse) || (null != userResponse.getUser())) {
                return responseEntity;
            } else {
                log.debug("getUser: Could not find user with id " + id);
                return HttpResponse
                        .newBuilder()
                        .setHttpStatus(HttpStatus.NOT_FOUND)
                        .setContentType(HttpResponse.JSON_CONTENT_TYPE)
                        .setProcessingError("No user with id " + id)
                        .build()
                        .toResponseEntity(serializer);
            }
        } finally {
            log.debug("getUser: End");
        }
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Valid UserForm userForm, BindingResult bindingResult) {
        try {
            log.debug("createUser: Start - userForm = " + userForm);

            Locale locale = getLocale();

            if (bindingResult.hasErrors()) {
                log.debug("createUser: Form has errors");
                return HttpResponse.getValidationErrorJsonResponse(bindingResult, messageSource, locale, serializer);
            }

            User userIn = new User();
            userIn.setFirstName(StringUtils.stripToNull(userForm.getFirstName()));
            userIn.setLastName(StringUtils.stripToNull(userForm.getLastName()));
            userIn.setEmail(StringUtils.stripToNull(userForm.getEmail()));

            CreateUserCommand createUserCommand = (CreateUserCommand) applicationContext.getBean(
                    "springbootdemo.command.createUserCommand", userIn);

            return userCommandExecutor
                    .execute(createUserCommand, getLocale())
                    .getResponseEntity();
        } finally {
            log.debug("createUser: End");
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") Long id, @Valid UserForm userForm,
                                             BindingResult bindingResult) {
        try {
            log.debug("updateUser: Start - userForm = " + userForm);

            Locale locale = getLocale();

            if (bindingResult.hasErrors()) {
                log.debug("updateUser: Form has errors");
                return HttpResponse.getValidationErrorJsonResponse(bindingResult, messageSource, locale, serializer);
            }

            User userIn = new User();
            userIn.setId(id);
            userIn.setFirstName(StringUtils.stripToNull(userForm.getFirstName()));
            userIn.setLastName(StringUtils.stripToNull(userForm.getLastName()));
            userIn.setEmail(StringUtils.stripToNull(userForm.getEmail()));

            UpdateUserCommand updateUserCommand = (UpdateUserCommand) applicationContext.getBean(
                    "springbootdemo.command.updateUserCommand", userIn);

            return userCommandExecutor
                    .execute(updateUserCommand, getLocale())
                    .getResponseEntity();
        } finally {
            log.debug("updateUser: End");
        }
    }
}
