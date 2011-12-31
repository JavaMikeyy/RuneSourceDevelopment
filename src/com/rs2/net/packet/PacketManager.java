package com.rs2.net.packet;

import com.rs2.Constants;
import com.rs2.model.players.Player;
import com.rs2.model.players.Player.LoginStages;
import com.rs2.net.packet.packets.AppearancePacketHandler;
import com.rs2.net.packet.packets.ButtonPacketHandler;
import com.rs2.net.packet.packets.ChatInterfacePacketHandler;
import com.rs2.net.packet.packets.ChatPacketHandler;
import com.rs2.net.packet.packets.CloseInterfacePacketHandler;
import com.rs2.net.packet.packets.CommandPacketHandler;
import com.rs2.net.packet.packets.DefaultPacketHandler;
import com.rs2.net.packet.packets.IdleLogoutPacketHandler;
import com.rs2.net.packet.packets.ItemPacketHandler;
import com.rs2.net.packet.packets.LoadRegionPacketHandler;
import com.rs2.net.packet.packets.NpcPacketHandler;
import com.rs2.net.packet.packets.ObjectPacketHandler;
import com.rs2.net.packet.packets.PlayerOptionPacketHandler;
import com.rs2.net.packet.packets.PrivateMessagingPacketHandler;
import com.rs2.net.packet.packets.WalkPacketHandler;

public class PacketManager {
	
	public static final int SIZE = 256;
	
	private static PacketHandler[] packets = new PacketHandler[SIZE];
	
	private static DefaultPacketHandler silent = new DefaultPacketHandler();
	private static WalkPacketHandler walking = new WalkPacketHandler();
	private static ObjectPacketHandler object = new ObjectPacketHandler();
	private static ItemPacketHandler item = new ItemPacketHandler();
	private static ChatInterfacePacketHandler chatInterface = new ChatInterfacePacketHandler();
	private static PrivateMessagingPacketHandler pm = new PrivateMessagingPacketHandler();
	private static NpcPacketHandler npc = new NpcPacketHandler();
	private static PlayerOptionPacketHandler playerOption = new PlayerOptionPacketHandler();
	
	public static void loadPackets() {
		System.out.println("Loading packets...");
		packets[WalkPacketHandler.MINI_MAP_WALK] = walking;
		packets[WalkPacketHandler.MAIN_WALK] = walking;
		packets[WalkPacketHandler.OTHER_WALK] = walking;
		packets[ObjectPacketHandler.ITEM_ON_OBJECT] = object;
		packets[ObjectPacketHandler.FIRST_CLICK] = object;
		packets[ObjectPacketHandler.SECOND_CLICK] = object;
		packets[ObjectPacketHandler.THIRD_CLICK] = object;
		packets[ItemPacketHandler.ITEM_OPERATE] = item;
		packets[ItemPacketHandler.DROP_ITEM] = item;
		packets[ItemPacketHandler.PICKUP_ITEM] = item;
		packets[ItemPacketHandler.HANDLE_OPTIONS] = item;
		packets[ItemPacketHandler.PACKET_145] = item;
		packets[ItemPacketHandler.PACKET_117] = item;
		packets[ItemPacketHandler.PACKET_43] = item;
		packets[ItemPacketHandler.PACKET_129] = item;
		packets[ItemPacketHandler.EQUIP_ITEM] = item;
		packets[ItemPacketHandler.USE_ITEM_ON_ITEM] = item;
		packets[ItemPacketHandler.FIRST_CLICK_ITEM] = item;
		packets[ItemPacketHandler.THIRD_CLICK_ITEM] = item;
		packets[LoadRegionPacketHandler.LOAD_REGION] = new LoadRegionPacketHandler();
		packets[AppearancePacketHandler.APPEARANCE] = new AppearancePacketHandler();
		packets[CommandPacketHandler.COMMAND] = new CommandPacketHandler();
		packets[IdleLogoutPacketHandler.IDLELOGOUT] = new IdleLogoutPacketHandler();
		packets[PrivateMessagingPacketHandler.ADD_FRIEND] = pm;
		packets[PrivateMessagingPacketHandler.REMOVE_FRIEND] = pm;
		packets[PrivateMessagingPacketHandler.ADD_IGNORE] = pm;
		packets[PrivateMessagingPacketHandler.REMOVE_IGNORE] = pm;
		packets[PrivateMessagingPacketHandler.SEND_PM] = pm;
		packets[NpcPacketHandler.FIRST_CLICK] = npc;
		packets[NpcPacketHandler.SECOND_CLICK] = npc;
		packets[NpcPacketHandler.ATTACK] = npc;
		packets[NpcPacketHandler.MAGIC_ON_NPC] = npc;
		packets[ChatInterfacePacketHandler.DIALOGUE] = chatInterface;
		packets[ChatInterfacePacketHandler.SHOW_ENTER_X] = chatInterface;
		packets[ChatInterfacePacketHandler.ENTER_X] = chatInterface;
		packets[ButtonPacketHandler.BUTTON] = new ButtonPacketHandler();
		packets[ChatPacketHandler.CHAT] = new ChatPacketHandler();
		packets[PlayerOptionPacketHandler.TRADE] = playerOption;
		packets[PlayerOptionPacketHandler.FOLLOW] = playerOption;
		packets[PlayerOptionPacketHandler.ATTACK] = playerOption;
		packets[PlayerOptionPacketHandler.TRADE_ANSWER] = playerOption;
		packets[PlayerOptionPacketHandler.MAGIC_ON_PLAYER] = playerOption;
		packets[CloseInterfacePacketHandler.CLOSE_INTERFACE] = new CloseInterfacePacketHandler();
		packets[0] = silent;
		packets[241] = silent;
		packets[86]  = silent;
		packets[3] = silent;
		packets[77] = silent;
		packets[210] = silent;
		packets[78] = silent;
		packets[226] = silent;
		int count = 0;
		for (int i = 0; i < packets.length; i ++) {
			if (packets[i] != null) {
				count ++;
			}
		}
		System.out.println("Loaded " + count + " packets.");
	}
	
	public static void handlePacket(Player player, Packet packet) {
		PacketHandler packetHandler = packets[packet.getOpcode()];
		if (packetHandler == null) {
			System.out.println("Unhandled packet opcode = " + packet.getOpcode() + " length = " + packet.getPacketLength());
			return;
		}
		try {
			packetHandler.handlePacket(player, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final void handleIncomingData(Player player) {
		try {
			// Read the incoming data.
			if (player.getSocketChannel().read(player.getInData()) == -1) {
				player.disconnect();
				return;
			}

			// Handle the received data.
			player.getInData().flip();
			while (player.getInData().hasRemaining()) {

				// Handle login if we need to.
				if (player.getLoginStage() != LoginStages.LOGGED_IN) {
					player.getLogin().handleLogin(player, player.getInData());
					break;
				}

				// Decode the packet opcode.
				if (player.getOpcode() == -1) {
					player.setOpcode(player.getInData().get() & 0xff);
					player.setOpcode(player.getOpcode() - player.getDecryptor().
							getNextValue() & 0xff);
				}

				// Decode the packet length.
				if (player.getPacketLength() == -1) {
					player.setPacketLength(Constants.PACKET_LENGTHS[player.getOpcode()]);
					if (player.getPacketLength() == -1) {
						if (!player.getInData().hasRemaining()) {
							player.getInData().flip();
							player.getInData().compact();
							break;
						}
						player.setPacketLength(player.getInData().get() & 0xff);
					}
				}

				// Decode the packet payload.
				if (player.getInData().remaining() >= player.getPacketLength()) {
					player.handlePacket();

					// Reset for the next packet.
					player.setOpcode(-1);
					player.setPacketLength(-1);
				} else {
					player.getInData().flip();
					player.getInData().compact();
					break;
				}
			}

			// Clear everything for the next read.
			player.getInData().clear();
		} catch (Exception ex) {
			ex.printStackTrace();
			player.disconnect();
		}
	}

	public static void setPackets(PacketHandler[] packets) {
		PacketManager.packets = packets;
	}

	public static PacketHandler[] getPackets() {
		return packets;
	}

	public static void setWalking(WalkPacketHandler walking) {
		PacketManager.walking = walking;
	}

	public static WalkPacketHandler getWalking() {
		return walking;
	}
	
	public static void setItem(ItemPacketHandler item) {
		PacketManager.item = item;
	}

	public static ItemPacketHandler getItem() {
		return item;
	}

	public static void setChatInterface(ChatInterfacePacketHandler chatInterface) {
		PacketManager.chatInterface = chatInterface;
	}

	public static ChatInterfacePacketHandler getChatInterface() {
		return chatInterface;
	}

	public static void setPm(PrivateMessagingPacketHandler pm) {
		PacketManager.pm = pm;
	}

	public static PrivateMessagingPacketHandler getPm() {
		return pm;
	}

	public static void setNpc(NpcPacketHandler npc) {
		PacketManager.npc = npc;
	}

	public static NpcPacketHandler getNpc() {
		return npc;
	}

	public interface PacketHandler {
		public void handlePacket(Player player, Packet packet);
	}

}
