package com.imjcker.spring.cloud.service.blog.service

import com.imjcker.spring.cloud.service.blog.domain.Blog

/**
 * blog service
 */
trait BlogService {
  /**
   * save blog
   * @param blog blog entity
   * @return blog entity
   */
  def save(blog: Blog): Blog


  /**
   * get blog by ID
   * @param id ID
   * @return blog entity if not null
   */
  def getById(id: Integer): Blog
}
