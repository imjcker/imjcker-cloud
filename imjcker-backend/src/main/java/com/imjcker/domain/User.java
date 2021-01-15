package com.imjcker.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
//@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class User extends BaseEntity{
    @Id
/*    @GeneratedValue(generator = "jpa-uuid")
    private String id;*/
    private String login;
    private String html_url;
    private int followers;
    private String avatar_url;

}
