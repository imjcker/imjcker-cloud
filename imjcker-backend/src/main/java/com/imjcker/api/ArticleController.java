package com.imjcker.api;

import com.imjcker.domain.Article;
import com.imjcker.domain.JsonResult;
import com.imjcker.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * Created by <a href='https://github.com/imjcker'>Alan Turing</a>
 * on 2017-12-19 at 1:47 PM
 *
 * @version 1.0
 */
@Slf4j
@RestController
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/article/list")
    public JsonResult getArticleList() {
        return JsonResult.success(articleService.findAll());
    }

    @GetMapping("/article/search")
    public JsonResult getArticleListByTitle(String title) {
        Assert.notNull(title, "title must not be null.");
        return JsonResult.success(articleService.search(title));
    }

    @PostMapping("/article/save")
    public JsonResult save(Article article) {
        return JsonResult.success(articleService.save(article));
    }

    @GetMapping("/article/{id}")
    public JsonResult getArticleById(@PathVariable String id) throws Exception {
        log.info("getting article:{}", id);
        return JsonResult.success(articleService.getArticleById(id));
    }

}
