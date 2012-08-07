package com.soccer.entities.impl;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;

import com.soccer.entities.IDAOPlayer;


/**
 * The persistent class for the players database table.
 * 
 */
public class DAOPlayer implements Serializable, IDAOPlayer {
	private static final long serialVersionUID = 1L;
	private String id;
	private byte[] active;
	private String address1;
	private String address2;
	private Date bday;
	private String description;
	private String email;
	private String fbUser;
	private String fname;
	private BigInteger idNum;
	private String lname;
	private String occupation;
	private byte[] p_img;
	private String tel1;
	private String tel2;
	private DAOPosition positionBean;

    public DAOPlayer() {
    }

    public DAOPlayer(HashMap<String, Object> columns) {
    	
    	setPlayer(columns);
    }

	private void setPlayer(HashMap<String, Object> columns) {
		this.setId("" + columns.get("id"));
		this.setIdNum(BigInteger.valueOf((Long)columns.get("id_num")));
		this.setFname((String) columns.get("fname"));
		this.setLname((String) columns.get("lname"));
		this.setPositionBean(new DAOPosition());
		this.setTel1("" + columns.get("tel1"));
		this.setTel2("" + columns.get("tel2"));
		this.setEmail("" + columns.get("email"));
		this.setBday((Date)columns.get("bday"));
		this.setFbUser((String) columns.get("fb_user"));
		this.setOccupation("" + columns.get("occupation"));
		this.setAddress1("" + columns.get("address1"));
		this.setAddress2("" + columns.get("address2"));
		this.setDescription("" + columns.get("description"));
		this.setP_img(null);
		this.setActive(null);
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public byte[] getActive() {
		return this.active;
	}

	public void setActive(byte[] active) {
		this.active = active;
	}


    public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}


    public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}


    public Date getBday() {
		return this.bday;
	}

	public void setBday(Date bday) {
		this.bday = bday;
	}


    public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


    public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getFbUser() {
		return this.fbUser;
	}

	public void setFbUser(String fbUser) {
		this.fbUser = fbUser;
	}


    public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}


	public BigInteger getIdNum() {
		return this.idNum;
	}

	public void setIdNum(BigInteger idNum) {
		this.idNum = idNum;
	}


    public String getLname() {
		return this.lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}


    public String getOccupation() {
		return this.occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}


    public byte[] getP_img() {
		return this.p_img;
	}

	public void setP_img(byte[] p_img) {
		this.p_img = p_img;
	}


    public String getTel1() {
		return this.tel1;
	}

	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}


    public String getTel2() {
		return this.tel2;
	}

	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}

	public DAOPosition getPositionBean() {
		return this.positionBean;
	}

	public void setPositionBean(DAOPosition positionBean) {
		this.positionBean = positionBean;
	}
}