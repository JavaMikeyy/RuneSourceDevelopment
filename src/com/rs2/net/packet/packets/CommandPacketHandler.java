package com.rs2.net.packet.packets;

import java.util.Arrays;

import com.rs2.model.players.Player;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class CommandPacketHandler implements PacketHandler {
	
	public static final int COMMAND = 103;

	@Override
	public void handlePacket(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case COMMAND:
			handleCommand(player, packet);
			break;
		}

	}
	
	private void handleCommand(Player player, Packet packet) {
		String command = packet.getIn().readString();
		String[] split = command.split(" ");
		player.handleCommand(split[0].toLowerCase(), 
				Arrays.copyOfRange(split, 1, split.length));
	}

}
