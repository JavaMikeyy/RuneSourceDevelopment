package com.rs2.model;

public class UpdateFlags {
	
	private boolean isUpdateRequired;
	private boolean chatUpdateRequired;
	private boolean isForceChatUpdate;
	private String forceChatMessage;
	private boolean graphicsUpdateRequired;
	private int graphicsId;
	private int graphicsDelay;
	private boolean animationUpdateRequired;
	private int animationId;
	private int animationDelay;
	private boolean entityFaceUpdate;
	private int entityFaceIndex = -1;
	private boolean faceToDirection;
	private Position face;
	private boolean hitUpdate;
	private boolean hitUpdate2;
	private int damage;
	private int damage2;
	private int hitType;
	private int hitType2;
	
	public void sendGraphic(int graphicsId, int graphicsDelay) {
		this.graphicsId = graphicsId;
		this.graphicsDelay = graphicsDelay;
		graphicsUpdateRequired = true;
		isUpdateRequired = true;
	}
	
	public void sendHighGraphic(int graphicsId, int graphicsDelay) {
		this.graphicsId = graphicsId;
		this.graphicsDelay = 6553600 + graphicsDelay;
		graphicsUpdateRequired = true;
		isUpdateRequired = true;
	}
	
	public void sendAnimation(int animationId, int animationDelay) {
		this.animationId = animationId;
		this.animationDelay = animationDelay;
		animationUpdateRequired = true;
		isUpdateRequired = true;
	}
	
	public void faceEntity(int entityFaceIndex) {
		this.entityFaceIndex = entityFaceIndex;
		entityFaceUpdate = true;
		isUpdateRequired = true;
	}
	
	public void sendFaceToDirection(Position face) {
		this.face = face;
		faceToDirection = true;
		isUpdateRequired = true;
	}
	
	public void sendHit(int damage, int hitType) {
		this.damage = damage;
		this.hitType = hitType;
		hitUpdate = true;
		isUpdateRequired = true;
	}
	
	public void sendForceMessage(String forceChatMessage) {
		this.forceChatMessage = forceChatMessage;
		isForceChatUpdate = true;
		isUpdateRequired = true;
	}
	
	public void reset() {
		chatUpdateRequired = false;
		graphicsUpdateRequired = false;
		animationUpdateRequired = false;
		entityFaceUpdate = false;
		faceToDirection = false;
		hitUpdate = false;
		hitUpdate2 = false;
	}
	
	public void setUpdateRequired(boolean isUpdateRequired) {
		this.isUpdateRequired = isUpdateRequired;
	}
	
	public boolean isUpdateRequired() {
		return isUpdateRequired;
	}

	public void setChatUpdateRequired(boolean chatUpdateRequired) {
		this.chatUpdateRequired = chatUpdateRequired;
	}

	public boolean isChatUpdateRequired() {
		return chatUpdateRequired;
	}

	public void setForceChatUpdate(boolean isForceChatUpdate) {
		this.isForceChatUpdate = isForceChatUpdate;
	}

	public boolean isForceChatUpdate() {
		return isForceChatUpdate;
	}

	public void setForceChatMessage(String forceChatMessage) {
		this.forceChatMessage = forceChatMessage;
	}

	public String getForceChatMessage() {
		return forceChatMessage;
	}

	public void setGraphicsUpdateRequired(boolean graphicsUpdateRequired) {
		this.graphicsUpdateRequired = graphicsUpdateRequired;
	}

	public boolean isGraphicsUpdateRequired() {
		return graphicsUpdateRequired;
	}

	public void setGraphicsId(int graphicsId) {
		this.graphicsId = graphicsId;
	}

	public int getGraphicsId() {
		return graphicsId;
	}

	public void setGraphicsDelay(int graphicsDelay) {
		this.graphicsDelay = graphicsDelay;
	}

	public int getGraphicsDelay() {
		return graphicsDelay;
	}

	public void setAnimationUpdateRequired(boolean animationUpdateRequired) {
		this.animationUpdateRequired = animationUpdateRequired;
	}

	public boolean isAnimationUpdateRequired() {
		return animationUpdateRequired;
	}

	public void setAnimationId(int animationId) {
		this.animationId = animationId;
	}

	public int getAnimationId() {
		return animationId;
	}

	public void setAnimationDelay(int animationDelay) {
		this.animationDelay = animationDelay;
	}

	public int getAnimationDelay() {
		return animationDelay;
	}

	public void setEntityFaceUpdate(boolean entityFaceUpdate) {
		this.entityFaceUpdate = entityFaceUpdate;
	}

	public boolean isEntityFaceUpdate() {
		return entityFaceUpdate;
	}

	public void setEntityFaceIndex(int entityFaceIndex) {
		this.entityFaceIndex = entityFaceIndex;
	}

	public int getEntityFaceIndex() {
		return entityFaceIndex;
	}

	public void setFaceToDirection(boolean faceToDirection) {
		this.faceToDirection = faceToDirection;
	}

	public boolean isFaceToDirection() {
		return faceToDirection;
	}

	public void setFace(Position face) {
		this.face = face;
	}

	public Position getFace() {
		return face;
	}

	public void setHitUpdate(boolean hitUpdate) {
		this.hitUpdate = hitUpdate;
	}

	public boolean isHitUpdate() {
		return hitUpdate;
	}

	public void setHitUpdate2(boolean hitUpdate2) {
		this.hitUpdate2 = hitUpdate2;
	}

	public boolean isHitUpdate2() {
		return hitUpdate2;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage2(int damage2) {
		this.damage2 = damage2;
	}

	public int getDamage2() {
		return damage2;
	}

	public void setHitType(int hitType) {
		this.hitType = hitType;
	}

	public int getHitType() {
		return hitType;
	}

	public void setHitType2(int hitType2) {
		this.hitType2 = hitType2;
	}

	public int getHitType2() {
		return hitType2;
	}

}
