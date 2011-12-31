package com.rs2.model.players;

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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.rs2.Constants;
import com.rs2.HostGateway;
import com.rs2.Server;
import com.rs2.model.Entity;
import com.rs2.model.Position;
import com.rs2.model.World;
import com.rs2.model.content.*;
import com.rs2.model.content.dialogue.Dialogue;
import com.rs2.model.content.questing.Questing;
import com.rs2.model.content.randomevents.*;
import com.rs2.model.content.combat.*;
import com.rs2.model.content.combat.util.Skulling;
import com.rs2.model.content.combat.magic.Magic;
import com.rs2.model.content.combat.ranged.Ranged;
import com.rs2.model.content.food.Food;
import com.rs2.model.content.skills.*;
import com.rs2.model.npcs.Npc;
import com.rs2.model.players.container.Container;
import com.rs2.model.players.container.Container.Type;
import com.rs2.model.tick.Tick;
import com.rs2.net.ActionSender;
import com.rs2.net.ISAACCipher;
import com.rs2.net.Login;
import com.rs2.net.StreamBuffer;
import com.rs2.net.packet.Packet;
import com.rs2.net.packet.PacketManager;
import com.rs2.util.Areas;
import com.rs2.util.Misc;
import com.rs2.util.PlayerSave;
import com.rs2.util.PunishmentManager;

/**
 * Represents a logged-in player.
 * 
 * @author blakeman8192
 * @author BFMV
 */
public class Player extends Entity {
	
	private final SelectionKey key;
	private final ByteBuffer inData;
	private SocketChannel socketChannel;
	private LoginStages loginStage = LoginStages.CONNECTED;
	private ISAACCipher encryptor;
	private ISAACCipher decryptor;
	private int opcode = -1;
	private int packetLength = -1;
	private String username;
	private String password;
	private int macAddress;
	private final Misc.Stopwatch timeoutStopwatch = new Misc.Stopwatch();
	private final List<Player> players = new LinkedList<Player>();
	private final List<Npc> npcs = new LinkedList<Npc>();
	private MovementHandler movementHandler = new MovementHandler(this);
	private Inventory inventory = new Inventory(this);
	private Equipment equipment = new Equipment(this);
	private PrivateMessaging privateMessaging = new PrivateMessaging(this);
	private Prayer prayer = new Prayer(this);
	private Teleportation teleportation = new Teleportation(this);
	private Emotes emotes = new Emotes(this);
	private Skill skill = new Skill(this);
	private ActionSender actionSender = new ActionSender(this);
	private Combat combat = new Combat(this);
	private Magic magic = new Magic(this);
	private Ranged ranged = new Ranged(this);
	private Food food = new Food(this);
	private Runecrafting runecrafting = new Runecrafting(this);
	private BoneBurying boneBurying = new BoneBurying(this);
	private Herblore herblore = new Herblore(this);
	private Fishing fishing = new Fishing(this);
	private Cooking cooking = new Cooking(this);
	private SkillInterfaces skillInterfaces = new SkillInterfaces(this);
	private SkillResources skillResources = new SkillResources(this);
	private Genie genie = new Genie(this);
	private Pets pets = new Pets(this);
	private Crafting crafting = new Crafting(this);
	private Dialogue dialogue = new Dialogue(this);
	private Questing questing = new Questing(this);
	private Following following = new Following(this);
	private Login login = new Login();
	private Position currentRegion = new Position(0, 0, 0);
	private int primaryDirection = -1;
	private int secondaryDirection = -1;
	private int staffRights = 0;
	private int chatColor;
	private byte[] chatText;
	private int gender = Constants.GENDER_MALE;
	private final int[] appearance = new int[7];
	private final int[] colors = new int[5];
	private Container bank = new Container(Type.ALWAYS_STACK, BankManager.SIZE);
	private Container trade = new Container(Type.STANDARD, Inventory.SIZE);
	private boolean hasDesigned;
	private boolean pickupItem;
	private Tick walkToAction = null;
	private int clickX;
	private int clickY;
	private int clickId;
	private int miscId;
	private boolean withdrawAsNote;
	private int enterXId;
	private int enterXSlot;
	private int enterXInterfaceId;
	private BankOptions bankOptions = BankOptions.SWAP_ITEM;
	private int shopId;
	private boolean isLoggedIn;
	private Map<Integer, Integer> bonuses = new HashMap<Integer, Integer>();
	private long[] friends = new long[200];
	private long[] ignores = new long[100];
	private int currentDialogueId;
	private int currentOptionId;
	private int optionClickId;
	private int currentGloryId;
	private int returnCode = 2;
	private TradeStage tradeStage = TradeStage.WAITING;
	private int[] pendingItems = new int[Inventory.SIZE];
	private int[] pendingItemsAmount = new int[Inventory.SIZE];
	private int energy = 100;
	private int weight = 5;
	private boolean needsPlacement;
	private boolean resetMovementQueue;
	private boolean appearanceUpdateRequired;
	private int prayerIcon = -1;
	private int skullIcon = -1;
	private int skullTimer = -1;
	private boolean[] isUsingPrayer = new boolean[26];
	private int prayerDrainTimer = 6;
	private MagicBookTypes magicBookType = MagicBookTypes.MODERN;
	private boolean autoRetaliate = false;
	private boolean isSkulled = false;
	private int screenBrightness = 2;
	private int mouseButtons = 0;
	private int chatEffects = 1;
	private int splitPrivateChat = 0;
	private int acceptAid = 0;
	private int musicVolume = 0;
	private int effectVolume = 0;
	private int questPoints = 0;
	
	private static Object[][] staff = {
		{"Mopar", 2}
	};
	
	@Override
	public void reset() {
		getUpdateFlags().reset();
		setPrimaryDirection(-1);
		setSecondaryDirection(-1);
		setAppearanceUpdateRequired(false);
		setResetMovementQueue(false);
		setNeedsPlacement(false);
	}
	
	@Override
	public void initAttributes() {
		getAttributes().put("isBanking", Boolean.FALSE);
		getAttributes().put("isShopping", Boolean.FALSE);
		getAttributes().put("usedGlory", Boolean.FALSE);
		getAttributes().put("operate", Boolean.FALSE);
		getAttributes().put("canTeleport", Boolean.TRUE);
		getAttributes().put("canPickup", Boolean.FALSE);
		getAttributes().put("doDamage", Boolean.FALSE);
		getAttributes().put("canRestoreEnergy", Boolean.FALSE);
	}

	public void applyDeath() {
		World.submit(new Tick(3) {
			int tick = 0;
			@Override
			public void execute() {
				if (tick == 0)
					getUpdateFlags().sendAnimation(2304, 0);
				if (tick == 1) {
					applyLife();
					stop();
				}
				tick++;
			}
		});
	}
	
	public void applyLife() {
		//dropitems
		if (getCombatingEntity() instanceof Player) {
			Player otherPlayer = World.getPlayers()[getCombatingEntity().getIndex()];
			otherPlayer.getActionSender().sendMessage("You have defeated " + getUsername() + "!");
			otherPlayer.getUpdateFlags().faceEntity(65535);
		}
		getActionSender().sendMessage("Oh dear, you have died!");
		getCombat().resetCombat(this);
		setCombatTimer(0);
		setFrozen(false);
		sendTeleport(Constants.START_X, Constants.START_Y, Constants.START_Z);
		getUpdateFlags().sendAnimation(65535, 0);
		for (int i = 0; i < getSkill().SKILL_COUNT; i++) {
			skill.getLevel()[i] = skill.getLevelForXP(skill.getExp()[i]);
			skill.refresh(i);
		}
		setDead(false);
	}
	
	@Override
	public void process() {
		// If no packet for more than 5 seconds, disconnect.
		/*if (getTimeoutStopwatch().elapsed() > 5000) {
			System.out.println(this + " timed out.");
			disconnect();
			return;
		}*/
		getCombat().combatTick(this);
		getFollowing().followTick(this);
		movementHandler.process();
		Skulling.skullTick(this);
	}
	
	@Override
	public void hit(int damage, int hitType) {
		if (damage > skill.getLevel()[Skill.HITPOINTS]) {
			damage = skill.getLevel()[Skill.HITPOINTS];
		}
		skill.getLevel()[Skill.HITPOINTS] -= damage;
		if (!getUpdateFlags().isHitUpdate()) {
			getUpdateFlags().setDamage(damage);
			getUpdateFlags().setHitType(hitType);
			getUpdateFlags().setHitUpdate(true);
		} else {
			getUpdateFlags().setDamage2(damage);
			getUpdateFlags().setHitType2(hitType);
			getUpdateFlags().setHitUpdate2(true);
		}
		setDamage(damage);
		setHitType(hitType);
		skill.refresh(Skill.HITPOINTS);
		if (skill.getLevel()[Skill.HITPOINTS] <= 0) {
			setDead(true);
			applyDeath();
		}
	}

	public Player(SelectionKey key) {
		this.key = key;
		inData = ByteBuffer.allocateDirect(512);
		if (key != null) {
			socketChannel = (SocketChannel) key.channel();
		}
		setPosition(new Position(Constants.START_X, Constants.START_Y, 
				Constants.START_Z));
		initAttributes();

		// Set the default appearance.
		getAppearance()[Constants.APPEARANCE_SLOT_CHEST] = 18;
		getAppearance()[Constants.APPEARANCE_SLOT_ARMS] = 26;
		getAppearance()[Constants.APPEARANCE_SLOT_LEGS] = 36;
		getAppearance()[Constants.APPEARANCE_SLOT_HEAD] = 0;
		getAppearance()[Constants.APPEARANCE_SLOT_HANDS] = 33;
		getAppearance()[Constants.APPEARANCE_SLOT_FEET] = 42;
		getAppearance()[Constants.APPEARANCE_SLOT_BEARD] = 10;

		// Set the default colors.
		getColors()[0] = 7;
		getColors()[1] = 8;
		getColors()[2] = 9;
		getColors()[3] = 5;
		getColors()[4] = 0;
		for (int i = 0; i < pendingItems.length; i ++) {
			pendingItems[i] = -1;
			pendingItemsAmount[i] = 0;
		}
	}
	
	public void handlePacket() {
		timeoutStopwatch.reset();
		int positionBefore = inData.position();
		StreamBuffer.InBuffer in = StreamBuffer.newInBuffer(inData);
		PacketManager.handlePacket(this, new Packet(opcode, packetLength, in));
		int read = inData.position() - positionBefore;
		for (int i = read; i < packetLength; i++) {
			inData.get();
		}
	}
	
	public void send(ByteBuffer buffer) {
		// Prepare the buffer for writing.
		buffer.flip();
		
		try {
			// ...and write it!
			//if (this != null)
				socketChannel.write(buffer);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Here we can allow updating of methods who write data to the buffer
	 * This updates all the time and prevents buffer writing bug
	 */
	public void sendTickUpdatesOnLogin() {
		World.submit(new Tick(1) {
			@Override
			public void execute() {
				if (!loginStage.equals(LoginStages.LOGGED_IN)) {
					this.stop();
					return;
				}
				getAttributes().put("canPickup", Boolean.TRUE);
				privateMessaging.refresh();
				/*if (!Areas.isInWildernessArea(getPosition())) {
					getActionSender().sendPlayerOption("Follow", 1);
					getActionSender().sendPlayerOption("Trade with", 2);
					getActionSender().sendPlayerOption("null", 3);
				} else {*/
					getActionSender().sendPlayerOption("Follow", 1);
					getActionSender().sendPlayerOption("Trade with", 2);
					getActionSender().sendPlayerOption("Attack", 3);
				//}
			}
		});
		World.submit(new Tick(2) {
			@Override
			public void execute() {
				if (!loginStage.equals(LoginStages.LOGGED_IN)) {
					this.stop();
					return;
				}
				if (energy < 100 && (Boolean) getAttributes().get("canRestoreEnergy")) {
					energy ++;
				}
				getActionSender().sendEnergy();
			}
		});
	}
	
	public void disconnect() {
		System.out.println(this + " disconnecting.");
		try {
			logout();
			socketChannel.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			HostGateway.exit(getSocketChannel().socket().getInetAddress().getHostAddress());
			Server.getSingleton().getPlayerMap().remove(key);
			key.cancel();
		}
	}

	/**
	  * Adds to the Players position.
	  */
	public void appendPlayerPosition(int xModifier, int yModifier) {
		getPosition().move(xModifier, yModifier);
	}
	
	/**
	  * Tells if player is withinDistance of the clicked object/npc/player
	  */
	
	public boolean withinDistance() {
		if (!getMovementHandler().walkToAction(new Position(getClickX(), getClickY()), 1)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Handles a player command.
	 * 
	 * @param keyword
	 *            the command keyword
	 * @param args
	 *            the arguments (separated by spaces)
	 */
	public void handleCommand(String keyword, String[] args) {
		if (keyword.equals("master")) {
			for (int i = 0; i < skill.getLevel().length; i++) {
				skill.getLevel()[i] = 99;
				skill.getExp()[i] = 200000000;
			}
			skill.refresh();
		}
		if (keyword.equals("item")) {
			int id = Integer.parseInt(args[0]);
			int amount = 1;
			if (args.length > 1) {
				amount = Integer.parseInt(args[1]);
			}
			inventory.addItem(new Item(id, amount));
		}
		if (keyword.equals("tele")) {
			int x = Integer.parseInt(args[0]);
			int y = Integer.parseInt(args[1]);
			teleport(new Position(x, y, getPosition().getZ()));
		}
		if (keyword.equals("mypos")) {
			getActionSender().sendMessage("You are at: " + getPosition());
		}
		if (keyword.equals("battle")) {
			getCombat().attackEntity(World.getNpcs()[1], World.getNpcs()[2]);
			getCombat().attackEntity(World.getNpcs()[2], World.getNpcs()[1]);
		}
		if (keyword.equals("home")) {
			teleport(new Position(Constants.START_X, Constants.START_Y, Constants.START_Z));
		}
		if (keyword.equals("anim")) {
			int animationId = Integer.parseInt(args[0]);
			getUpdateFlags().sendAnimation(animationId, 0);
		}
		if (keyword.equals("gfx")) {
			int gfxId = Integer.parseInt(args[0]);
			getUpdateFlags().sendGraphic(gfxId, 0);
		}
		if (keyword.equals("addxp")) {
			skill.addExp(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		}
		if (keyword.equals("interface")) {
			actionSender.sendInterface(Integer.parseInt(args[0]));
		}
		if (keyword.equals("info")) {
			getDialogue().sendStatement5("Welcome to Azure (RuneSource).", "This has been highly modified.", "If you have any questions,", 
			"Please contact metallic_mike@yahoo.com on MSN.", "I hope you enjoy this server.");
		}
		if (keyword.equals("death")) {
			skill.getLevel()[Skill.HITPOINTS] = 0;
			skill.refresh(Skill.HITPOINTS);
			setDead(true);
			applyDeath();
		}
		if (keyword.equals("max")) {
			for (int i = 0; i < 7; i++)
				skill.addExp(i, 14000000);
		}
		if (keyword.equals("runes")) {
			for (int i = 0; i < ((566 - 554) + 1); i++)
				inventory.addItem(new Item(554 + i, 1000));
				inventory.addItem(new Item(1381, 1));
				inventory.addItem(new Item(4675, 1));
		}
		if (keyword.equals("banuser") && getStaffRights() >= 2) {
			PunishmentManager.appendPunishment(args[0].toLowerCase(), PunishmentManager.Punishments.BAN, true, args[1].toLowerCase());
		}
		if (keyword.equals("unbanuser") && getStaffRights() >= 2) {
			PunishmentManager.appendPunishment(args[0].toLowerCase(), PunishmentManager.Punishments.BAN, false, 0);
		}
		if (keyword.equals("muteuser") && getStaffRights() >= 1) {
			PunishmentManager.appendPunishment(args[0].toLowerCase(), PunishmentManager.Punishments.MUTE, true, args[1].toLowerCase());
		}
		if (keyword.equals("unmuteuser") && getStaffRights() >= 1) {
			PunishmentManager.appendPunishment(args[0].toLowerCase(), PunishmentManager.Punishments.MUTE, false, 0);
		}
		if (keyword.equals("addressbanuser") && getStaffRights() >= 2) {
			PunishmentManager.appendPunishment(args[0].toLowerCase(), PunishmentManager.Punishments.ADDRESS_BAN, true, args[1].toLowerCase());
		}
		if (keyword.equals("unaddressbanuser") && getStaffRights() >= 2) {
			PunishmentManager.appendPunishment(args[0].toLowerCase(), PunishmentManager.Punishments.ADDRESS_BAN, false, 0);
		}
		if (keyword.equals("addressmuteuser") && getStaffRights() >= 1) {
			PunishmentManager.appendPunishment(args[0].toLowerCase(), PunishmentManager.Punishments.ADDRESS_MUTE, true, args[1].toLowerCase());
		}
		if (keyword.equals("unaddressmuteuser") && getStaffRights() >= 1) {
			PunishmentManager.appendPunishment(args[0].toLowerCase(), PunishmentManager.Punishments.ADDRESS_MUTE, false, 0);
		}
		if (keyword.equals("genie")) {
			genie.sendRandom();
		}
		if (keyword.equals("modern")) {
			getActionSender().sendSidebarInterface(6, 1151);
			magicBookType = MagicBookTypes.MODERN;
		}
		if (keyword.equals("ancient")) {
			getActionSender().sendSidebarInterface(6, 12855);
			magicBookType = MagicBookTypes.ANCIENT;
		}
		if (keyword.equals("sound")) {
			actionSender.sendSound(Integer.parseInt(args[0]), 1, 0);
		}
		if (keyword.equals("emptyinv")) {
			int id = Integer.parseInt(args[0]);
			Item item = new Item(id);
			inventory.removeItem(item);
		}
	}

	/**
	 * Teleports the player to the desired position.
	 * 
	 * @param position
	 *            the position
	 */
	public void teleport(Position position) {
		getPosition().setAs(position);
		actionSender.sendMapRegion();
		movementHandler.reset();
		setResetMovementQueue(true);
		getActionSender().sendDetails();
	}
	
	public void sendTeleport(int x, int y, int height) {
		teleport(new Position(x, y, height));
	}
	
	public boolean isBusy() {
		return (Boolean) getAttributes().get("isBanking") 
			|| (Boolean) getAttributes().get("isShopping") || 
				!(Boolean) getAttributes().get("canTeleport");
	}

	public void login() throws Exception {
		int response = getReturnCode();

		// Check if the player is already logged in.
		for (Player player : World.getPlayers()) {
			if (player == null) {
				continue;
			}
			if (player.getUsername().equals(getUsername())) {
				response = Constants.LOGIN_RESPONSE_ACCOUNT_ONLINE;
			}
		}

		for (final Object[] staffMembers : staff) {
			if (getUsername().equalsIgnoreCase((String) staffMembers[0])) {
				staffRights = (Integer) staffMembers[1];
			}
		}

		StreamBuffer.OutBuffer resp = StreamBuffer.newOutBuffer(3);
		resp.writeByte(response);
		resp.writeByte(getStaffRights());
		resp.writeByte(0);
		send(resp.getBuffer());
		if (response != 2) {
			disconnect();
			return;
		}
		World.register(this);
		actionSender.sendLogin().sendConfigsOnLogin();
		refreshOnLogin();
		getQuesting().clearQuestGuide();
		getQuesting().updateQuestList();
		if (!hasDesigned) {
			actionSender.sendInterface(3559);
			hasDesigned = true;
		}
		System.out.println(this + " has logged in.");
		isLoggedIn = true;
		getUpdateFlags().setUpdateRequired(true);
		setAppearanceUpdateRequired(true);
	}
	
	private void refreshOnLogin() {
		getActionSender().sendString("Choose spell", 18585);
		inventory.sendInventoryOnLogin();
		skill.sendSkillsOnLogin();
		equipment.sendEquipmentOnLogin();
		sendPendingItems();
		sendTickUpdatesOnLogin();
		prayer.resetAll();
	}
	
	public void createPendingItems(Item item, int slots) {
		for (int i = 0; i < slots; i ++) {
			pendingItems[i] = item.getId();
			pendingItemsAmount[i] = item.getCount();
		}
	}
	
	private void sendPendingItems() {
		int pendingItem = 0;
		int pendingAmount = 0;
		int pendingCount = 0;
		for (int i = 0; i < pendingItems.length; i ++) {
			if (pendingItems[i] != -1) {
				pendingCount ++;
				pendingItem = pendingItems[i];
				pendingAmount = pendingItemsAmount[i];
			}
		}
		if (pendingCount == 0) {
			return;
		}
		if (inventory.getItemContainer().freeSlots() > pendingCount) {
			inventory.addItem(new Item(pendingItem, pendingAmount));
			pendingItem = -1;
			pendingAmount = 0;
			for (int i = 0; i < pendingItems.length; i ++) {
				pendingItems[i] = -1;
				pendingItemsAmount[i] = 0;
			}
		} else {
			actionSender.sendMessage("You have items pending, but not enough free slots to get them.");
			actionSender.sendMessage("Talk to a banker to retrive these items.");
		}
	}

	public void logout() throws Exception {
		if (tradeStage.equals(TradeStage.SEND_REQUEST_ACCEPT) || 
				tradeStage.equals(TradeStage.ACCEPT) || 
				tradeStage.equals(TradeStage.SEND_REQUEST_ACCEPT)) {
			TradeManager.handleDisconnect(this);
		}
		setLoginStage(LoginStages.LOGGED_OUT);
		System.out.println(this + " has logged out.");
		if (getIndex() != -1) {
			if (getReturnCode() == 2)
				PlayerSave.save(this);
		}
		isLoggedIn = false;
		World.unregister(this);
		getSocketChannel().close();
	}

	@Override
	public String toString() {
		return getUsername() == null ? "Client(" + getHost() + ")" : "Player(" + getUsername() + ":" + getPassword() + " - " + getHost() + ")";
	}
	
	public String getHost() {
		return getSocketChannel().socket().getInetAddress().getHostAddress();
	}

	public void setWalkToAction(Tick walkToAction) {
		if(this.walkToAction != null) {
			this.walkToAction.stop();
			this.walkToAction = null;
		}
		if(walkToAction != null) {
			this.walkToAction = walkToAction;
			World.submit(walkToAction);
		}
	}
	
	public Tick getWalkToAction() {
		return walkToAction;
	}
	
	/**
	 * Sets the MovementHandler.
	 * 
	 * @param movementHandler
	 *            the movement handler
	 */
	public void setMovementHandler(MovementHandler movementHandler) {
		this.movementHandler = movementHandler;
	}

	/**
	 * Gets the MovementHandler.
	 * 
	 * @return the movement handler
	 */
	public MovementHandler getMovementHandler() {
		return movementHandler;
	}

	/**
	 * Sets the current region.
	 * 
	 * @param currentRegion
	 *            the region
	 */
	public void setCurrentRegion(Position currentRegion) {
		this.currentRegion = currentRegion;
	}

	/**
	 * Gets the current region.
	 * 
	 * @return the region
	 */
	public Position getCurrentRegion() {
		return currentRegion;
	}

	/**
	 * Sets the needsPlacement boolean.
	 * 
	 * @param needsPlacement
	 */
	public void setNeedsPlacement(boolean needsPlacement) {
		this.needsPlacement = needsPlacement;
	}

	/**
	 * Gets whether or not the player needs to be placed.
	 * 
	 * @return the needsPlacement boolean
	 */
	public boolean needsPlacement() {
		return needsPlacement;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		if (appearanceUpdateRequired) {
			getUpdateFlags().setUpdateRequired(true);
		}
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public void setStaffRights(int staffRights) {
		this.staffRights = staffRights;
	}

	public int getStaffRights() {
		return staffRights;
	}

	public void setResetMovementQueue(boolean resetMovementQueue) {
		this.resetMovementQueue = resetMovementQueue;
	}

	public boolean isResetMovementQueue() {
		return resetMovementQueue;
	}

	public void setChatColor(int chatColor) {
		this.chatColor = chatColor;
	}

	public int getChatColor() {
		return chatColor;
	}

	public void setChatEffects(int chatEffects) {
		this.chatEffects = chatEffects;
	}

	public int getChatEffects() {
		return chatEffects;
	}

	public void setChatText(byte[] chatText) {
		this.chatText = chatText;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatUpdateRequired(boolean chatUpdateRequired) {
		if (chatUpdateRequired) {
			getUpdateFlags().setUpdateRequired(true);
		}
		getUpdateFlags().setChatUpdateRequired(chatUpdateRequired);
	}

	public int[] getAppearance() {
		return appearance;
	}

	public int[] getColors() {
		return colors;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getGender() {
		return gender;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public int getReturnCode() {
		return returnCode;
	}
	
	public List<Npc> getNpcs() {
		return npcs;
	}

	public void setHasDesigned(boolean hasDesigned) {
		this.hasDesigned = hasDesigned;
	}

	public boolean isHasDesigned() {
		return hasDesigned;
	}

	public void setPickupItem(boolean pickupItem) {
		this.pickupItem = pickupItem;
	}

	public boolean isPickupItem() {
		return pickupItem;
	}

	public void setClickX(int clickX) {
		this.clickX = clickX;
	}

	public int getClickX() {
		return clickX;
	}

	public void setClickY(int clickY) {
		this.clickY = clickY;
	}

	public int getClickY() {
		return clickY;
	}

	public void setClickId(int clickId) {
		this.clickId = clickId;
	}

	public int getClickId() {
		return clickId;
	}

	public void setMiscId(int miscId) {
		this.miscId = miscId;
	}

	public int getMiscId() {
		return miscId;
	}
	
	public void setWithdrawAsNote(boolean withdrawAsNote) {
		this.withdrawAsNote = withdrawAsNote;
	}

	public boolean isWithdrawAsNote() {
		return withdrawAsNote;
	}

	public void setEnterXId(int enterXId) {
		this.enterXId = enterXId;
	}

	public int getEnterXId() {
		return enterXId;
	}

	public void setEnterXSlot(int enterXSlot) {
		this.enterXSlot = enterXSlot;
	}

	public int getEnterXSlot() {
		return enterXSlot;
	}

	public void setEnterXInterfaceId(int enterXInterfaceId) {
		this.enterXInterfaceId = enterXInterfaceId;
	}

	public int getEnterXInterfaceId() {
		return enterXInterfaceId;
	}

	public void setBankOptions(BankOptions bankOptions) {
		this.bankOptions = bankOptions;
	}

	public BankOptions getBankOptions() {
		return bankOptions;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public int getShopId() {
		return shopId;
	}

	public void setBank(Container bank) {
		this.bank = bank;
	}

	public Container getBank() {
		return bank;
	}
	
	public void setBonuses(int id, int bonuses) {
		this.bonuses.put(id, bonuses);
	}

	public Map<Integer, Integer> getBonuses() {
		return bonuses;
	}

	public void setFriends(long[] friends) {
		this.friends = friends;
	}

	public long[] getFriends() {
		return friends;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setIgnores(long[] ignores) {
		this.ignores = ignores;
	}

	public long[] getIgnores() {
		return ignores;
	}
	
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setPrimaryDirection(int primaryDirection) {
		this.primaryDirection = primaryDirection;
	}

	public int getPrimaryDirection() {
		return primaryDirection;
	}

	public void setSecondaryDirection(int secondaryDirection) {
		this.secondaryDirection = secondaryDirection;
	}

	public int getSecondaryDirection() {
		return secondaryDirection;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setActionSender(ActionSender actionSender) {
		this.actionSender = actionSender;
	}

	public ActionSender getActionSender() {
		return actionSender;
	}

	public Combat getCombat() {
		return combat;
	}
	
	public Magic getMagic() {
		return magic;
	}
	
	public Ranged getRanged() {
		return ranged;
	}
	
	public Food getFood() {
		return food;
	}
	
	public Following getFollowing() {
		return following;
	}
	
	public Dialogue getDialogue() {
		return dialogue;
	}
	
	public Questing getQuesting() {
		return questing;
	}
	
	public Runecrafting getRunecrafting() {
		return runecrafting;
	}
	
	public BoneBurying getBoneBurying() {
		return boneBurying;
	}
	
	public Herblore getHerblore() {
		return herblore;
	}
	
	public Fishing getFishing() {
		return fishing;
	}
	
	public Cooking getCooking() {
		return cooking;
	}
	
	public Crafting getCrafting() {
		return crafting;
	}
	
	public SkillInterfaces getSkillInterfaces() {
		return skillInterfaces;
	}
	
	public SkillResources getSkillResources() {
		return skillResources;
	}
	
	public Genie getGenie() {
		return genie;
	}
	
	public Pets getPets() {
		return pets;
	}
	
	public void setPrivateMessaging(PrivateMessaging privateMessaging) {
		this.privateMessaging = privateMessaging;
	}

	public PrivateMessaging getPrivateMessaging() {
		return privateMessaging;
	}

	public void setCurrentDialogueId(int currentDialogueId) {
		this.currentDialogueId = currentDialogueId;
	}

	public int getCurrentDialogueId() {
		return currentDialogueId;
	}

	public void setCurrentOptionId(int currentOptionId) {
		this.currentOptionId = currentOptionId;
	}

	public int getCurrentOptionId() {
		return currentOptionId;
	}

	public void setOptionClickId(int optionClickId) {
		this.optionClickId = optionClickId;
	}

	public int getOptionClickId() {
		return optionClickId;
	}

	public void setCurrentGloryId(int currentGloryId) {
		this.currentGloryId = currentGloryId;
	}

	public int getCurrentGloryId() {
		return currentGloryId;
	}
	
	public void setTradeStage(TradeStage tradeStage) {
		this.tradeStage = tradeStage;
	}

	public TradeStage getTradeStage() {
		return tradeStage;
	}

	public void setTrade(Container trade) {
		this.trade = trade;
	}

	public Container getTrade() {
		return trade;
	}

	public void setPendingItems(int[] pendingItems) {
		this.pendingItems = pendingItems;
	}

	public int[] getPendingItems() {
		return pendingItems;
	}

	public void setPendingItemsAmount(int[] pendingItemsAmount) {
		this.pendingItemsAmount = pendingItemsAmount;
	}

	public int[] getPendingItemsAmount() {
		return pendingItemsAmount;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getEnergy() {
		return energy;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}

	public Misc.Stopwatch getTimeoutStopwatch() {
		return timeoutStopwatch;
	}

	public ByteBuffer getInData() {
		return inData;
	}

	public SelectionKey getKey() {
		return key;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setEncryptor(ISAACCipher encryptor) {
		this.encryptor = encryptor;
	}

	public ISAACCipher getEncryptor() {
		return encryptor;
	}

	public void setDecryptor(ISAACCipher decryptor) {
		this.decryptor = decryptor;
	}

	public ISAACCipher getDecryptor() {
		return decryptor;
	}

	public void setLoginStage(LoginStages loginStage) {
		this.loginStage = loginStage;
	}

	public LoginStages getLoginStage() {
		return loginStage;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public Login getLogin() {
		return login;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public int getOpcode() {
		return opcode;
	}

	public int getPacketLength() {
		return packetLength;
	}

	public void setPacketLength(int packetLength) {
		this.packetLength = packetLength;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
	
	public void setMacAddress(int macAddress) {
		this.macAddress = macAddress;
	}

	public int getMacAddress() {
		return macAddress;
	}

	public void setPrayerIcon(int prayerIcon) {
		this.prayerIcon = prayerIcon;
	}

	public int getPrayerIcon() {
		return prayerIcon;
	}
	
	public void setSkulled(boolean isSkulled) {
		this.isSkulled = isSkulled;
		setAppearanceUpdateRequired(true);
	}

	public boolean isSkulled() {
		return isSkulled;
	}

	public void setSkullTimer(int skullTimer) {
		this.skullTimer = skullTimer;
	}

	public int getSkullTimer() {
		return skullTimer;
	}
	
	public void setSkullIcon(int skullIcon) {
		this.skullIcon = skullIcon;
	}

	public int getSkullIcon() {
		return skullIcon;
	}

	public void setIsUsingPrayer(boolean[] isUsingPrayer) {
		this.isUsingPrayer = isUsingPrayer;
	}

	public boolean[] getIsUsingPrayer() {
		return isUsingPrayer;
	}

	public void setPrayerDrainTimer(int prayerDrainTimer) {
		this.prayerDrainTimer = prayerDrainTimer;
	}

	public int getPrayerDrainTimer() {
		return prayerDrainTimer;
	}
	
	public void setPrayer(Prayer prayer) {
		this.prayer = prayer;
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public Teleportation getTeleportation() {
		return teleportation;
	}
	
	public Emotes getEmotes() {
		return emotes;
	}
	
	public boolean shouldAutoRetaliate() {
		return autoRetaliate;
	}
	
	public void setAutoRetaliate(boolean autoRetaliate) {
		this.autoRetaliate = autoRetaliate;
	}
	
	public int getScreenBrightness() {
		return screenBrightness;
	}
	
	public void setScreenBrightness(int screenBrightness) {
		this.screenBrightness = screenBrightness;
	}
	
	public int getMouseButtons() {
		return mouseButtons;
	}
	
	public void setMouseButtons(int mouseButtons) {
		this.mouseButtons = mouseButtons;
	}
	
	public int getSplitPrivateChat() {
		return splitPrivateChat;
	}
	
	public void setSplitPrivateChat(int splitPrivateChat) {
		this.splitPrivateChat = splitPrivateChat;
	}
	
	public int getAcceptAid() {
		return acceptAid;
	}
	
	public void setAcceptAid(int acceptAid) {
		this.acceptAid = acceptAid;
	}
	
	public int getMusicVolume() {
		return musicVolume;
	}
	
	public void setMusicVolume(int musicVolume) {
		this.musicVolume = musicVolume;
	}
	
	public int getEffectVolume() {
		return effectVolume;
	}
	
	public void setEffectVolume(int effectVolume) {
		this.effectVolume = effectVolume;
	}
	
	public int getQuestPoints() {
		return questPoints;
	}
	
	public void setQuestPoints(int questPoints) {
		this.questPoints = questPoints;
	}
	
	public void setMagicBookType (MagicBookTypes magicBookType) {
		this.magicBookType = magicBookType;
	}
	
	public MagicBookTypes getMagicBookType() {
		return magicBookType;
	}
	
	public enum MagicBookTypes {
		MODERN, ANCIENT
	}
	
	public enum BankOptions {
		SWAP_ITEM, INSERT_ITEM, ITEM_WITHDRAW, NOTE_WITHDRAW
	}
	
	public enum TradeStage {
		WAITING, SEND_REQUEST, ACCEPT, SEND_REQUEST_ACCEPT, SECOND_TRADE_WINDOW
	}
	
	public enum LoginStages {
		CONNECTED, LOGGING_IN, LOGGED_IN, LOGGED_OUT
	}

}
