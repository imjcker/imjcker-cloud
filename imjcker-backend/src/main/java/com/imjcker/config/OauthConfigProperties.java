package com.imjcker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("oauth.github")
public class OauthConfigProperties {
    private String user = "imjcker";
    private String state = "state";
    private String clientId = "4722c87ed10d135e4e3c";
    private String clientSecret = "a518592ca4739fa5010f1639e7c2b683d5289bc5";

    private String authorize = "https://github.com/login/oauth/authorize";
    private String redirect_uri = "http://localhost/oauth/github/callback";
}
