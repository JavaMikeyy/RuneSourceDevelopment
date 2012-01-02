package com.rs2.net;

import com.rs2.Constants;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.players.GroundItem;
import com.rs2.model.players.Item;
import com.rs2.model.players.Player;
import com.rs2.model.players.GlobalObject;
import com.rs2.model.players.Player.BankOptions;

public class ActionSender {

	private Player player;

	public ActionSender(Player player) {
		this.player = player;
	}

	public ActionSender sendLogin() {
		sendDetails();
		sendPacket107();
		sendMapRegion();
		int[] sidebars = {2423, 3917, 638, 3213, 1644, 5608, 0, -1, 5065, 5715, 
				2449, 904, 147, 962};
		for (int i = 0; i < sidebars.length; i ++) {
			sendSidebarInterface(i, sidebars[i]);
		}
		if (player.getMagicBookType() == Player.MagicBookTypes.MODERN) {
			sendSidebarInterface(6, 1151);
		}
		else {
			sendSidebarInterface(6, 12855);
		}
		sendEnergy();
		sendWeight();
		sendMessage("Welcome to " + Constants.SERVER_NAME + ".");
		player.getPrivateMessaging().sendPMOnLogin();
		return this;
	}

	public ActionSender sendConfigsOnLogin() {
		resetAutoCastInterface();
		sendConfig(166, player.getScreenBrightness());//screenBrightness
		sendConfig(170, player.getMouseButtons());//mouseButtons
		sendConfig(171, player.getChatEffects());//chatEffects
		sendConfig(287, player.getSplitPrivateChat());//splitPrivateChat
		sendConfig(427, player.getAcceptAid());//acceptAid
		sendConfig(168, player.getMusicVolume());//musicVolume
		sendConfig(169, player.getEffectVolume());//effectVolume
		sendConfig(304, player.getBankOptions().equals(BankOptions.SWAP_ITEM) ? 0 : 1);
		sendConfig(115, player.isWithdrawAsNote() ? 1 : 0);
		sendConfig(173, 0);
		return this;
	}

	public ActionSender sendSkill(int skillID, int level, double exp) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 134);
		out.writeByte(skillID);
		out.writeInt((int) exp, StreamBuffer.ByteOrder.MIDDLE);
		out.writeByte(level);
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender updateFlashingSideIcon(int tabId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 152);
		out.writeByte(tabId);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendUpdateItem(int slot, int inventoryId, Item item) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(32);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 34);
		out.writeShort(1688);
		out.writeByte(slot);
		out.writeShort(item.getId() + 1);
		if (item.getCount() > 254) {
			out.writeByte(255);
			out.writeShort(item.getCount());
		} else {
			out.writeByte(item.getCount());
		}
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendUpdateItems(int inventoryId, Item[] items) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2048);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 53);
		out.writeShort(inventoryId);
		out.writeShort(items.length);
		for (Item item : items) {
			if (item != null) {
				if (item.getCount() > 254) {
					out.writeByte(255);
					out.writeInt(item.getCount(), StreamBuffer.ByteOrder.INVERSE_MIDDLE);
				} else {
					out.writeByte(item.getCount());
				}
				out.writeShort(item.getId() + 1, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			} else {
				out.writeByte(0);
				out.writeShort(0, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
			}
		}
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendObject(GlobalObject object) {
		sendCoords(object.getPosition());
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 151);
		out.writeByte(0, StreamBuffer.ValueType.S);
		out.writeShort(object.getId(), StreamBuffer.ByteOrder.LITTLE);
		out.writeByte(((object.getType() << 2) + (object.getFace() & 3)),
				StreamBuffer.ValueType.S);
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender sendMessage(String message) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(message.length() + 3);
		out.writeVariablePacketHeader(player.getEncryptor(), 253);
		out.writeString(message);
		out.finishVariablePacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendSidebarInterface(int menuId, int form) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 71);
		out.writeShort(form);
		out.writeByte(menuId, StreamBuffer.ValueType.A);
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender sendProjectile(Position position, int offsetX, int offsetY, int id, int startHeight, int endHeight, int speed, int lockon) {
		sendCoordinates2(position);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(16);
		out.writeHeader(player.getEncryptor(), 117);
		out.writeByte(50);
		out.writeByte(offsetY);
		out.writeByte(offsetX);
		out.writeShort(lockon);
		out.writeShort(id);
		out.writeByte(startHeight);
		out.writeByte(endHeight);
		out.writeShort(51);
		out.writeShort(speed);
		out.writeByte(16);
		out.writeByte(64);
		player.send(out.getBuffer());
		return this;
	}
	
	public ActionSender sendCoordinates2(Position position){
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 85);
		int y = position.getY() - player.getCurrentRegion().getRegionY() * 8 - 2;
		int x = position.getX() - player.getCurrentRegion().getRegionX() * 8 - 3;
		out.writeByte(y, StreamBuffer.ValueType.C);
		out.writeByte(x, StreamBuffer.ValueType.C);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendMapRegion() {
		player.getCurrentRegion().setAs(player.getPosition());
		player.setNeedsPlacement(true);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 73);
		out.writeShort(player.getPosition().getRegionX() + 6, StreamBuffer.ValueType.A);
		out.writeShort(player.getPosition().getRegionY() + 6);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendLogout() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeHeader(player.getEncryptor(), 109);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendInterface(int id) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 97);
		out.writeShort(id);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendWalkableInterface(int id) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 208);
		out.writeShort(id, StreamBuffer.ByteOrder.BIG);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendInterface(int interfaceId, int inventoryId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 248);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A);
		out.writeShort(inventoryId);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender removeInterfaces() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeHeader(player.getEncryptor(), 219);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendCoords(Position position){
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 85);
		int y = position.getY() - 8 * player.getCurrentRegion().getRegionY();
		int x = position.getX() - 8 * player.getCurrentRegion().getRegionX();
		out.writeByte(y, StreamBuffer.ValueType.C);
		out.writeByte(x, StreamBuffer.ValueType.C);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendGroundItem(GroundItem groundItem) {
		sendCoords(groundItem.getPos());
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(6);
		out.writeHeader(player.getEncryptor(), 44);
		out.writeShort(groundItem.getItem().getId(), StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(groundItem.getItem().getCount());
		out.writeByte(0);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender removeGroundItem(GroundItem groundItem) {
		sendCoords(groundItem.getPos());
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 156);
		out.writeByte(0, StreamBuffer.ValueType.S);
		out.writeShort(groundItem.getItem().getId());
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendConfig(int id, int value) {
		if (value < 128) {
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
			out.writeHeader(player.getEncryptor(), 36);
			out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
			out.writeByte(value);
			player.send(out.getBuffer());
		} else {
			StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
			out.writeHeader(player.getEncryptor(), 87);
			out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
			out.writeInt(value, StreamBuffer.ByteOrder.MIDDLE);
			player.send(out.getBuffer());
		}
		return this;
	}

	public ActionSender sendString(String message, int interfaceId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(message.length() + 6);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 126);
		out.writeString(message);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A);
		out.finishVariableShortPacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendFriendList(long name, int world) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(10);
		out.writeHeader(player.getEncryptor(), 50);
		if (world != 0) {
			world += 9;
		}
		out.writeLong(name);
		out.writeByte(world);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendPMServer(int state) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 221);
		out.writeByte(state);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendItemOnInterface(int id, int zoom, int model) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 246);
		out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(zoom);
		out.writeShort(model);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendStillGraphic(int graphicId, Position pos, int delay) {
		sendCoords(pos);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		out.writeHeader(player.getEncryptor(), 4);
		out.writeByte(0);
		out.writeShort(graphicId);
		out.writeByte(pos.getZ());
		out.writeShort(delay);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendPrivateMessage(long name, int rights, byte[] message, int messageSize) {
		//TODO: FIXME
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2048);
		out.writeVariablePacketHeader(player.getEncryptor(), 196);
		out.writeLong(name);
		out.writeInt(player.getPrivateMessaging().getLastPrivateMessageId());
		out.writeByte(rights);
		out.writeBytes(message, messageSize);
		out.finishVariablePacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendChatInterface(int id) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 164);
		out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendDialogueAnimation(int animId, int interfaceId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 200);
		out.writeShort(animId);
		out.writeShort(interfaceId);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendPlayerDialogueHead(int interfaceId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 185);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendNPCDialogueHead(int npcId, int interfaceId) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 75);
		out.writeShort(npcId, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		out.writeShort(interfaceId, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendSound(int id, int tone, int delay) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(6);
		out.writeHeader(player.getEncryptor(), 174);
		out.writeShort(id);
		out.writeByte(tone);
		out.writeShort(delay);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendSong(int id) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 74);
		out.writeShort(id, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendPlayerOption(String option, int slot) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(option.length() + 6);
		out.writeVariablePacketHeader(player.getEncryptor(), 104);
		out.writeByte(slot, StreamBuffer.ValueType.C);
		out.writeByte(0, StreamBuffer.ValueType.A);
		out.writeString(option);
		out.finishVariablePacketHeader();
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendDetails() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 249);
		out.writeByte(1, StreamBuffer.ValueType.A);
		out.writeShort(player.getIndex(), StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendEnergy() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 110);
		out.writeByte(player.getEnergy());
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendWeight() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(3);
		out.writeHeader(player.getEncryptor(), 240);
		out.writeShort(player.getWeight());
		player.send(out.getBuffer());
		return this;
	}

	public ActionSender sendPacket107() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 107);
		player.send(out.getBuffer());
		return this;
	}
	
	public void updateAutoCastInterface(int spellIndex) {
		String spellName = player.getMagic().getSpellDefinitions()[spellIndex].getSpellName();
		sendString(spellName, 352);
		sendConfig(108, 3);
		sendConfig(43, 3);
	}
	
	public void resetAutoCastInterface() {
		if (player.getMagic().autoCast == null && player.getMagic().autoCastChange == null) {
			sendConfig(108, 0);
			sendConfig(43, 0);
			return;
		}
		sendConfig(43, 0);
		sendConfig(108, 2);
	}
	
}
