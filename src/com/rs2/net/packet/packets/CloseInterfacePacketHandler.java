package com.rs2.net.packet.packets;

import com.rs2.model.players.Player;
import com.rs2.model.players.TradeManager;
import com.rs2.model.players.Player.TradeStage;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class CloseInterfacePacketHandler implements PacketHandler {
	
	public static final int CLOSE_INTERFACE = 130;

	@Override
	public void handlePacket(Player player, Packet packet) {
		if (!player.getTradeStage().equals(TradeStage.WAITING)) {
			TradeManager.declineTrade(player);
		}
		player.getAttributes().put("isBanking", Boolean.FALSE);
		player.getAttributes().put("isShopping", Boolean.FALSE);
	}

}
