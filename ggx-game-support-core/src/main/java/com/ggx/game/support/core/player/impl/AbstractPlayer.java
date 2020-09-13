package com.ggx.game.support.core.player.impl;

import com.ggx.core.common.session.GGXSession;
import com.ggx.game.support.core.player.GGXPlayer;

/**
     玩家基类
 * 
 * @author zai
 * 2019-01-21 20:21:20
 */
public abstract class AbstractPlayer implements GGXPlayer{
	
	protected GGXSession session; 
	
	//玩家id
	protected String playerId; 
	
	//序号
	protected int index; 
	
	//是否在线
	protected boolean online; 
	
	public String getPlayerId() {
		return playerId;
	}


	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}


	@Override
	public GGXSession getSession() {
		return session;
	}

	public void setSession(GGXSession session) {
		this.session = session;
	}


	@Override
	public int getIndex() {
		return index;
	}


	@Override
	public boolean isOnline() {
		return online;
	}


	public void setIndex(int seat) {
		this.index = seat;
	}


	public void setOnline(boolean online) {
		this.online = online;
	}


	
	
	
}
