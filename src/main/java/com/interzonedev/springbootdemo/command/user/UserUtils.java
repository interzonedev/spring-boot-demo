package com.interzonedev.springbootdemo.command.user;

import com.interzonedev.springbootdemo.model.User;
import com.interzonedev.springbootdemo.persistence.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

/**
 * Utility methods for actions that can be performed with the {@link User} model.
 */
@Named("springbootdemo.command.userUtils")
public class UserUtils {

    private final UserRepository userRepository;

    @Inject
    public UserUtils(@Named("springbootdemo.persistence.userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gets an {@link Optional<User>} that, if it exists, holds the {@link User} with the specified email address.
     *
     * @param email The email address of the {@link User} to find.
     *
     * @return Returns an {@link Optional<User>} that, if it exists, holds the {@link User} with the specified email
     * address.
     */
    public Optional<User> getUserWithEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("The email address must be set");
        }
        User userWithEmailOnly = new User();
        userWithEmailOnly.setEmail(email);
        return userRepository.findOne(Example.of(userWithEmailOnly));
    }

    /**
     * Gets a clone of the specified {@link User} with all {@code String} properties set to {@code null} if they are
     * blank or empty.
     *
     * @param userIn The {@link User} to clone and nullify to fields on.
     * @return Returns a clone of the specified {@link User} with all {@code String} properties set to {@code null} if
     * they are blank or empty.
     */
    public User getNullifiedUser(User userIn) {
        if (null == userIn) {
            return null;
        }

        User userOut = new User();

        userOut.setId(userIn.getId());
        userOut.setFirstName(StringUtils.stripToNull(userIn.getFirstName()));
        userOut.setLastName(StringUtils.stripToNull(userIn.getLastName()));
        userOut.setEmail(StringUtils.stripToNull(userIn.getEmail()));
        userOut.setTimeCreated(userIn.getTimeCreated());
        userOut.setTimeUpdated(userIn.getTimeUpdated());

        return userOut;
    }
}
