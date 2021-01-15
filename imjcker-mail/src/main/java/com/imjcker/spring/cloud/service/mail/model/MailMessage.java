package com.imjcker.spring.cloud.service.mail.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table
public class MailMessage {
    @Id
    @GeneratedValue
    private Integer id;

    private String fromAccount;

    private String toAccount;

    private String text;

    private String subject;
}
