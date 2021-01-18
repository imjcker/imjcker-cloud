package com.imjcker.api.handler.plugin.resourceCache.impl.kafka;

import com.imjcker.api.handler.plugin.resourceCache.IResourceHandler;

/**
 * Kafka
 */
public class KafkaMQResourceHandler implements IResourceHandler{

	private String host;
	private String topicName;

	public KafkaMQResourceHandler(String host,String topicName){
		this.host = host;
		this.topicName = topicName;
	}

	@Override
	public KafkaMQ creat() {

		KafkaMQ rabbitMQ = new KafkaMQ(host,topicName);
		rabbitMQ.open();

		return rabbitMQ;
	}



	@Override
	public String getResourceId() {

		return String.format("kafka-%s-%s", host,topicName);
	}
}
