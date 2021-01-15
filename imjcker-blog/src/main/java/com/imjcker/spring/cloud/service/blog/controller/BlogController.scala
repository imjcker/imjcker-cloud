package com.imjcker.spring.cloud.service.blog.controller

import com.imjcker.spring.cloud.service.blog.domain.Blog
import com.imjcker.spring.cloud.service.blog.service.BlogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, PostMapping, RequestMapping, RequestParam, RestController}

@RestController
@RequestMapping(Array("/blog"))
class BlogController {
  @Autowired
  var blogService: BlogService = _

  @PostMapping(Array("save"))
  def save(blog: Blog): Blog = this.blogService.save(blog)

  @GetMapping(Array("getById"))
  def getById(@RequestParam id: Integer): Blog = this.blogService.getById(id)

}
