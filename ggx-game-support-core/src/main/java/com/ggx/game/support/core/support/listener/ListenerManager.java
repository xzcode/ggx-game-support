package com.ggx.game.support.core.support.listener;

import java.util.List;

import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.game.support.core.support.listener.listener.Listener;

/**
 * 取消行为支持接口
 *
 * @param <T>
 * @author zai
 * 2020-09-15 16:55:47
 */
public interface ListenerManager<T> {
	
	/**
	 * 获取所有监听器
	 *
	 * @return
	 * @author zai
	 * 2020-09-15 16:58:42
	 */
	List<Listener<T>> getListeners();
	
	
	/**
	 * 添加取消监听器
	 *
	 * @param listener
	 * @author zai
	 * 2020-09-15 16:55:56
	 */
	default void addListener(Listener<T> listener) {
		getListeners().add(listener);
	}
	
	/**
	 * 移除监听器
	 *
	 * @param listener
	 * @author zai
	 * 2020-09-15 16:56:42
	 */
	default void removeListener(Listener<T> listener) {
		getListeners().remove(listener);
	}
	
	/**
	 * 触发所有监听器
	 *
	 * @param listener
	 * @author zai
	 * 2020-09-15 17:15:16
	 */
	default void triggerListeners(T data) {
		this.triggerListeners(data, true);
	}
	
	/**
	 * 触发所有监听器
	 *
	 * @param data
	 * @param continueOnError 遇到错误是否继续触发后续监听器
	 * @author zai
	 * 2020-09-15 17:38:15
	 */
	default void triggerListeners(T data, boolean continueOnError) {
		List<Listener<T>> listeners = getListeners();
		for (Listener<T> listener : listeners) {
			if (continueOnError) {
				try {
					listener.onTrigger(data);
				} catch (Exception e) {
					GGLoggerUtil.getLogger(this).error("Trigger Listener Error!", e);
				}
			}else {
				triggerListeners(data);
			}
		}
	}
	
	
	
}
