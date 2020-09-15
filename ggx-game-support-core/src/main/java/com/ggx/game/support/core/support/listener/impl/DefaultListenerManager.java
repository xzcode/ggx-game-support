package com.ggx.game.support.core.support.listener.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ggx.game.support.core.support.listener.ListenerManager;
import com.ggx.game.support.core.support.listener.listener.Listener;

/**
 * 默认监听器管理器
 *
 * @param <T>
 * @author zai
 * 2020-09-15 17:40:26
 */
public class DefaultListenerManager<T> implements ListenerManager<T>{
	
	protected List<Listener<T>> listeners = new CopyOnWriteArrayList<>();

	@Override
	public List<Listener<T>> getListeners() {
		return listeners;
	}

}
