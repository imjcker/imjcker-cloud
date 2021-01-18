package com.imjcker.api.handler.plugin.resourceCache.impl.rabbitmq;

import com.imjcker.api.handler.plugin.resourceCache.IResourceHandler;

/**
 * RabbitMQ
 */
public class RabbitMQResourceHandler implements IResourceHandler{
	private String host;
	private String userName;
	private String passwd;
	private String topicName;


	public RabbitMQResourceHandler(String host, String userName, String passwd, String topicName) {
		this.host = host;
		this.userName = userName;
		this.passwd = passwd;
		this.topicName = topicName;
	}

	@Override
	public RabbitMQ creat() {

		RabbitMQ rabbitMQ = new RabbitMQ(host,userName,passwd,topicName);
		rabbitMQ.open();

		return rabbitMQ;
	}



	@Override
	public String getResourceId() {

		return String.format("rabbitmq-%s-%s-%s-%s", host,userName,passwd,topicName);
	}
}
