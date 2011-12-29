package com.rs2.net.packet.packets;

import com.rs2.model.content.Teleportation;
import com.rs2.model.players.BankManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.TradeManager;
import com.rs2.model.players.Player.BankOptions;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager.PacketHandler;
import com.rs2.util.Misc;

public class ButtonPacketHandler implements PacketHandler {
	
	public static final int BUTTON = 185;

	@Override
	public void handlePacket(Player player, Packet packet) {
		handleButton(player, Misc.hexToInt(packet.getIn().readBytes(2)));
	}
	
	private void handleButton(Player player, int buttonId) {
		player.getPrayer().setPrayers(buttonId);
		player.getTeleportation().activateTeleportButton(buttonId);
		player.getEmotes().activateEmoteButton(buttonId);
		player.getCooking().clickingInterfaceButtons(buttonId);
		player.getCrafting().tanningHideButtons(buttonId);
		player.getSkillInterfaces().clickingInterfaceButtons(buttonId);
		player.getDialogue().optionButtons(buttonId);
		player.getQuesting().clickQuestGuide(buttonId);
		player.getGenie().clickLampButton(buttonId);
		player.getMagic().clickingToAutoCast(buttonId);
		switch (buttonId) {
		case 152:
			player.getMovementHandler().setRunToggled(false);
			break;
		case 153:
			player.getMovementHandler().setRunToggled(true);
			break;
		case 150:
			player.setAutoRetaliate(true);
			break;
		case 151:
			player.setAutoRetaliate(false);
			break;
		case 3138:
			player.setScreenBrightness(1);
			break;
		case 3140:
			player.setScreenBrightness(2);
			break;
		case 3142:
			player.setScreenBrightness(3);
			break;
		case 3144:
			player.setScreenBrightness(4);
			break;
		case 3146://setMouseButtons (0/1)
			player.setMouseButtons(1);
			break;
		case 3145://setMouseButtons (1/1)
			player.setMouseButtons(0);
			break;
		case 3147:
			player.setChatEffects(0);
			break;
		case 3148:
			player.setChatEffects(1);
			break;
		case 3189:
			player.setSplitPrivateChat(1);
			break;
		case 3190:
			player.setSplitPrivateChat(0);
			break;
		case 48176:
			player.setAcceptAid(1);
			break;
		case 48177:
			player.setAcceptAid(0);
			break;
		case 3162://setMusicVolume (0/4)
			player.setMusicVolume(4);
			break;
		case 3163://setMusicVolume (1/4)
			player.setMusicVolume(3);
			break;
		case 3164://setMusicVolume (2/4)
			player.setMusicVolume(2);
			break;
		case 3165://setMusicVolume (3/4)
			player.setMusicVolume(1);
			break;
		case 3166://setMusicVolume (4/4)
			player.setMusicVolume(0);
			break;
		case 3173://setEffectVolume (0/4)
			player.setEffectVolume(4);
			break;
		case 3174://setEffectVolume (1/4)
			player.setEffectVolume(3);
			break;
		case 3175://setEffectVolume (2/4)
			player.setEffectVolume(2);
			break;
		case 3176://setEffectVolume (3/4)
			player.setEffectVolume(1);
			break;
		case 3177://setEffectVolume (4/4)
			player.setEffectVolume(0);
			break;
		case 13218:
			TradeManager.acceptStageTwo(player);
			break;
		case 13092:
			TradeManager.acceptStageOne(player);
			break;
		case 14067:
			player.setAppearanceUpdateRequired(true);
			player.getUpdateFlags().setUpdateRequired(true);
			player.getActionSender().removeInterfaces();
			break;
		case 9154:
			if (player.getCombatTimer() > 0) {
				player.getActionSender().sendMessage("You can't logout while in combat!");
				return;
			}
			if (player.getPets().getPet() != null)
				player.getPets().unregisterPet();
			player.getActionSender().sendLogout();
			break;
		case 74214:
			player.getMovementHandler().setRunToggled(!player.getMovementHandler().isRunToggled());
			break;
		case 21010:
			player.setWithdrawAsNote(true);
			break;
		case 21011:
			player.setWithdrawAsNote(false);
			break;
		case 31194:
			player.setBankOptions(BankOptions.SWAP_ITEM);
			break;
		case 31195:
			player.setBankOptions(BankOptions.INSERT_ITEM);
			break;
		default:
			System.out.println("Unhandled button: " + buttonId);
			break;
		}
	}
}
