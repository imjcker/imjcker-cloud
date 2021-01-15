package com.imjcker.spring.cloud.service.elastic.repository;


import com.imjcker.spring.cloud.service.elastic.model.Doc;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface ElasticRepository extends ElasticsearchCrudRepository<Doc, String> {

}
