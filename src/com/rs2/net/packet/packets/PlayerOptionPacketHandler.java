package com.rs2.net.packet.packets;

import com.rs2.model.World;
import com.rs2.model.players.Player;
import com.rs2.model.players.TradeManager;
import com.rs2.model.players.Player.TradeStage;
import com.rs2.model.content.combat.magic.Magic;
import com.rs2.model.tick.Tick;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;

public class PlayerOptionPacketHandler implements PacketHandler {
	
	public static final int TRADE = 153;
	public static final int FOLLOW = 128;
	public static final int ATTACK = 73;
	public static final int TRADE_ANSWER = 139;
	public static final int MAGIC_ON_PLAYER = 249;

	@Override
	public void handlePacket(Player player, Packet packet) {
		switch (packet.getOpcode()) {
		case TRADE:
			handleTrade(player, packet);
			break;
		case FOLLOW:
			handleFollow(player, packet);
			break;
		case ATTACK:
			handleAttack(player, packet);
			break;
		case TRADE_ANSWER:
			handleTradeAnswer(player, packet);
			break;
		case MAGIC_ON_PLAYER:
			handleMagicOnPlayer(player, packet);
			break;
		}
	}
	
	private void handleTrade(final Player player, final Packet packet) {
		final int otherPlayerId = packet.getIn().readShort(true, 
				StreamBuffer.ByteOrder.LITTLE);
		final Player otherPlayer = World.getPlayers()[otherPlayerId];
		player.setClickId(otherPlayerId);
		player.getUpdateFlags().faceEntity(otherPlayerId + 32768);
		World.submit(new Tick(1) {
			@Override
			public void execute() {
				if (!player.getMovementHandler().walkToAction(otherPlayer.getPosition(), 1)) {
					return;
				}
				TradeManager.handleTradeRequest(player, otherPlayer);
				this.stop();
			}
		});
	}
	
	private void handleTradeAnswer(final Player player, final Packet packet) {
		final int otherPlayerId = packet.getIn().readShort(true, 
				StreamBuffer.ByteOrder.LITTLE);
		final Player otherPlayer = World.getPlayers()[otherPlayerId];
		boolean canRequest = false;
		if (otherPlayer.getTradeStage().equals(TradeStage.SEND_REQUEST_ACCEPT) || 
				otherPlayer.getTradeStage().equals(TradeStage.ACCEPT) || 
				otherPlayer.getTradeStage().equals(TradeStage.SECOND_TRADE_WINDOW)) {
			canRequest = false;
			return;
		} else {
			canRequest = true;
		}
		if (canRequest) {
			player.setClickId(otherPlayerId);
			player.getUpdateFlags().faceEntity(otherPlayerId + 32768);
			World.submit(new Tick(1) {
				@Override
				public void execute() {
					TradeManager.handleTradeRequest(player, otherPlayer);
					this.stop();
				}
			});
		}
	}
	
	private void handleFollow(Player player, Packet packet) {
		player.getMovementHandler().reset();
		int playerToFollow = packet.getIn().readShort();
		Player leader = World.getPlayers()[playerToFollow];
		player.setFollowingEntity(leader);
	}
	
	private void handleAttack(Player player, Packet packet) {
		player.getMovementHandler().reset();
		Player otherPlayer = World.getPlayers()[packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE)];
		player.setInstigatingAttack(true);
		player.setFollowingEntity(otherPlayer);
		player.setTarget(otherPlayer);
	}
	
	private void handleMagicOnPlayer(Player player, Packet packet) {
		player.getMovementHandler().reset();
		Player otherPlayer = World.getPlayers()[packet.getIn().readShort(true, StreamBuffer.ValueType.A)];
		int magicId = packet.getIn().readShort(true, StreamBuffer.ByteOrder.LITTLE);
		player.getActionSender().sendMessage("Magic id: " + magicId);
		player.getMagic().calculateAttackWithMagic(player, otherPlayer, magicId, Magic.MagicTypes.SINGLE_ATTACK);
	}
}




