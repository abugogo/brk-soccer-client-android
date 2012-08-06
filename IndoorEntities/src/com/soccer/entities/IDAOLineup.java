package com.soccer.entities;

/**
 * The persistent class for the lineup database table.
 * 
 */
public interface IDAOLineup {
	public ILineupPK getId();
	public void setId(ILineupPK id);
	public Object getColor();
	public void setColor(Object color);

	public int getGoal();
	public void setGoal(int goal);
	public String getMisc();
	public void setMisc(String misc);
	public int getOGoal();
	public void setOGoal(int oGoal);
	public short getPoints();
	public void setPoints(short points);
}