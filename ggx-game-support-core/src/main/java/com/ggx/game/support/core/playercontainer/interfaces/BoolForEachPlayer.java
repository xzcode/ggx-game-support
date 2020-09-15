package com.ggx.game.support.core.playercontainer.interfaces;

/**
 * 遍历玩家接口
 * 
 * @param <P>
 * @author zai
 * 2019-02-11 10:50:38
 */
public interface BoolForEachPlayer<P> {

	/**
	 * 每次遍历执行的方法
	 * 
	 * @param player
	 * @return 返回布尔值，true则继续执行，false则终止遍历
	 * @author zai
	 * 2019-02-11 10:50:58
	 */
	boolean each(P player);
	
}
