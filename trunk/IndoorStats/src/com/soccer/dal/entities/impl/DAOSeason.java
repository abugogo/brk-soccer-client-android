package com.soccer.dal.entities.impl;

import java.io.Serializable;

import com.soccer.dal.entities.api.IDAOSeason;

import java.util.Date;


/**
 * The persistent class for the seasons database table.
 * 
 */
public class DAOSeason implements Serializable, IDAOSeason {
	private static final long serialVersionUID = 1L;
	private int id;
	private Date edate;
	private String misc;
	private Date sdate;

    public DAOSeason() {
    }


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


    public Date getEdate() {
		return this.edate;
	}

	public void setEdate(Date edate) {
		this.edate = edate;
	}


    public String getMisc() {
		return this.misc;
	}

	public void setMisc(String misc) {
		this.misc = misc;
	}


    public Date getSdate() {
		return this.sdate;
	}

	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}

}