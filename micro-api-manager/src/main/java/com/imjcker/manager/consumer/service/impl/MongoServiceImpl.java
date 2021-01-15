package com.imjcker.manager.consumer.service.impl;

import com.imjcker.manager.consumer.service.MongoService;
import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class MongoServiceImpl implements MongoService {
    private final MongoClient mongoClient;

    @Autowired
    public MongoServiceImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void insert(String databaseName, String collectionName, String message) {
        Document objDoc = Document.parse(message);
        MongoCollection<Document> mongoCollection = getCollection(databaseName, collectionName);
        mongoCollection.insertOne(objDoc);
    }

    @Override
    public void insertCountModel(String databaseName, String collectionName, String message) {
        Document objDoc = Document.parse(message);
        // mongoTTL索引需要date类型的字段
        objDoc.put("date", getMongoDate(new Date()));
        MongoCollection<Document> mongoCollection = getCollection(databaseName, collectionName);
        mongoCollection.insertOne(objDoc);
    }

    private MongoCollection<Document> getCollection(String dbName, String collectionName) {
        MongoDatabase mongoDatabase = mongoClient.getDatabase(dbName);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(collectionName);
        mongoCollection.withReadPreference(ReadPreference.secondaryPreferred());
        return mongoCollection;
    }

    private static Date getMongoDate(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.HOUR_OF_DAY, -8);
        return ca.getTime();
    }
}
