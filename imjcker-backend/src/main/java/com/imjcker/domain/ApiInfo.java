package com.imjcker.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class ApiInfo {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String path;
    private String method;
    private String memo;

    private String requestUrl;
    private String requestMethod;
}
