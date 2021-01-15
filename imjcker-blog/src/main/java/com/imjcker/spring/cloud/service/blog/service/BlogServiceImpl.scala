package com.imjcker.spring.cloud.service.blog.service

import com.imjcker.spring.cloud.service.blog.dao.BlogDao
import com.imjcker.spring.cloud.service.blog.domain.Blog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service class BlogServiceImpl extends BlogService {
  @Autowired
  var blogDao: BlogDao = _

  override def save(blog: Blog): Blog = {
    this.blogDao.save(blog)
  }

  override def getById(id: Integer): Blog = this.blogDao.findById(id).get()
}
