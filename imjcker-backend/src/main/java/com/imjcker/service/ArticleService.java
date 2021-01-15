package com.imjcker.service;

import java.util.List;

import com.imjcker.domain.Article;

/**
 * Created by <a href='https://github.com/imjcker'>Alan Turing</a>
 * on 2018-03-05 at 10:39 PM
 *
 * @version 1.0
 */
public interface ArticleService {
    Article save(Article article);

    Article getArticleById(String id) throws Exception;

    List<Article> findAll();

    List<Article> search(String title);

    void delete(String article);
}
