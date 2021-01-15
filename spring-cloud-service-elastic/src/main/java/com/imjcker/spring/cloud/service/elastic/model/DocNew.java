package com.imjcker.spring.cloud.service.elastic.model;

import lombok.Data;

@Data
public class DocNew {
    private String uuid;

    private String name;
    private String gender;
//    private String birth;
    private String address;
    private String state;
    private String city;
    private String zip;
    private String country;
    private String phone;
    private String username;
    private String password;
    private String email;
    private String avatar;

    private Boolean married;
}
