package com.ggx.game.support.core.scene.interfaces;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.ggx.core.common.executor.support.ExecutorSupport;
import com.ggx.core.common.message.model.Message;
import com.ggx.core.common.message.send.support.SendMessageSupport;
import com.ggx.game.support.core.interfaces.condition.CheckCondition;
import com.ggx.game.support.core.player.GGXPlayer;

/**
 * 房间游戏控制器接口
 * 
 * @param <R>
 * @param <P>
 * @author zai
 * 2019-02-16 17:57:18
 * @param <R>
 */
public interface PlayerSceneSupport<P extends GGXPlayer> extends ExecutorSupport, SendMessageSupport {
	
	
	
	/**
	 * 获取所有玩家
	 * 
	 * @return
	 * 
	 * @author zai 2019-02-10 14:18:49
	 */
	Map<Object, P> getPlayers();
	

	/**
	 * 根据id获取玩家
	 * 
	 * @return
	 * 
	 * @author zai 2019-02-10 14:34:15
	 */
	default P getPlayer(Object playerNo) {
		return getPlayers().get(playerNo);
	}
	
	/**
	 * 根据条件获取玩家
	 * 
	 * @param room
	 * @param condition
	 * @return
	 * @author zai
	 * 2019-02-16 17:48:42
	 */
	default P getPlayer(CheckCondition<P> condition) {
		for (Entry<Object, P> entry : getPlayers().entrySet()) {
			if (condition.check(entry.getValue())) {
				return entry.getValue();
			}
		}
		return null;
	}
	

	/**
	 * 遍历所有玩家
	 * 
	 * @param room
	 * @param eachPlayer
	 * 
	 * @author zai 2019-02-10 14:19:14
	 */
	default void eachPlayer(ForEachPlayer<P> eachPlayer) {
		for (Entry<Object, P> e : getPlayers().entrySet()) {
			eachPlayer.each(e.getValue());
		}
	}
	
	/**
	 * 遍历所有玩家并返回布尔值
	 * 
	 * @param room
	 * @param eachPlayer
	 * @return
	 * @author zai
	 * 2019-02-11 10:56:05
	 */
	default boolean boolEachPlayer(BoolForEachPlayer<P> eachPlayer) {
		for (Entry<Object, P> entry : getPlayers().entrySet()) {
			if (!eachPlayer.each(entry.getValue())) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 广播给所有玩家
	 * 
	 * @param message
	 * @author zai
	 * 2019-12-25 18:52:39
	 */
	default void broadcastToAllPlayer(Message message) {
		eachPlayer((player) -> {
			player.send(message);
		});
	}
	
	

	/**
	 * 广播给所有满足条件的玩家
	 * @param room
	 * @param actionId
	 * @param message
	 * @param condition
	 * 
	 * @author zai
	 * 2019-02-23 16:49:17
	 */
	default void broadcastToAllPlayer(Message message, CheckCondition<P> condition) {
		eachPlayer((player) -> {
			if (condition.check(player)) {
				player.send(message);	
			}
		});
	}

	
	/**
	 * 随机获取玩家
	 * 
	 * @return
	 * @author zai
	 * 2019-02-16 17:59:11
	 */
	@SuppressWarnings("unchecked")
	default P getRandomPlayer() {
		Map<Object, P> players = getPlayers();
		if (players.size() <= 0) {
			return null;
		}
		if (players.size() == 1) {
			return players.entrySet().stream().findFirst().get().getValue();
		}
		return ((Entry<Object, P>)players.entrySet().toArray()[ThreadLocalRandom.current().nextInt(players.size())]).getValue();
	}

	/**
	 * 随机获取满足条件的玩家
	 * 
	 * @param condition
	 * @return
	 * @author zai
	 * 2019-02-16 18:03:09
	 */
	default P getRandomPlayer(CheckCondition<P> condition) {
			Map<Object, P> players = getPlayers();
			List<P> plist = new ArrayList<>(players.size());
			for (Entry<Object, P> entry : players.entrySet()) {
				if (condition.check(entry.getValue())) {
					plist.add(entry.getValue());
				}
			}
			if (players.size() > 1) {
				return plist.get(ThreadLocalRandom.current().nextInt(plist.size()));
			}
			return plist.get(0);
		
	}

	
	/**
	 * 获取下一个位置的玩家
	 * @param room
	 * @param curplayer
	 * @param condition
	 * @return
	 */
	default P getNextPlayer(int startIndex, CheckCondition<P> condition) {
			//获取排序后的玩家
			List<P> pList = getSortedOnlinePlayerList();
			//排除起始序号
			if (condition != null) {
				pList = pList.stream().filter(o -> o.getIndex() != startIndex && condition.check(o)).collect(Collectors.toList());
			}else {
				pList = pList.stream().filter(o -> o.getIndex() != startIndex).collect(Collectors.toList());
			}
			
			if (pList.size() == 0) {
				return null;
			}
			
			for (P p : pList) {
				if (p.getIndex() > startIndex) {
					return p;
				}
			}
			
			for (P p : pList) {
				if (p.getIndex() < startIndex) {
					return p;
				}
			}
			
	
			return null;
		
	}
	
	/**
	 * 获取下一个位置的玩家
	 * 
	 * @param room
	 * @param startIndexNo
	 * @return
	 * @author zzz
	 * 2019-09-12 15:58:40
	 */
	default P getNextPlayer(int startIndexNo) {
		return getNextPlayer(startIndexNo, null);
		
	}

	/**
	 * 根据条件获取玩家
	 * 
	 * @param room
	 * @param condition
	 * @return
	 * @author zai
	 * 2019-02-20 19:01:58
	 */
	default List<P> getPlayers(CheckCondition<P> condition) {
			Map<Object, P> players = getPlayers();
			List<P> pList = new ArrayList<>(players.size());
			for (Entry<Object, P> entry : players.entrySet()) {
				if (condition == null) {
					pList.add(entry.getValue());
				}else {
					if (condition.check(entry.getValue())) {
						pList.add(entry.getValue());
					}			
				}
			}
			return pList;
	}

	/**
	 * 获取玩家列表
	 * 
	 * @param room
	 * @return
	 * @author zai
	 * 2019-02-20 20:36:06
	 */
	default List<P> getPlayerList() {
			Map<Object, P> players = getPlayers();
			List<P> pList = new ArrayList<>(players.size());
			for (Entry<Object, P> entry : players.entrySet()) {
				pList.add(entry.getValue());
			}
			return pList;
	}

	/**
	 * 获取一个序号
	 * 
	 * @param room
	 * @return
	 * @author zai
	 * 2019-02-21 11:15:34
	 */
	default int getNewIndex() {
		
		int maxPlayerNum = getMaxPlayerNum();
		List<Integer> nums = new ArrayList<>(maxPlayerNum);
		for (int i = 1; i <= maxPlayerNum; i++) {
			nums.add(i);
		}
		Map<Object, P> players = getPlayers();
		for (Entry<Object, P> entry : players.entrySet()) {
			nums.remove(new Integer(entry.getValue().getIndex()));
		}
		if (nums.size() == 0) {
			return -1;
		}
		return nums.get(0);
	}
	
	/**
	 * 根据序号获取玩家
	 * 
	 * @param index
	 * @return
	 * @author zai
	 * 2019-12-21 16:09:53
	 */
	default P getPlayerByIndex(int index) {
		List<P> players = getPlayerList();
		for (P p : players) {
			if (p.getIndex() == index) {
				return p;
			}
		}
		return null;
	}
	
	
	/**
	 * 获取最大玩家数
	 * 
	 * @return
	 * @author zai
	 * 2019-02-21 11:07:05
	 */
	int getMaxPlayerNum();

	/**
	 * 玩家迭代
	 * @param room
	 * @param iteration
	 * 
	 * @author zai
	 * 2019-02-23 16:03:00
	 */
	default void iteratePlayer(PlayerIteration<P> iteration) {
		Iterator<Entry<Object, P>> it = getPlayers().entrySet().iterator();
		while (it.hasNext()) {
			Entry<Object, P> next = it.next();
			iteration.it(next.getValue(), next, it);			
		}
	}

	/**
	 * 获取指定条件的玩家数量
	 * 
	 * @param room
	 * @param condition
	 * @return
	 * @author zai
	 * 2019-02-28 15:21:05
	 */
	default int countPlayers(CheckCondition<P> condition) {
			int i = 0;
			for (Entry<Object, P> entry : getPlayers().entrySet()) {
				if (condition.check(entry.getValue())) {
					i++;
				}
			}
			return i;
	}
	
	
	/**
	 * 获取玩家数量
	 * 
	 * @param room
	 * @return
	 * @author zai
	 * 2019-02-28 15:22:16
	 */
	default int countPlayers() {
		return getPlayers().size();
	}

	
	/**
	 * 获取已排序的在线玩家
	 * 
	 * @param room
	 * @return
	 * @author zai
	 * 2019-03-27 16:26:55
	 */
	default List<P> getSortedOnlinePlayerList() {
		List<P> onlinePlayers = getPlayers(player -> {
			return player.isOnline();
		});
		Collections.sort(onlinePlayers, (x, y) -> x.getIndex() - y.getIndex());
		return onlinePlayers;
	}
	
	/**
	 * 获取已排序的参在线的玩家
	 * 
	 * @param room
	 * @param comparator
	 * @return
	 * @author zai
	 * 2019-05-30 12:17:29
	 */
	default List<P> getSortedOnlinePlayerList(Comparator<P> comparator) {
			List<P> onlinePlayers = getPlayers(player -> {
				return player.isOnline();
			});
			Collections.sort(onlinePlayers, comparator);
			return onlinePlayers;
	}

	/**
	 * 遍历每个已准备玩家
	 * 
	 * @param room
	 * @param eachPlayer
	 * @author zai
	 * 2019-05-16 14:19:58
	 */
	default void eachOnlinePlayer(ForEachPlayer<P> eachPlayer) {
		P player = null;
		for (Entry<Object, P> e : getPlayers().entrySet()) {
			player = e.getValue();
			if (player.isOnline()) {
				eachPlayer.each(e.getValue());				
			}
		}
	}
	
	/**
	 * 获取指定条件的参在线的玩家数量
	 * 
	 * @param room
	 * @param condition
	 * @return
	 * @author zai
	 * 2019-05-21 15:54:04
	 */
	default int countOnlinePlayers(CheckCondition<P> condition) {
		int i = 0;
		P player = null;
		for (Entry<Object, P> entry : getPlayers().entrySet()) {
			player = entry.getValue();
			if (player.isOnline() && condition.check(player)) {
				i++;
			}
		}
		return i;
	}
	
	/**
	 * 获取参在线的玩家list
	 * 
	 * @param room
	 * @return
	 * @author zai
	 * 2019-05-30 12:03:07
	 */
	default List<P> getOnlinePlayerList() {
		Map<Object, P> players = getPlayers();
		List<P> pList = new ArrayList<>(players.size());
		for (Entry<Object, P> entry : players.entrySet()) {
			P player = entry.getValue();
			if (player.isOnline()) {
				pList.add(player);					
			}
		}
		return pList;
	}

	
	/**
	 * 广播给所有玩家
	 * 
	 * @param room
	 * @param actionId
	 * @author zai
	 * 2019-06-26 11:06:07
	 */
	default void broadcastToAllPlayer(String actionId) {
		eachPlayer(player -> {
			player.send(actionId);
		});
	}
	
	/**
	 * 广播给所有玩家
	 * 
	 * @param actionId
	 * @param message
	 * @author zai
	 * 2019-12-26 10:03:47
	 */
	default void broadcastToAllPlayer(String actionId, Object message) {
		eachPlayer(player -> {
			player.send((Message) message);
		});
	}

	
	/**
	 * 双循环玩家遍历
	 * 
	 * @param room
	 * @param eachPlayer
	 * @author zzz
	 * 2019-09-10 12:16:44
	 */
	default void doubleEachPlayer(DoubleEachPlayer<P> eachPlayer) {
		Set<Entry<Object, P>> entrySet = getPlayers().entrySet();
		for (Entry<Object, P> e : entrySet) {
			for (Entry<Object, P> e2 : entrySet) {
				eachPlayer.each(e.getValue(), e2.getValue());
			}
		}
	}

	/**
	 * 双循环玩家遍历
	 * 
	 * @param room
	 * @param eachPlayer
	 * @author zzz
	 * 2019-09-10 12:28:39
	 */
	default void doubleEachOnlinePlayer(DoubleEachPlayer<P> eachPlayer) {
		P player = null;
		P player2 = null;
		Set<Entry<Object, P>> entrySet = getPlayers().entrySet();
		for (Entry<Object, P> e : entrySet) {
			player = e.getValue();
			if (player.isOnline()) {
				for (Entry<Object, P> e2 : entrySet) {
					player2 = e2.getValue();
					if (player2.isOnline()) {
						eachPlayer.each(player, player2);							
					}
				}
			}
		}
	}

	
}
