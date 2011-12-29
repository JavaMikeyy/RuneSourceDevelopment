package com.rs2.net.packet.packets;

import com.rs2.model.players.GlobalObjectHandler;
import com.rs2.model.players.ItemManager;
import com.rs2.model.players.Player;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class LoadRegionPacketHandler implements PacketHandler {
	
	public static final int LOAD_REGION = 121;

	@Override
	public void handlePacket(Player player, Packet packet) {
		ItemManager.getInstance().loadOnRegion(player);
		GlobalObjectHandler.createGlobalObject(player);
	}

}
