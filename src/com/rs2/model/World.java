package com.rs2.model;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CountDownLatch;

import com.rs2.Constants;
import com.rs2.util.Misc;
import com.rs2.model.npcs.Npc;
import com.rs2.model.npcs.NpcDefinition;
import com.rs2.model.npcs.NpcUpdating;
import com.rs2.model.players.ItemManager;
import com.rs2.model.players.Player;
import com.rs2.model.players.PlayerUpdating;
import com.rs2.model.tick.Tick;
import com.rs2.model.tick.TickManager;

/**
 * Handles all logged in players.
 * 
 * @author blakeman8192
 */
public class World {

	/** All registered players. */
	private static Player[] players = new Player[2048];

	/** All registered NPCs. */
	private static Npc[] npcs = new Npc[Constants.MAX_NPCS];
	
	private static TickManager tickManager = new TickManager();
	
	private static NpcDefinition[] definitions = new NpcDefinition[Constants.MAX_NPCS];
	
	private static ExecutorService threadPool;
	
	static {
		int cpus = Runtime.getRuntime().availableProcessors();
		threadPool = Executors.newFixedThreadPool(cpus);
	}

	/**
	 * Performs the processing of all players.
	 * 
	 * @throws Exception
	 */
	public static void process() throws Exception {
		Iterator<Tick> tickIt$ = tickManager.getTickables().iterator();
		while(tickIt$.hasNext()) {
			Tick t = tickIt$.next();
			t.cycle();
			if(!t.isRunning()) {
				tickIt$.remove();
			}
		}
		// Perform any logic processing for players.
		for (int i = 0; i < players.length; i++) {
			Player player = players[i];
			if (player == null) {
				continue;
			}
			try {
				player.process();
			} catch (Exception ex) {
				ex.printStackTrace();
				player.disconnect();
			}
		}
		
		// Perform any logic processing for NPCs.
		for (int i = 0; i < npcs.length; i++) {
			Npc npc = npcs[i];
			if (npc == null) {
				continue;
			}
			try {
				npc.process();
			} catch (Exception ex) {
				ex.printStackTrace();
				unregister(npc);
			}
		}

		// Update all players.
		final CountDownLatch latch = new CountDownLatch(playerAmount());
		for (int i = 0; i < players.length; i++) {
			final Player player = players[i];
			if (player == null) {
				continue;
			}
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					synchronized (player) {
						try {
							PlayerUpdating.update(player);
							NpcUpdating.update(player);
						} catch (Exception ex) {
							ex.printStackTrace();
							player.disconnect();
						} finally {
							latch.countDown();
						}
					}
				}
			});
		}
		try {
			latch.await();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		// Reset all players after cycle.
		for (int i = 0; i < players.length; i++) {
			Player player = players[i];
			if (player == null) {
				continue;
			}
			try {
				player.reset();
			} catch (Exception ex) {
				ex.printStackTrace();
				player.disconnect();
			}
		}
		
		// Reset all npcs after cycle.
		for (int i = 0; i < npcs.length; i++) {
			Npc npc = npcs[i];
			if (npc == null) {
				continue;
			}
			try {
				npc.reset();
			} catch (Exception ex) {
				ex.printStackTrace();
				unregister(npc);
			}
		}
		ItemManager.getInstance().tick();
	}

	/**
	 * Registers a player for processing.
	 * 
	 * @param player
	 *            the player
	 */
	public static void register(Player player) {
		for (int i = 1; i < players.length; i++) {
			if (players[i] == null) {
				players[i] = player;
				player.setIndex(i);
				return;
			}
		}
		throw new IllegalStateException("Server is full!");
	}

	/**
	 * Registers an NPC for processing.
	 * 
	 * @param npc
	 *            the npc
	 */
	public static void register(Npc npc) {
		for (int i = 1; i < npcs.length; i++) {
			if (npcs[i] == null) {
				npcs[i] = npc;
				npc.setIndex(i);
				return;
			}
		}
		throw new IllegalStateException("Server is full!");
	}

	/**
	 * Unregisters a player from processing.
	 * 
	 * @param player
	 *            the player
	 */
	public static void unregister(Player player) {
		if (player.getIndex() == -1) {
			return;
		}
		players[player.getIndex()] = null;
	}

	/**
	 * Unregisters an NPC from processing.
	 * 
	 * @param npc
	 *            the npc
	 */
	public static void unregister(Npc npc) {
		if (npc.getIndex() == -1) {
			return;
		}
		npcs[npc.getIndex()] = null;
	}

	/**
	 * Gets the amount of players that are online.
	 * 
	 * @return the amount of online players
	 */
	public static int playerAmount() {
		int amount = 0;
		for (int i = 1; i < players.length; i++) {
			if (players[i] != null) {
				amount++;
			}
		}
		return amount;
	}

	/**
	 * Gets the amoutn of NPCs that are online.
	 * 
	 * @return the amount of online NPCs
	 */
	public static int npcAmount() {
		int amount = 0;
		for (int i = 1; i < npcs.length; i++) {
			if (npcs[i] != null) {
				amount++;
			}
		}
		return amount;
	}
	
	public static Player getPlayerByName(String name) {
		for (Player player : players) {
			if (player == null)
				continue;
			if (player.getUsername().equals(name)) {
				return player;
			}
		}
		return null;
	}
	
	public static int getNpcIndex(int id) {
		for (int i = 1; i < npcs.length; i++) {
			if (npcs[i] != null) {
				if (npcs[i].getNpcId() == id) {
					return npcs[i].getIndex();
				}
			}
		}
		return -1;
	}
	
	public static void submit(final Tick tick) {
		World.tickManager.submit(tick);
	}

	public static void sendProjectile(Position position, int offsetX, int offsetY, int id, int startHeight, int endHeight, int speed, int lockon) {
		for (Player player : players) {
			if(player == null) 
				continue;
			if (position.isViewableFrom(player.getPosition())) {
				player.getActionSender().sendProjectile(position, offsetX, offsetY, id, startHeight, endHeight, speed, lockon);
			}
		}
	}
	
	/**
	 * Gets all registered players.
	 * 
	 * @return the players
	 */
	public static Player[] getPlayers() {
		return players;
	}

	/**
	 * Gets all registered NPCs.
	 * 
	 * @return the npcs
	 */
	public static Npc[] getNpcs() {
		return npcs;
	}

	public static void setTickManager(TickManager tickManager) {
		World.tickManager = tickManager;
	}

	public static TickManager getTickManager() {
		return tickManager;
	}

	public static void setDefinitions(NpcDefinition[] definitions) {
		World.definitions = definitions;
	}

	public static NpcDefinition[] getDefinitions() {
		return definitions;
	}

}
