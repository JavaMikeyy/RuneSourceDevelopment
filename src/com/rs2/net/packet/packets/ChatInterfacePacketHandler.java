package com.rs2.net.packet.packets;

import com.rs2.model.players.BankManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.TradeManager;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class ChatInterfacePacketHandler implements PacketHandler {
	
	public static final int DIALOGUE = 40;
	public static final int SHOW_ENTER_X = 135;
	public static final int ENTER_X = 208;

	@Override
	public void handlePacket(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case DIALOGUE:
			handleDialogue(player, packet);
			break;
		case SHOW_ENTER_X:
			showEnterX(player, packet);
			break;
		case ENTER_X:
			handleEnterX(player, packet);
			break;
		}
	}
	
	private void handleDialogue(Player player, Packet packet) {
		int nextDialogue = player.getDialogue().getNextDialogue();
		if(nextDialogue > 0) {
			player.getDialogue().sendDialogue(nextDialogue);
		} else {
			player.getActionSender().removeInterfaces();
		}
	}
	
	private void showEnterX(Player player, Packet packet) {
		player.setEnterXSlot(packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE));
		player.setEnterXInterfaceId(packet.getIn().readShort(StreamBuffer.ValueType.A));
		player.setEnterXId(packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE));
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 27);
		player.send(out.getBuffer());
	}
	
	private void handleEnterX(Player player, Packet packet) {
		int amount = packet.getIn().readInt();
		if (player.getEnterXInterfaceId() == 5064) {
			BankManager.bankItem(player, player.getEnterXSlot(), player.getEnterXId(), amount);
		} else if (player.getEnterXInterfaceId() == 5382) {
			BankManager.withdrawItem(player, player.getEnterXSlot(), player.getEnterXId(), amount);
		}  else if (player.getEnterXInterfaceId() == 3322) {
			TradeManager.offerItem(player, player.getEnterXSlot(), player.getEnterXId(), amount);
		} else if(player.getEnterXInterfaceId() == 3415) {
			TradeManager.removeTradeItem(player, player.getEnterXSlot(), player.getEnterXId(), amount);
		}
	}

}
