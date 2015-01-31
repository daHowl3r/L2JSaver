/*
 * Authors: Issle, Howler, Matim
 * File: Treasure.java
 */
package com.l2jsaver.features.TreasureHunting.Model;

/**
 * @author Issle
 *
 */
public class Treasure {

	public Treasure(int ownerId, int _itemCount, String _ownerName)
	{
		this.ownerName = _ownerName;
		this.ownerId = ownerId;
		this.itemCount = _itemCount;
	}
	
	private String ownerName;
	private int ownerId;
	private int itemCount;
	
	private int x;
	private int y;
	private int z;
	
	/**
	 * @param count the itemCount to set
	 */
	public void setItemCount(int count) {
		this.itemCount = count;
	}
	/**
	 * @return the itemCount
	 */
	public int getItemCount() {
		return itemCount;
	}
	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	/**
	 * @return the ownerId
	 */
	public int getOwnerId() {
		return ownerId;
	}
	/**
	 * @param ownerName the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	/**
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}
	/**
	 * @param z the z to set
	 */
	public void setZ(int z) {
		this.z = z;
	}
	/**
	 * @return the z
	 */
	public int getZ() {
		return z;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
}
