package com.interzonedev.springbootdemo.model;


import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Database entity model for users.
 */
@Entity
@Table(name = "sbd_user")
public class User {
    public static final String MODEL_NAME = "user";

    @Column(name = "sbd_user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "sbd_user_seq", allocationSize = 1)
    private Long id;

    @Column(name = "first_name")
    @Length(max = 255)
    private String firstName;

    @Column(name = "last_name")
    @Length(max = 255)
    private String lastName;

    @Column(name = "email")
    @NotBlank
    @Length(max = 255)
    private String email;

    @Column(name = "time_created", insertable = false, updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCreated;

    @Column(name = "time_updated", insertable = false, updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeUpdated;

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

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Date getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(Date timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof User)) {
            return false;
        }

        User that = (User) obj;

        return Objects.equal(id, that.id)
                && Objects.equal(firstName, that.firstName)
                && Objects.equal(lastName, that.lastName)
                && Objects.equal(email, that.email)
                && Objects.equal(timeCreated, that.timeCreated)
                && Objects.equal(timeUpdated, that.timeUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                id,
                firstName,
                lastName,
                email,
                timeCreated,
                timeUpdated
        );
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
