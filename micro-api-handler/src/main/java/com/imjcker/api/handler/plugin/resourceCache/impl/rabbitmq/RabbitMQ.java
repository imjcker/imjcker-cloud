package com.imjcker.api.handler.plugin.resourceCache.impl.rabbitmq;

import java.util.List;

import com.imjcker.api.common.vo.LogPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.imjcker.api.handler.plugin.resourceCache.IJsonSerializer;
import com.imjcker.api.handler.plugin.resourceCache.IResource;
import com.imjcker.api.handler.plugin.resourceCache.impl.ResourceUtils;
import com.imjcker.api.handler.plugin.resourceCache.impl.ResourceUtils.Addr;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;


/**
 * RabbitMQ
 */
public class RabbitMQ implements IResource {
	private static Logger LOGGER = LoggerFactory.getLogger(RabbitMQ.class);

	private static int DEFAULT_PORT = AMQP.PROTOCOL.PORT;
	private Connection connection = null;
	private Channel channel = null;
	private boolean available = false;

	private String host;
	private String userName;
	private String passwd;
	private String topicName;

	public RabbitMQ(String host, String userName, String passwd, String topicName) {
		this.host = host;
		this.userName = userName;
		this.passwd = passwd;
		this.topicName = topicName;
	}

	public void relase() {
		available = false;
		try {
			if (channel != null) {
				channel.close();
			}
		} catch (Exception e) {
			LOGGER.error("Exception:{}",e);
		}

		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			LOGGER.error(LogPojo.getErrorLogMsg("rabbitMQ release 发生异常",this,e));
		}
	}

	@Override
	public boolean isAvalable() {
		return available;
	}

	public void open() {
		try {
			// 创建链接Factory
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUsername(userName);
			factory.setPassword(passwd);
			factory.setAutomaticRecoveryEnabled(true);

			connection = factory.newConnection(getAddress(host));
			connection.addShutdownListener(new ShutdownListener() {
				@Override
				public void shutdownCompleted(ShutdownSignalException cause) {
					relase();
				}

			});

			channel = connection.createChannel();
			channel.queueDeclare(topicName, true, false, false, null);

			this.available = true;

			LOGGER.debug(LogPojo.getInfoLogMsg("建立RabbitMQ连接成功", this));
		} catch (Exception e) {

			LOGGER.error(LogPojo.getErrorLogMsg("建立RabbitMQ连接出错", this, e));
			relase();
		}

	}

	private Address[] getAddress(String host) {
		List<Addr> address = ResourceUtils.getAddress(host, DEFAULT_PORT);

		Address[] result = new Address[address.size()];
		for (int i = 0; i < address.size(); i++) {

			Addr addr = address.get(i);
			result[i] = new Address(addr.getHost(), addr.getPort());
		}
		return result;
	}

	@Override
	public boolean push(IJsonSerializer pojo) {
		try {
			if (!isAvalable()) return false;
			String message = pojo.toJsonString();

			String[] topicNames = topicName.split("[,，]");

			for (int i = 0; i < topicNames.length; i++) {
				channel.basicPublish("", topicName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
			}
			LOGGER.debug(LogPojo.getInfoLogMsg("发送RabbitMQ消息成功", pojo));
			return true;
		} catch (Exception e) {

			LOGGER.error(LogPojo.getErrorLogMsg("发送RabbitMQ消息出错", pojo, e));
			relase();
			return false;
		}
	}

	public String getHost() {
		return host;
	}

	public String getUserName() {
		return userName;
	}

	public String getPasswd() {
		return passwd;
	}

	public String getTopicName() {
		return topicName;
	}
}
