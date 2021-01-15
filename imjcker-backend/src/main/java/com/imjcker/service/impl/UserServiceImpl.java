package com.imjcker.service.impl;

import com.alibaba.fastjson.JSON;
import com.imjcker.config.OauthConfigProperties;
import com.imjcker.domain.User;
import com.imjcker.repository.UserRepository;
import com.imjcker.service.UserService;
import com.imjcker.common.http.HttpClientResult;
import com.imjcker.common.http.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private OauthConfigProperties oauthConfigProperties;

    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(OauthConfigProperties oauthConfigProperties, UserRepository userRepository) {
        this.oauthConfigProperties = oauthConfigProperties;
        this.userRepository = userRepository;
    }


    @Override
    public User save(String code, String state) {
        Assert.notNull(code, "code can not be null");

        try {
            HttpClientResult token_result = HttpClientUtils.doGet(
                    "https://github.com/login/oauth/access_token?code=" + code +
                            "&client_id=" + oauthConfigProperties.getClientId() +
                            "&client_secret=" + oauthConfigProperties.getClientSecret() +
                            "&state=" + state
            );
            String access_token = token_result.getContent();
            log.info("access_token: {}", access_token);
            HttpClientResult result = HttpClientUtils.doGet("https://api.github.com/user?" + access_token.substring(0, access_token.indexOf("&")));
            User user = JSON.parseObject(result.getContent(), User.class);
            return userRepository.save(user);
        } catch (Exception e) {
            log.error("save user error: {}", e.getMessage());
        }
        return null;
    }

    @Override
    @Cacheable(cacheNames = {"user"}, condition = "#result != null ")
    public User getUser(String login) {
        return userRepository.getUserByLogin(login);
    }

    @Cacheable(cacheNames = "github_user", key = "#login")
    @Override
    public User getUserByName(String login) {
        return userRepository.getUserByLogin(login);
    }
}
