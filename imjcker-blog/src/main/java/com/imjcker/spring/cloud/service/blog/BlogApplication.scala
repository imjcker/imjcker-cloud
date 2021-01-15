package com.imjcker.spring.cloud.service.blog

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class BlogApplication {
  print("hello world")
}
object BlogApplication {
  def main(args: Array[String]){
    SpringApplication.run(classOf[BlogApplication], args:_*)
  }
}