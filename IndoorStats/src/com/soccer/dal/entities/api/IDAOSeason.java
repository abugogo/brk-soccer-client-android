package com.soccer.dal.entities.api;

import java.util.Date;

/**
 * The persistent class for the seasons database table.
 * 
 */
public interface IDAOSeason {
	public int getId();
	public void setId(int id);
	public Date getEdate();
	public void setEdate(Date edate);
	public String getMisc();
	public void setMisc(String misc);
	public Date getSdate();
	public void setSdate(Date sdate);
}