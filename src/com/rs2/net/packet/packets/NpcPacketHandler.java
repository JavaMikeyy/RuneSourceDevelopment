package com.rs2.net.packet.packets;

import com.rs2.model.World;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.Player;
import com.rs2.model.players.WalkToActions;
import com.rs2.model.players.WalkToActions.Actions;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.Misc;

public class NpcPacketHandler implements PacketHandler {
	
	public static final int FIRST_CLICK = 155;
	public static final int SECOND_CLICK = 17;
	public static final int ATTACK = 72;
	public static final int MAGIC_ON_NPC = 131;

	@Override
	public void handlePacket(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case FIRST_CLICK:
			handleFirstClick(player, packet);
			break;
		case SECOND_CLICK:
			handleSecondClick(player, packet);
			break;
		case ATTACK:
			handleAttack(player, packet);
			break;
		case MAGIC_ON_NPC:
			handleMagicOnNpc(player, packet);
			break;
		}
	}
	
	private void handleFirstClick(Player player, Packet packet) {
		int npcSlot = packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE);
		Npc npc = World.getNpcs()[npcSlot];
		player.setClickId(npc.getNpcId());
		player.setClickX(npc.getPosition().getX());
		player.setClickY(npc.getPosition().getY());
		player.getUpdateFlags().faceEntity(npcSlot);
		npc.getUpdateFlags().faceEntity(player.getIndex() + 32768);
		WalkToActions.setActions(Actions.NPC_FIRST_CLICK);
		WalkToActions.doActions(player);
	}
	
	private void handleSecondClick(Player player, Packet packet) {
		int npcSlot = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE) & 0xFFFF;
		Npc npc = World.getNpcs()[npcSlot];
		player.setClickId(npc.getNpcId());
		player.setClickX(npc.getPosition().getX());
		player.setClickY(npc.getPosition().getY());
		player.getUpdateFlags().faceEntity(npcSlot);
		npc.getUpdateFlags().faceEntity(player.getIndex() + 32768);
		WalkToActions.setActions(Actions.NPC_SECOND_CLICK);
		WalkToActions.doActions(player);
	}
	
	private void handleAttack(final Player player, Packet packet) {
		int npcSlot = packet.getIn().readShort(StreamBuffer.ValueType.A);
		final Npc npc = World.getNpcs()[npcSlot];
		player.setClickId(npc.getNpcId());
		player.setClickX(npc.getPosition().getX());
		player.setClickY(npc.getPosition().getY());
		player.setTarget(npc);
		player.setInstigatingAttack(true);
	}
	
	private void handleMagicOnNpc(final Player player, Packet packet) {
		int npcSlot = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		int magicId = packet.getIn().readShort(StreamBuffer.ValueType.A);
		final Npc npc = World.getNpcs()[npcSlot];
	}
	
}
