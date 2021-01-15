package com.imjcker.spring.cloud.service.elastic.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "new_index", type = "doc")
public class Doc {
    @Id
    private String uuid;

    private String name;
    private String gender;
//    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-mm-dd")
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
