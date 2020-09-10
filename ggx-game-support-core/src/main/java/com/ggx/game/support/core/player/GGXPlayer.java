package com.ggx.game.support.core.player;

import com.ggx.game.support.core.player.support.GGXPlayerSupport;

/**
 * GGX玩家统一接口
 *
 * @author zai
 * 2020-09-10 18:11:50
 */
public interface GGXPlayer extends GGXPlayerSupport{
	
	/**
	 * 获取玩家id
	 *
	 * @return
	 * @author zai
	 * 2020-09-10 18:05:40
	 */
	String getPlayerId();
	
	/**
	 * 获取序号
	 *
	 * @return
	 * @author zai
	 * 2020-09-10 17:39:36
	 */
	int getIndex();
	
	/**
	 * 是否在线
	 *
	 * @return
	 * @author zai
	 * 2020-09-10 17:41:27
	 */
	boolean isOnline();
	
	/**
	 * 重置玩家数据
	 *
	 * @author zai
	 * 2020-09-10 18:11:12
	 */
	void reset();
	

}
