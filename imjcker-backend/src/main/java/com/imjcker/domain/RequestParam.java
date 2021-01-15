package com.imjcker.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestParam {
    @Id
    @GeneratedValue
    private int id;
    private int apiId;
    private String type;
    private String location;
    private String must;
    private String defaultValue;
    private String memo;
}
