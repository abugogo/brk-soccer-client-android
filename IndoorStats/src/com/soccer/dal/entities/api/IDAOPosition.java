package com.soccer.dal.entities.api;

/**
 * The persistent class for the positions database table.
 * 
 */
public interface IDAOPosition {
	public short getId();
	public void setId(short id);
	public String getName();
	public void setName(String name);
	public String getType();
	public void setType(String type);
}