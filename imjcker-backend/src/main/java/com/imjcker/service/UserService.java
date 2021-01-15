package com.imjcker.service;

import com.imjcker.domain.User;

public interface UserService {

    User save(String code, String state);

    User getUser(String login);

    User getUserByName(String login);
}
