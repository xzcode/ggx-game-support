package com.ggx.game.support.card.game.room.filter;

import com.ggx.core.common.event.EventManager;
import com.ggx.core.common.event.model.EventData;
import com.ggx.core.common.filter.EventFilter;
import com.ggx.core.common.session.GGXSession;
import com.ggx.core.common.utils.logger.GGLoggerUtil;
import com.ggx.game.support.card.game.house.House;
import com.ggx.game.support.card.game.player.RoomPlayer;
import com.ggx.game.support.card.game.room.SingleThreadExecutorRoom;

/**
 * 单线程执行器房间操作过滤器
 * <br>
 * 用于绑定用户操作到房间内的单线程执行器
 * 
 * @param <P>
 * @param <R>
 * @param <H>
 * @author zai
 * 2019-12-22 21:46:57
 */
public class SingleThreadExecutorRoomOperactionEventFilter
<
P extends RoomPlayer<P, R, H>,
R extends SingleThreadExecutorRoom<P, R, H>,
H extends House<P, R, H>
> implements EventFilter {
	
	protected EventManager eventManager;
	
	protected String playerSessionKey;

	public SingleThreadExecutorRoomOperactionEventFilter(String playerSessionKey, EventManager eventManager) {
		this.playerSessionKey = playerSessionKey;
		this.eventManager = eventManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean doFilter(EventData<?> eventData) {
		GGXSession session = eventData.getSession();
		if (session == null) {
			return true;
		}
		P player = (P) session.getAttribute(playerSessionKey);	
		if (player == null) {
			return true;
		}
		R room = player.getRoom();
		if (room != null) {
			room.addOperaction((r) -> {
				try {
					eventManager.emitEvent(eventData);
				} catch (Exception e) {
					GGLoggerUtil.getLogger(this).error("Event Filter Error!", e);
				}
			});
			return false;
		}
		return true;
	}

}
