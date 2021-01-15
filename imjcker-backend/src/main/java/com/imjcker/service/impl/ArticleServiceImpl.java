package com.imjcker.service.impl;

import java.util.Date;
import java.util.List;

import com.imjcker.domain.Article;
import com.imjcker.repository.ArticleRepository;
import com.imjcker.service.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by <a href='https://github.com/imjcker'>Alan Turing</a>
 * on 2018-03-05 at 10:41 PM
 *
 * @version 1.0
 */
@Slf4j
@Service("articleService")
public class ArticleServiceImpl implements ArticleService {
    private final
    ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }


    public Article save(Article article) {
        String title = article.getContent().substring(2, article.getContent().indexOf("\n"));
        log.info("article:{}", title);
        article.setTitle(title);
        article.setCreateDate(new Date().toString());
        return articleRepository.save(article);
    }

    @Override
    public Article getArticleById(String id) throws Exception {
        return articleRepository.findOne(id);
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAllByOrderByCreateDateDesc();
    }

    @Override
    public List<Article> search(String title) {
        return articleRepository.findArticlesByTitleLike(title);
    }

    public void delete(String id) {
        articleRepository.delete(id);
    }

}
