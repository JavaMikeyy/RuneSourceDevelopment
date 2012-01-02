package com.rs2.net.packet.packets;

import com.rs2.model.Position;
import com.rs2.model.players.Player;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.plugin.PluginManager;

public class WalkPacketHandler implements PacketHandler {
	
	public static final int MINI_MAP_WALK = 248;
	public static final int MAIN_WALK = 164;
	public static final int OTHER_WALK = 98;

	@Override
	public void handlePacket(Player player, Packet packet) {
		int length = packet.getPacketLength();
		if (!player.getMovementHandler().canWalk() || player.isDead()) {
			return;
		}
		if (packet.getOpcode() == MINI_MAP_WALK) {
			length -= 14;
		}
		if (packet.getOpcode() != OTHER_WALK) {
			player.setWalkToAction(null);
			player.getSkill().setStopSkillTick(true);
			player.setInstigatingAttack(false);
			player.getFollowing().resetFollow(player);
			PluginManager.reset();
		}
		if (player.isFrozen()) {
			player.getActionSender().sendMessage("You are frozen and cannot move!");
			return;
		}
		int steps = (length - 5) / 2;
		int[][] path = new int[steps][2];
		int firstStepX = packet.getIn().readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		for (int i = 0; i < steps; i++) {
			path[i][0] = packet.getIn().readByte();
			path[i][1] = packet.getIn().readByte();
		}
		int firstStepY = packet.getIn().readShort(StreamBuffer.ByteOrder.LITTLE);
		player.getMovementHandler().reset();
		player.getMovementHandler().setRunPath(packet.getIn().readByte(StreamBuffer.ValueType.C) == 1);
		player.getMovementHandler().addToPath(new Position(firstStepX, firstStepY));
		for (int i = 0; i < steps; i++) {
			path[i][0] += firstStepX;
			path[i][1] += firstStepY;
			player.getMovementHandler().addToPath(new Position(path[i][0], path[i][1]));
		}
		player.getMovementHandler().finish();
		player.getMovementHandler().resetOnWalkPacket();
	}
}
