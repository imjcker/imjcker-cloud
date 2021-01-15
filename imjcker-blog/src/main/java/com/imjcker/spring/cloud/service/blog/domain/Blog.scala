package com.imjcker.spring.cloud.service.blog.domain

import javax.persistence.{Entity, GeneratedValue, Id}

import scala.beans.BeanProperty

@Entity
class Blog {
  @Id
  @GeneratedValue
  @BeanProperty
  var id: Integer = _
  @BeanProperty
  var title: String = _
}
