package com.imjcker.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by <a href='https://github.com/imjcker'>Alan Turing</a>
 * on 2018-02-23 at 3:13 PM
 *
 * @version 1.0
 */
@Data
@Entity
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Article {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private String title;
    private String createDate;
    @Column(columnDefinition = "TEXT")
    private String content;
}
