package com.imjcker.repository;

import com.imjcker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String>, JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    User getUserByLogin(String login);

    User findByLogin(String username);

}
