package com.imjcker.spring.cloud.service.blog.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BLogTest @Autowired() (private

val blogService: BlogService){
  @Test
  def save() {
    val entity = this.blogService.getById(1)
    print(entity)
  }

}
