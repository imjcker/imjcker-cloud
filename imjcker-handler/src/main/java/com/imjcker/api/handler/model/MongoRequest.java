package com.imjcker.api.handler.model;

import java.io.Serializable;

public class MongoRequest implements Serializable {

    private static final long serialVersionUID = -6128195768877642371L;

    private String dbName;

    private String collectionName;

    private AsyncModel asyncModel;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public AsyncModel getAsyncModel() {
        return asyncModel;
    }

    public void setAsyncModel(AsyncModel asyncModel) {
        this.asyncModel = asyncModel;
    }
}
