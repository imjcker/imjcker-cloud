package com.imjcker.api.handler.plugin.resourceCache.impl.mongo;

import com.imjcker.api.common.vo.LogPojo;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imjcker.api.handler.plugin.resourceCache.IJsonSerializer;
import com.imjcker.api.handler.plugin.resourceCache.IResource;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * MongoDB
 */
public class MongoDB implements IResource{
	private static Logger LOGGER = LoggerFactory.getLogger(MongoDB.class);

    private MongoClient client;
    private boolean available = false;

    //mongodb://kwiner:test123@127.0.0.1/test?authMechanism=MONGODB-CR&maxPoolSize=500
    private String uri;
    private String databaseName;
    private String collectionName;


	public MongoDB(String uri, String databaseName, String collectionName) {
		this.uri = uri;
		this.databaseName = databaseName;
		this.collectionName = collectionName;
	}

	public void relase(){
		available = false;
		try{
			if(client != null){

				client.close();
			}
		}catch (Exception e) {
			LOGGER.error(LogPojo.getErrorLogMsg("Mongo release 发生异常",this,e));
		}
	}

	@Override
	public boolean isAvalable() {
		return available;
	}

	public void open() {
		try{
			client = new MongoClient(new MongoClientURI(uri));

			LOGGER.debug("创建MongoDB链接成功");
			available = true;
		}catch (Exception e) {

			LOGGER.error(LogPojo.getErrorLogMsg("创建MongoDB链接出错",this,e));
			relase();
		}

	}

	@Override
	public boolean push(IJsonSerializer pojo) {
		try {
			if(!isAvalable()) return false;

			MongoDatabase database = client.getDatabase(databaseName);
			MongoCollection<Document> collection = database.getCollection(collectionName);

			String json = pojo.toJsonString();
			Document document = Document.parse(json);
			collection.insertOne(document );

			LOGGER.debug("发送MongoDB消息成功");
	        return true;
		} catch (Exception e) {

			LOGGER.error(LogPojo.getErrorLogMsg( "发送MongoDB消息出错",pojo,e));
			relase();
			return false;
		}
	}

	public String getUri() {
		return uri;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public String getCollectionName() {
		return collectionName;
	}
}
