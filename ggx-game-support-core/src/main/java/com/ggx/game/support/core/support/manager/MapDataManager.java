package com.ggx.game.support.core.support.manager;

import java.util.Map;

/**
 * Map管理器接口
 *
 * @author zai
 * 2020-09-15 14:28:15
 */
public interface MapDataManager<K, V> {
	
	
	
	Map<K, V> getMap();
	
	/**
	 * 添加
	 *
	 * @param key 键
	 * @param value 值
	 * @author zai
	 * 2020-09-15 18:07:43
	 */
	default void put(K key, V value) {
		getMap().put(key, value);
	}
	
	/**
	 * 移除
	 *
	 * @param key
	 * @author zai
	 * 2020-09-15 16:49:08
	 */
	default V remove(K key) {
		return getMap().remove(key);
	}

}
