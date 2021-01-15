package com.imjcker.api.handler.plugin.resourceCache.impl.kafka;

import java.util.Properties;

import com.imjcker.api.common.vo.LogPojo;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imjcker.api.handler.plugin.resourceCache.IJsonSerializer;
import com.imjcker.api.handler.plugin.resourceCache.IResource;
import org.apache.kafka.clients.producer.Producer;

/**
 * Kafka
 */
public class KafkaMQ implements IResource{
	private static Logger LOGGER = LoggerFactory.getLogger(KafkaMQ.class);

    private boolean available = false;

    private String host;
    private String topicName;
    private Producer<String, String> producer;

    public KafkaMQ(String host, String topicName){
    	this.host = host;
    	this.topicName = topicName;
    }

	public void relase(){
		available = false;
		try{
			if(producer != null){
				producer.close();
			}
		}catch (Exception e) {
		}
	}

	@Override
	public boolean isAvalable() {
		return available;
	}

	public void open() {
		try{
			 Properties props = new Properties();
	         props.put("broker-list", host);
	         props.put("topic", topicName);
	         props.put("bootstrap.servers", host);
	         //The "all" setting we have specified will result in blocking on the full commit of the record, the slowest but most durable setting.
	        //“所有”设置将导致记录的完整提交阻塞，最慢的，但最持久的设置。
	         props.put("acks", "all");
	         //如果请求失败，生产者也会自动重试，即使设置成０ the producer can automatically retry.
	         props.put("retries", 0);

	         //The producer maintains buffers of unsent records for each partition.
	         props.put("batch.size", 16384);
	         //默认立即发送，这里这是延时毫秒数
	         props.put("linger.ms", 1);
	         //生产者缓冲大小，当缓冲区耗尽后，额外的发送调用将被阻塞。时间超过max.block.ms将抛出TimeoutException
	         props.put("buffer.memory", 33554432);
	         //The key.serializer and value.serializer instruct how to turn the key and value objects the user provides with their ProducerRecord into bytes.
	         props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	         props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

	         //创建kafka的生产者类
	         producer = new KafkaProducer<String, String>(props);


			LOGGER.debug(LogPojo.getInfoLogMsg("发送KafkaMQ链接成功", this));
	        available = true;
		}catch (Exception e) {

			LOGGER.error(LogPojo.getErrorLogMsg("创建KafkaMQ链接出错",this, e));
			relase();
		}

	}

	@Override
	public boolean push(IJsonSerializer pojo) {
		try {
			if(!isAvalable()) {
				return false;
			}

	        String msg = pojo.toJsonString();
	        ProducerRecord<String, String> message = new ProducerRecord<String, String>(topicName,msg);
			producer.send(message );

			LOGGER.debug(LogPojo.getInfoLogMsg("发送KafkaMQ消息成功", pojo));
	        return true;
		} catch (Exception e) {
			LOGGER.error(LogPojo.getErrorLogMsg("发送KafkaMQ消息出错", pojo, e));
			relase();
			return false;
		}
	}

	public String getHost() {
		return host;
	}

	public String getTopicName() {
		return topicName;
	}

	public Producer<String, String> getProducer() {
		return producer;
	}


}
