package com.imjcker.api.handler.plugin.resourceCache.impl.mongo;

import com.imjcker.api.handler.plugin.resourceCache.IResourceHandler;

/**
 * MongoDB
 */
public class MongoDBResourceHandler implements IResourceHandler{
	private String uri;
	private String dbName;
	private String collectionName;

	public MongoDBResourceHandler(String uri,String dbName,String collectionName){
		this.uri = uri;
		this.dbName = dbName;
		this.collectionName = collectionName;
	}

	@Override
	public MongoDB creat() {
		MongoDB mongoDB = new MongoDB(uri,dbName,collectionName);
		mongoDB.open();
		return mongoDB;
	}



	@Override
	public String getResourceId() {

		return String.format("mongodb-%s-%s-%s", uri,dbName,collectionName);
	}
}
