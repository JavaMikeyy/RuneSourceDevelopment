package com.rs2.net.packet.packets;

import com.rs2.model.players.Player;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

/**
 * @author Purehit (http://rune-server.org)
 */

public class IdleLogoutPacketHandler implements PacketHandler {

	public static final int IDLELOGOUT = 202;
	
	public void handlePacket(Player player, Packet packet) {
		/*try {
			if (player.getPets().getPet() != null)
				player.getPets().unregisterPet();
			player.logout();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}