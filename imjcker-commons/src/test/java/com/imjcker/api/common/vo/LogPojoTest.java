package com.imjcker.api.common.vo;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogPojoTest {
	private static Logger LOGGER = LoggerFactory.getLogger(LogPojoTest.class);


	public static void main(String[] args){
		for(int i=0;i< 1000;i++){
			new Thread(new Runnable() {

				@Override
				public void run() {
					String requestUuid = UUID.randomUUID().toString();
					LogPojo.init(requestUuid, Thread.currentThread().getName(), "serverName", "appkey", "versionId");

					LOGGER.info(LogPojo.getInfoLogMsg("开始", requestUuid));

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
					}


					LOGGER.info(LogPojo.getSuccessLogMsg("结束", requestUuid));


//					LOGGER.info(LogPojo.getErrorLogMsg("处理成功", null,new RuntimeException("我是异常")));

				}
			}).start();
		}



	}
}
