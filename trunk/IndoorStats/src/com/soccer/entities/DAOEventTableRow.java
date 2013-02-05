package com.soccer.entities;

import com.soccer.entities.impl.DAOAggrLEvents;

public class DAOEventTableRow extends DAOAggrLEvents {
	private static final long serialVersionUID = 1L;
	
	private String _fname;
	private String _lname;
	public String get_fname() {
		return _fname;
	}
	public void set_fname(String _fname) {
		this._fname = _fname;
	}
	public String get_lname() {
		return _lname;
	}
	public void set_lname(String _lname) {
		this._lname = _lname;
	}
	
}
