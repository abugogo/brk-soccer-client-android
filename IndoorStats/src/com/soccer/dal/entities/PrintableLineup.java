package com.soccer.dal.entities;

import com.soccer.entities.impl.DAOLEvent;
import com.soccer.entities.impl.DAOLineup;

public class PrintableLineup extends DAOLineup {

	private static final long serialVersionUID = 1L;
	private String fname = "";
	private String lname = "";
	
	public PrintableLineup() {
		super();
	}
	
	
	
	public String getFname() {
		return fname;
	}



	public void setFname(String fname) {
		this.fname = fname;
	}



	public String getLname() {
		return lname;
	}



	public void setLname(String lname) {
		this.lname = lname;
	}



	public String print() {
		return toStringEvents();
	}
	
	public String toStringEvents() {
		String evts = "";
		int g = 0, og = 0, r = 0, y = 0, c = 0;
		for (DAOLEvent ge : lstEvents) {
			switch (ge.getType()) {
			case Goal:
				g++;
				break;
			case O_Goal:
				og++;
				break;
			case Y_Card:
				y++;
				break;
			case R_Card:
				r++;
				break;
			case Cook:
				c++;
				break;
			}
		}
		evts = fname.concat(" ").concat(lname)
				+ (((g + og + r + y + c) > 0) ? "(" + g + "," + og + "," + r
						+ "," + y + "," + c + ")" : "");
		return evts;
	}
	
	
}
