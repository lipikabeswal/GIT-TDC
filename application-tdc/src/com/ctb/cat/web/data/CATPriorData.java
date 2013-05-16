package com.ctb.cat.web.data;

public class CATPriorData {
	
	private int subtestId;
	private int externalId;
	private int abilityScore;
	private int priorRosterId;
	private CATPriorItemData [] priorItems;
	
	
	public int getPriorRosterId() {
		return priorRosterId;
	}
	public void setPriorRosterId(int priorRosterId) {
		this.priorRosterId = priorRosterId;
	}
	public int getExternalId() {
		return externalId;
	}
	public void setExternalId(int externalId) {
		this.externalId = externalId;
	}
	public int getSubtestId() {
		return subtestId;
	}
	public void setSubtestId(int subtestId) {
		this.subtestId = subtestId;
	}
	public int getAbilityScore() {
		return abilityScore;
	}
	public void setAbilityScore(int abilityScore) {
		this.abilityScore = abilityScore;
	}
	public CATPriorItemData[] getPriorItems() {
		return priorItems;
	}
	public void setPriorItems(CATPriorItemData[] priorItems) {
		this.priorItems = priorItems;
	}
	
	
}
