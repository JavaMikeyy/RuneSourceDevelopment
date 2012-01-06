package com.rs2.util;

/*
 * This file is part of RuneSource.
 *
 * RuneSource is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RuneSource is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RuneSource.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.rs2.model.players.BankManager;
import com.rs2.model.players.Item;
import com.rs2.model.players.Player;


/**
 * Static utility methods for saving and loading players.
 * 
 * @author blakeman8192
 */
public class PlayerSave {

	/** The directory where players are saved. */
	public static final String directory = "./data/characters/";

	/**
	 * Saves the player.
	 * 
	 * @param player
	 *            the player to save
	 * @return
	 */
	public static void save(Player player) throws Exception {
		File file = new File(directory + player.getUsername() + ".dat");
		if (!file.exists()) {
			file.createNewFile();
		} else {
			file.delete();
		}
		FileOutputStream outFile = new FileOutputStream(file);
		DataOutputStream write = new DataOutputStream(outFile);
		write.writeUTF(player.getUsername());
		write.writeUTF(player.getPassword());
		write.writeInt(player.getStaffRights());
		write.writeInt(player.getPosition().getX());
		write.writeInt(player.getPosition().getY());
		write.writeInt(player.getPosition().getZ());
		write.writeInt(player.getGender());
		write.writeBoolean(player.isHasDesigned());
		write.writeInt(player.getScreenBrightness());
		write.writeInt(player.getMouseButtons());
		write.writeInt(player.getChatEffects());
		write.writeInt(player.getSplitPrivateChat());
		write.writeInt(player.getAcceptAid());
		write.writeInt(player.getMusicVolume());
		write.writeInt(player.getEffectVolume());
		write.writeInt(player.getQuestPoints());
		write.writeUTF((String) player.getSlayerTask()[0]);
		write.writeInt((Integer) player.getSlayerTask()[1]);
		for (int i = 0; i < player.getQuesting().questData.length; i ++) {
			write.writeInt((Integer) player.getQuesting().questData[i][1]);
		}
		for (int i = 0; i < 4; i ++) {
			write.writeInt((Integer) player.getRunecrafting().getPouchData(i));
		}
		for (int i = 0; i < player.getAppearance().length; i ++) {
			write.writeInt(player.getAppearance()[i]);
		}
		for (int i = 0; i < player.getColors().length; i ++) {
			write.writeInt(player.getColors()[i]);
		}
		for (int i = 0; i < player.getSkill().getLevel().length; i ++) {
			write.writeInt(player.getSkill().getLevel()[i]);
		}
		for (int i = 0; i < player.getSkill().getExp().length; i ++) {
			write.writeInt((int) player.getSkill().getExp()[i]);
		}
		for (int i = 0; i < 28; i ++) {
			Item item = player.getInventory().getItemContainer().get(i);
			if (item == null) {
				write.writeInt(65535);
			} else {
				write.writeInt(item.getId());
				write.writeInt(item.getCount());
			}
		}
		for (int i = 0; i < 14; i ++) {
			Item item = player.getEquipment().getItemContainer().get(i);
			if (item == null) {
				write.writeInt(65535);
			} else {
				write.writeInt(item.getId());
				write.writeInt(item.getCount());
			}
		}
		for (int i = 0; i < BankManager.SIZE; i ++) {
			Item item = player.getBank().get(i);
			if (item == null) {
				write.writeInt(65535);
			} else {
				write.writeInt(item.getId());
				write.writeInt(item.getCount());
			}
		}
		for (int i = 0; i < player.getFriends().length; i ++) {
			write.writeLong(player.getFriends()[i]);
		}
		for (int i = 0; i < player.getIgnores().length; i ++) {
			write.writeLong(player.getIgnores()[i]);
		}
		for (int i = 0; i < player.getPendingItems().length; i ++) {
			write.writeInt(player.getPendingItems()[i]);
			write.writeInt(player.getPendingItemsAmount()[i]);
		}
	}

	/**
	 * Loads the player (and sets the loaded attributes).
	 * 
	 * @param player
	 *            the player to load.
	 * @return 0 for success, 1 if the player does not have a saved game, 2 for
	 *         invalid username/password
	 */
	public static void load(Player player) throws Exception {
		File file = new File(directory + player.getUsername() + ".dat");
		if (!file.exists()) {
			return;
		}
		FileInputStream inFile = new FileInputStream(file);
		DataInputStream load = new DataInputStream(inFile);
		player.setUsername(load.readUTF());
		String password = load.readUTF();
		player.setPassword(password);
		player.setStaffRights(load.readInt());
		player.getPosition().setX(load.readInt());
		player.getPosition().setY(load.readInt());
		player.getPosition().setZ(load.readInt());
		player.setGender(load.readInt());
		player.setHasDesigned(load.readBoolean());
		player.setScreenBrightness(load.readInt());
		player.setMouseButtons(load.readInt());
		player.setChatEffects(load.readInt());
		player.setSplitPrivateChat(load.readInt());
		player.setAcceptAid(load.readInt());
		player.setMusicVolume(load.readInt());
		player.setEffectVolume(load.readInt());
		player.setQuestPoints(load.readInt());
		Object[] slayerTask = {load.readUTF(), load.readInt()};
		player.setSlayerTask(slayerTask);
		for (int i = 0; i < player.getQuesting().questData.length; i ++) {
			player.getQuesting().questData[i][1] = load.readInt();
		}
		for (int i = 0; i < 4; i ++) {
			player.getRunecrafting().setPouchData(i, load.readInt());
		}
		for (int i = 0; i < player.getAppearance().length; i++ ) {
			player.getAppearance()[i] = load.readInt();
		}
		for (int i = 0; i < player.getColors().length; i ++) {
			player.getColors()[i] = load.readInt();
		}
		for (int i = 0; i < player.getSkill().getLevel().length; i ++) {
			player.getSkill().getLevel()[i] = load.readInt();
		}
		for (int i = 0; i < player.getSkill().getExp().length; i ++) {
			player.getSkill().getExp()[i] = load.readInt();
		}
		for (int i = 0; i < 28; i ++) {
			int id = load.readInt();
			if (id != 65535) {
				int amount = load.readInt();
				Item item = new Item(id, amount);
				player.getInventory().getItemContainer().set(i, item);
			}
		}
		for (int i = 0; i < 14; i ++) {
			int id = load.readInt();
			if (id != 65535) {
				int amount = load.readInt();
				Item item = new Item(id, amount);
				player.getEquipment().getItemContainer().set(i, item);
			}
		}
		for (int i = 0; i < BankManager.SIZE; i ++) {
			int id = load.readInt();
			if (id != 65535) {
				int amount = load.readInt();
				Item item = new Item(id, amount);
				player.getBank().set(i, item);
			}
		}
		for (int i = 0; i < player.getFriends().length; i ++) {
			player.getFriends()[i] = load.readLong();
		}
		for (int i = 0; i < player.getIgnores().length; i ++) {
			player.getIgnores()[i] = load.readLong();
		}
		for (int i = 0; i < player.getPendingItems().length; i ++) {
			player.getPendingItems()[i] = load.readInt();
			player.getPendingItemsAmount()[i] = load.readInt();
		}
	}

}
