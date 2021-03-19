package com.interzonedev.springbootdemo.web.user;

import com.google.common.base.MoreObjects;
import com.interzonedev.springbootdemo.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Web form used to hold parameters for basic CRUD operations on the {@link User} model.
 */
public class UserForm {

    private Long id;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @NotBlank
    @Size(max = 255)
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.toString())
                .add("id", id)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("email", email)
                .toString();
    }
}
