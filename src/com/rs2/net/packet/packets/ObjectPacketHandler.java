package com.rs2.net.packet.packets;

import com.rs2.model.players.Player;
import com.rs2.model.players.WalkToActions;
import com.rs2.model.players.WalkToActions.Actions;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.plugin.PluginManager;

public class ObjectPacketHandler implements PacketHandler {
	
	public static final int ITEM_ON_OBJECT = 192;
	public static final int FIRST_CLICK = 132;
	public static final int SECOND_CLICK = 252;
	public static final int THIRD_CLICK = 70;
	
	@Override
	public void handlePacket(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case ITEM_ON_OBJECT:
			handleItemOnObject(player, packet);
			break;
		case FIRST_CLICK:
			handleFirstClick(player, packet);
			break;
		case SECOND_CLICK:
			handleSecondClick(player, packet);
			break;
		case THIRD_CLICK:
			handleThirdClick(player, packet);
			break;
		}
	}
	
	private void handleItemOnObject(Player player, Packet packet) {
		packet.getIn().readShort();
		int objectId = packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE);
		int objectY = packet.getIn().readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		packet.getIn().readShort();
		int objectX = packet.getIn().readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		int itemId = packet.getIn().readShort();
		player.setClickId(objectId);
		player.setClickX(objectX);
		player.setClickY(objectY);
		player.setMiscId(itemId);
		WalkToActions.setActions(Actions.ITEM_ON_OBJECT);
		WalkToActions.doActions(player);
	}
	
	private void handleFirstClick(Player player, Packet packet) {
		player.setClickX(packet.getIn().readShort(true, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE));
		player.setClickId(packet.getIn().readShort());
		player.setClickY(packet.getIn().readShort(StreamBuffer.ValueType.A));
		if (!PluginManager.onPacketArrival(player, packet))
			return;
		WalkToActions.setActions(Actions.OBJECT_FIRST_CLICK);
		WalkToActions.doActions(player);
	}
	
	private void handleSecondClick(Player player, Packet packet) {
		player.setClickId(packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE));
		player.setClickY(packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE));
		player.setClickX(packet.getIn().readShort(StreamBuffer.ValueType.A));
		WalkToActions.setActions(Actions.OBJECT_SECOND_CLICK);
		WalkToActions.doActions(player);
	}
	
	private void handleThirdClick(Player player, Packet packet) {
		player.setClickId(packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE));
		player.setClickY(packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE));
		player.setClickX(packet.getIn().readShort(StreamBuffer.ValueType.A));
		WalkToActions.setActions(Actions.OBJECT_THIRD_CLICK);
		WalkToActions.doActions(player);
	}


}
