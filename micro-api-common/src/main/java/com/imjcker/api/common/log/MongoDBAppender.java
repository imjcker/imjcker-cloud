//package com.lemon.common.log.appender;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.bson.Document;
//
//import com.mongodb.MongoClient;
//import com.mongodb.ServerAddress;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//
//import ch.qos.logback.classic.spi.LoggingEvent;
//import ch.qos.logback.core.AppenderBase;
//import ch.qos.logback.core.status.ErrorStatus;
//
///**
// * @Title: MongoDB存储日志Appender类
// * @Package com.lemon.common.log.appender
// * @author zhoulong
// * @date 2017年7月20日 上午11:05:57
// * @version V2.0
// */
//
//public class MongoDBAppender extends AppenderBase<LoggingEvent> {
//
//    private MongoClient _mongo;
//    private MongoCollection<Document> _collection;
//    private String _dbHost = "172.16.16.70";
//    private String _dbName = "logging";
//    private String _dbCollectionName = "logging";
//
//
//    @Override
//    public void start() {
//        try {
//        	String[] hosts = _dbHost.split(",");
//            List<ServerAddress> serverAddressList = new ArrayList<>();
//            for(String host : hosts) {
//                serverAddressList.add(new ServerAddress(host));
//            }
//            _mongo = new MongoClient(serverAddressList);
//        	MongoDatabase db = _mongo.getDatabase(_dbName);
//        	_collection = db.getCollection(_dbCollectionName);
//        } catch (Exception e) {
//            addStatus(new ErrorStatus("Failed to initialize MondoDB", this, e));
//            return;
//        }
//        super.start();
//    }
//
//    public void setDbHost(String dbHost) {
//        _dbHost = dbHost;
//    }
//
//    public void setDbName(String dbName) {
//        _dbName = dbName;
//    }
//
//    public void setDbCollectionName(String dbCollectionName) {
//    	_dbCollectionName = dbCollectionName;
//    }
//
//    @Override
//    public void stop() {
//        _mongo.close();
//        super.stop();
//    }
//
//    @Override
//    protected void append(LoggingEvent event) {
//    	try{
//        	Document document = getDocument(event.getFormattedMessage());
//        	if(document != null){
//        		_collection.insertOne(document);
//        	}
//    	}catch (Exception e) {
//    		addStatus(new ErrorStatus("日志写入到MongDB出错", this, e));
//		}
//    }
//
//	private Document getDocument(String json) {
//        try{
//        	return Document.parse(json);
//        }catch (Exception e) {
//        	return null;
//        }
//	}
//}
