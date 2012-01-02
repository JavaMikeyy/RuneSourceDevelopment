package com.rs2.model.players;

import com.rs2.model.World;
import com.rs2.util.NameUtil;

public class PrivateMessaging {
	
	private final Player player;
	private static int lastPrivateMessageId = 1;
	
	public PrivateMessaging(Player player) {
		this.player = player;
	}
	
	public void sendPMOnLogin() {
		player.getActionSender().sendPMServer(2);
		refresh();
	}
	
	public void refresh() {
		for (int i = 0; i < player.getFriends().length; i ++) {
			if (player.getFriends()[i] == 0) {
				continue;
			}
			if (player.getFriends() == null) {
				continue;
			}
			if (player == null) {
				continue;
			}
			player.getActionSender().sendFriendList(player.getFriends()[i], 
					getWorld(player.getFriends()[i]));
		}
		long name = NameUtil.nameToLong(player.getUsername());
		int world = getWorld(name);
		for (Player players : World.getPlayers()) {
			if (players == null)
				continue;
			if (players.getPrivateMessaging().contains(players.getFriends(), name)) {
				players.getActionSender().sendFriendList(name, world);
			}
		}
	}
	
	public void addToFriendsList(long name) {
		if (getCount(player.getFriends()) >= 200) {
			player.getActionSender().sendMessage("Your friends list is full.");
			return;
		}
		if (contains(player.getFriends(), name)) {
			player.getActionSender().sendMessage(""+ NameUtil.longToName(name) + " is already on your friends list.");
			return;
		}
		int slot = getFreeSlot(player.getFriends());
		player.getFriends()[slot] = name;
		player.getActionSender().sendFriendList(name, getWorld(name));
	}
	
	public void addToIgnoresList(long name) {
		if (getCount(player.getIgnores()) >= 100) {
			player.getActionSender().sendMessage("Your ignores list is full.");
			return;
		}
		if (contains(player.getIgnores(), name)) {
			player.getActionSender().sendMessage(""+ NameUtil.longToName(name) + " is already on your ignores list.");
			return;
		}
		int slot = getFreeSlot(player.getIgnores());
		player.getIgnores()[slot] = name;
	}
	
	public void sendPrivateMessage(Player from, long to, byte[] message,
			int messageSize) {
		for (Player p : World.getPlayers()) {
			if (p != null) {
				if (NameUtil.nameToLong(p.getUsername()) == to) {
					p.getActionSender().sendPrivateMessage(NameUtil.nameToLong(from.getUsername()),
							from.getStaffRights(), message, messageSize);
				}
			}
		}
	}
	
	public void removeFromList(long[] person, long name) {
		for (int i = 0; i < person.length; i ++) {
			if (person[i] == name) {
				person[i] = 0;
			}
		}
	}
	
	private int getWorld(long friend) {
		for (Player p : World.getPlayers()) {
			if (p != null) {
				if (NameUtil.nameToLong(p.getUsername()) == friend) {
					return 1;
				}
			}
		}
		return 0;
	}
	
	public boolean contains(long[] person, long name) {
		for (int i = 0; i < person.length; i++) {
			if (person[i] == name) {
				return true;
			}
		}
		return false;
	}
	
	public int getCount(long[] name) {
		int count = 0;
		for (long names : name) {
			if (names > 0) {
				count ++;
			}
		}
		return count;
	}
	
	public int getFreeSlot(long[] person) {
		for (int i = 0; i < person.length; i ++) {
			if (person[i] == 0) {
				return i;
			}
		}
		return -1;
	}

	public int getLastPrivateMessageId() {
		return lastPrivateMessageId ++;
	}

}
