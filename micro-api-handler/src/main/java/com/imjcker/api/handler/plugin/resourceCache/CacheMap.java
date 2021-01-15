package com.imjcker.api.handler.plugin.resourceCache;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 带过期的缓存Map
 */
public class CacheMap<K, V extends IResource> {
	private Map<K,ExpireValue<V>> _map = new ConcurrentHashMap<K,ExpireValue<V>>();


	public CacheMap(){
//		this(0,1000*60*10);		//10分钟
		this(0,1000);		//10分钟
	}

	protected CacheMap(long delay,long period){
        new Timer().schedule(new TimerTask(){
            @Override
            public void run() {
                long now = System.currentTimeMillis();//获取系统时间

                Iterator<Entry<K, ExpireValue<V>>> entries = _map.entrySet().iterator();
                while (entries.hasNext()) {

                    Entry<K, ExpireValue<V>> entry = entries.next();
                    ExpireValue<V> expireValue = entry.getValue();
                    if(expireValue.expireAt < now){

                    	expireValue.value.relase();
	                    entries.remove();
                    }
                }

            }
        }, delay,period);
	}


	/**
	 * 获取值,会自动更新到期时间
	 */
	public V get(Object key) {

		ExpireValue<V> expireValue = _map.get(key);

		if(expireValue != null){
			expireValue.updateExpire();
			return expireValue.value;
		}

		return null;
	}


    /**
     * @param key
     * @param value
     * @param keepTime 保存的时间,单位毫秒
     * @return
     */
    public void put(K key, V value,long keepTime) {
		ExpireValue<V> expireValue = new ExpireValue<V>(value, keepTime);
		_map.put(key, expireValue);
    }

	private class ExpireValue<M>{
		/**
		 * 到期的时间
		 */
		private long expireAt ;
		/**
		 * 存入的时间
		 */
		private long storeTime;
		private long keepTime;
		private M value;

		public ExpireValue(M value,long keepTime){
			this.keepTime = keepTime;
			this.storeTime = System.currentTimeMillis();
			this.expireAt = this.storeTime + this.keepTime;
			this.value = value;
		}

		public void updateExpire() {
			this.storeTime = System.currentTimeMillis();
			this.expireAt = this.storeTime + this.keepTime;

		}
	}

}
