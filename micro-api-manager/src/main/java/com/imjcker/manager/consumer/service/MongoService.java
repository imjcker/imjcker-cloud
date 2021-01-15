package com.imjcker.manager.consumer.service;

public interface MongoService {

    void insert(String databaseName, String collectionName, String message);

    void insertCountModel(String databaseName, String collectionName, String message);
}
