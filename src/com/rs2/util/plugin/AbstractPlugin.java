package com.rs2.util.plugin;

import com.rs2.model.players.Player;
import com.rs2.net.packet.Packet;

public abstract class AbstractPlugin {
	
	public abstract String getName();
	
	public abstract String getAuthor();
	
	public abstract double getVersion();
	
	public abstract boolean canTick();
	
	public void onCreate() {
	}
	
	public void onDestroy() {
	}
	
	public void onTick() {
	}
	
	public void reset() {
	}
	
	/**
	 * Called when a packet arrives.
	 * @param player The player receiving the packet.
	 * @param packet The received packet.
	 * @return Return TRUE if this packet should be processed elsewhere, 
	 * return FALSE if this packet should not be processed elsewhere.
	 */
	public boolean onPacketArrival(Player player, Packet packet) {
		return true;
	}
	
	/**
	 * Called when the passed in player is being updated.
	 * @param player The player being updated.
	 */
	public void onPlayerTick(Player player) {
	}
	
	/**
	 * Logs the specified message to local system output.
	 * @param message The message to log.
	 */
	public void log(String message) {
		System.out.println("[PLUGIN-" + getName() + "]: " + message);
	}

}
