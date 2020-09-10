package com.ggx.game.support.card.game.room.listener;

/**
 * 添加房间监听器 
 * @param <R>
 * 
 * @author zai
 * 2019-12-01 12:19:12
 */
@FunctionalInterface
public interface IAddRoomListener<R> {
	
	void add(R room);
	
}
