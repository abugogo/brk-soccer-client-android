package com.soccer.entities.impl;

import java.io.Serializable;

import com.soccer.entities.IDAOPosition;

/**
 * The persistent class for the positions database table.
 * 
 */
public class DAOPosition implements Serializable, IDAOPosition {
	private static final long serialVersionUID = 1L;
	private short id;
	private String name;
	private String type;

	public DAOPosition() {
	}

	public short getId() {
		return this.id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}