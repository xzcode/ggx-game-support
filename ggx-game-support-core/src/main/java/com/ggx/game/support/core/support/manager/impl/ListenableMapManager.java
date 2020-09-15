package com.ggx.game.support.core.support.manager.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ggx.game.support.core.support.listener.ListenerManager;
import com.ggx.game.support.core.support.listener.impl.DefaultListenerManager;
import com.ggx.game.support.core.support.listener.listener.Listener;
import com.ggx.game.support.core.support.manager.MapManager;

public abstract class ListenableMapManager<K, V> implements MapManager<K, V>{
	
	protected Map<K, V> map = new ConcurrentHashMap<>();
	
	private ListenerManager<V> onPutListenerManager = new DefaultListenerManager<>();
	
	private ListenerManager<V> onRemoveListenerManager = new DefaultListenerManager<>();
	
	@Override
	public Map<K, V> getMap() {
		return map;
	}
	
	public void onPut(Listener<V> listener) {
		this.onPutListenerManager.addListener(listener);
	}
	
	public void onRemove(Listener<V> listener) {
		this.onRemoveListenerManager.addListener(listener);
	}

	@Override
	public void put(K key, V value) {
		MapManager.super.put(key, value);
		this.onPutListenerManager.triggerListeners(value);
	}

	@Override
	public V remove(K key) {
		V removedValue = MapManager.super.remove(key);
		this.onPutListenerManager.triggerListeners(removedValue);
		return removedValue;
	}
	
	

}
