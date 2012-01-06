package com.rs2.model.content;

import com.rs2.util.Misc;
import com.rs2.model.players.Player;
import com.rs2.model.npcs.Npc;
import com.rs2.model.Entity;
import com.rs2.model.World;
import com.rs2.util.clip.Region;
import com.rs2.util.clip.PathFinder;

/**
  * By Mikey` of Rune-Server
  */
public class Following {

	private Player player;
	private Npc npc;
	
	public Following(Player player) {
		this.player = player;
	}
	
	public Following(Npc npc) {
		this.npc = npc;
	}
	
	/**
	  * Direction coordinate modifiers
	  */
	public final int[][] COORDINATE_MODIFIERS =
	{{-1, 1}, {0, 1}, {1, 1}, {-1, 0}, 
	{1, 0}, {-1, -1}, {0, -1}, {1, -1}};
	
	public final int[][][] SECONDARY_COORDINATE_MODIFIERS =
	{
	{{0, 0}, {-1, 2}, {0, 0}, {-2, 1}, {0, 0}, {0, 0}, {0, 0}, {0, 0}},
	{{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}},
	{{0, 0}, {1, 2}, {0, 0}, {0, 0}, {2, 1}, {0, 0}, {0, 0}, {0, 0}},
	{{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}},
	{{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}},
	{{0, 0}, {0, 0}, {0, 0}, {-2, -1}, {0, 0}, {0, 0}, {-1, -2}, {0, 0}},
	{{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}},
	{{0, 0}, {0, 0}, {0, 0}, {0, 0}, {2, -1}, {0, 0}, {1, -2}, {0, 0}},
	};
	
	/**
	  * The entity's follow tick.
	  */
	public void followTick(Entity entity) {
		if (entity.getFollowingEntity() != null)
			followEntity(entity);
	}
	
	/**
	  * An entity following another entity.
	  */
	public void followEntity(Entity follower) {
		if (outOfRange(follower.getFollowingEntity(), follower) ||
				follower.getFollowingEntity().isDead()) {
			resetFollow(follower);
			return;
		}
		if (follower.getFollowingEntity() instanceof Player)
			follower.getUpdateFlags().faceEntity(follower.getFollowingEntity().getIndex() + 32768);
		else
			follower.getUpdateFlags().faceEntity(follower.getFollowingEntity().getIndex());
		if (inStoppingPosition(follower, 1) || follower.isFrozen()) {
			return;
		}
		if (follower instanceof Player && follower.isInstigatingAttack() 
		&& player.getCombat().withinRange(follower, follower.getFollowingEntity())
		&& Misc.getDistance(follower.getFollowingEntity().getPosition(), follower.getPosition()) != 0) {
			return;
		}
		else if (follower instanceof Npc && follower.isInstigatingAttack() 
		&& npc.getCombat().withinRange(follower, follower.getFollowingEntity())
		&& Misc.getDistance(follower.getFollowingEntity().getPosition(), follower.getPosition()) != 0) {
			return;
		}
		int direction = calculateWalkPath(follower);
		if (direction == -1)
			return;
		int xModifier = 0, yModifier = 0;
		if (follower instanceof Player) {
			if (player.getMovementHandler().isRunToggled() &&
			Misc.getDistance(follower.getFollowingEntity().getPosition(), follower.getPosition()) > 2) {
				int runDirection = calculateRunPath(follower, direction);
				player.setPrimaryDirection(direction);
				player.setSecondaryDirection(runDirection);
				if (runDirection != direction) {
					xModifier = SECONDARY_COORDINATE_MODIFIERS[direction][runDirection][0];
					yModifier = SECONDARY_COORDINATE_MODIFIERS[direction][runDirection][1];
				}
				else {
					xModifier = COORDINATE_MODIFIERS[direction][0] * 2;
					yModifier = COORDINATE_MODIFIERS[direction][1] * 2;
				}
				player.appendPlayerPosition(xModifier, yModifier);
			}
			else {
				player.setPrimaryDirection(direction);
				xModifier = COORDINATE_MODIFIERS[direction][0];
				yModifier = COORDINATE_MODIFIERS[direction][1];
				player.appendPlayerPosition(xModifier, yModifier);
			}
		}
		else if (follower instanceof Npc) {
			if (follower.getFollowingEntity() instanceof Player)
				npc.getUpdateFlags().faceEntity(follower.getFollowingEntity().getIndex() + 32768);
			else
				npc.getUpdateFlags().faceEntity(follower.getFollowingEntity().getIndex());
			if (Region.tileClipped(npc.getPosition(), COORDINATE_MODIFIERS[direction][0], COORDINATE_MODIFIERS[direction][1], 
					0, false)) {
				direction = PathFinder.checkDirection(npc.getPosition(), direction, false);
				System.out.println("to " + direction);
				if (Region.tileClipped(npc.getPosition(), COORDINATE_MODIFIERS[direction][0], 
						COORDINATE_MODIFIERS[direction][1], 
						0, false)) {
					direction = PathFinder.checkDirection(npc.getPosition(), direction, true);
					System.out.println("to " + direction);
					if (Region.tileClipped(npc.getPosition(), COORDINATE_MODIFIERS[direction][0], 
							COORDINATE_MODIFIERS[direction][1], 
							0, false)) {
						System.out.println("false");
						return;
					}
				}
			}
			npc.setPrimaryDirection(direction);
			xModifier = COORDINATE_MODIFIERS[direction][0];
			yModifier = COORDINATE_MODIFIERS[direction][1];
			npc.appendNpcPosition(xModifier, yModifier);
		}
	}
	
	/**
	  * Calculates the entity's walk path.
	  */
	public int calculateWalkPath(Entity follower) {
		int followerX = follower.getPosition().getX(), followerY = follower.getPosition().getY();
		int leaderX = follower.getFollowingEntity().getPosition().getX(), 
		leaderY = follower.getFollowingEntity().getPosition().getY();
		int height = follower.getFollowingEntity().getPosition().getZ();
		if (leaderX == followerX && leaderY == followerY) {
			switch (Misc.randomNumber(2)) {
				case 0:
					return 1;
				case 1:
					return 6;
			}
		}
		if (leaderX > followerX && leaderY == followerY) {
			return 4;
		}
		if (leaderX == followerX && leaderY > followerY) {
			return 1;
		}
		if (leaderX < followerX && leaderY == followerY) {
			return 3;
		}
		if (leaderX == followerX && leaderY < followerY) { 
			return 6;
		}
		if (leaderX > followerX && leaderY > followerY) {
			return 2;
		}
		if (leaderX < followerX && leaderY < followerY) {
			return 5;
		}
		if (leaderX > followerX && leaderY < followerY) {
			return 7;
		}
		if (leaderX < followerX && leaderY > followerY) {
			return 0;
		}
		return -1;
	}
	
	/**
	  * Calculates the entity's run path.
	  */
	public int calculateRunPath(Entity follower, int direction) {
		int followerX = follower.getPosition().getX(), followerY = follower.getPosition().getY();
		int leaderX = follower.getFollowingEntity().getPosition().getX(), 
		leaderY = follower.getFollowingEntity().getPosition().getY();
		switch (direction) {
			case 0:
				if ((leaderY - followerY) == 1)
					return 3;
				if ((leaderX - followerX) == -1)
					return 1;
				break;
			case 2:
				if ((leaderX - followerX) == 1)
					return 1;
				if ((leaderY - followerY) == 1)
					return 4;
				break;
			case 5:
				if ((leaderY - followerY) == -1)
					return 3;
				if ((leaderX - followerX) == -1)
					return 6;
				break;
			case 7:
				if ((leaderX - followerX) == 1)
					return 6;
				if ((leaderY - followerY) == -1)
					return 4;
				break;
		}
		return direction;
	}
	
	/**
	  * Resets the follower.
	  */
	public void resetFollow(Entity follower) {
		follower.setFollowingEntity(null);
	}
	
	/**
	  * Is the leader out of range of the follower?
	  */
	private boolean outOfRange(Entity leader, Entity follower) {
		return Misc.getDistance(leader.getPosition(), follower.getPosition()) > 16;
	}
	
	/**
	  * Is the follower at the correct stopping distance?
	  */
	private boolean inStoppingPosition(Entity follower, int distanceToFollow) {
		return (Misc.getDistance(follower.getFollowingEntity().getPosition(), follower.getPosition()) <= distanceToFollow && 
		Misc.getDistance(follower.getFollowingEntity().getPosition(), follower.getPosition()) != 0);
	}

}


