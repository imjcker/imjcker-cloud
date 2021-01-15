package com.imjcker.api.handler.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @Title: 权重随机负载均衡算法实现类
 * @Package com.lemon.client.util
 * @author yezhiyuan
 * @date 2017年9月6日 下午5:22:19
 * @version V2.0
 */
public class LoadBalance {

	/**
	 * @Title: 获取服务器
	 * @param @param Map<服务器, 权重0-10>
	 */
	public static String getServer(Map<String, Integer> map) {
		// 重建一个Map，避免并发问题
		Map<String, Integer> serverMap = new HashMap<String, Integer>();
		serverMap.putAll(map);

		// 取得Ip地址List
		Set<String> keySet = serverMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		List<String> serverList = new ArrayList<String>();
		Integer serverListSize = 0;
		while (iterator.hasNext()) {
			String server = iterator.next();
			// 权重范围限定，0 ~ 10，超过取10
			int weightTemp = serverMap.get(server);
			int weight = weightTemp > 10 ? 10 : weightTemp;
			for (int i = 0; i < weight; i++){
				serverList.add(server);
				serverListSize++;
			}
		}
		// 选择本次路由地址
        Random random = new Random();
        int randomPos = random.nextInt(serverListSize);
        String server = serverList.get(randomPos);

		return server;
	}

}
