package com.imjcker.spring.cloud.service.blog.dao

import com.imjcker.spring.cloud.service.blog.domain.Blog
import org.springframework.data.repository.CrudRepository

trait BlogDao extends CrudRepository[Blog, Integer] {

}
