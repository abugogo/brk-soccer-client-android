package com.soccer.entities.impl;

import java.io.Serializable;

import com.soccer.entities.IDAOLineup;
import com.soccer.entities.ILineupPK;


/**
 * The persistent class for the lineup database table.
 * 
 */
public class DAOLineup implements Serializable, IDAOLineup {
	private static final long serialVersionUID = 1L;
	private ILineupPK id;
	private Object color;
	private int goal;
	private String misc;
	private int oGoal;
	private short points;

    public DAOLineup() {
    }


	public ILineupPK getId() {
		return this.id;
	}

	public void setId(ILineupPK id) {
		this.id = id;
	}
	

	public Object getColor() {
		return this.color;
	}

	public void setColor(Object color) {
		this.color = color;
	}


	public int getGoal() {
		return this.goal;
	}

	public void setGoal(int goal) {
		this.goal = goal;
	}


	public String getMisc() {
		return this.misc;
	}

	public void setMisc(String misc) {
		this.misc = misc;
	}


	public int getOGoal() {
		return this.oGoal;
	}

	public void setOGoal(int oGoal) {
		this.oGoal = oGoal;
	}


	public short getPoints() {
		return this.points;
	}

	public void setPoints(short points) {
		this.points = points;
	}


}