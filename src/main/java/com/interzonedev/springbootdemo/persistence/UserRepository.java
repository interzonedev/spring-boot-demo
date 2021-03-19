package com.interzonedev.springbootdemo.persistence;

import com.interzonedev.springbootdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.inject.Named;

/**
 * Repository for retrieving and persisting {@link User}s.
 */
@Named("springbootdemo.persistence.userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
}
