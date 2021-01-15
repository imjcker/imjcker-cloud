package com.imjcker.repository;

import com.imjcker.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by <a href='https://github.com/imjcker'>Alan Turing</a>
 * on 2018-02-25 at 3:16 AM
 *
 * @version 1.0
 */
public interface ArticleRepository extends CrudRepository<Article, String>, JpaRepository<Article, String>, JpaSpecificationExecutor<Article> {
    List<Article> findAllByOrderByCreateDateDesc();

    List<Article> findArticlesByTitleLike(String title);

}
