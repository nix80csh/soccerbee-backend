package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the account_player database table.
 * 
 */
@Entity
@Table(name="account_player")
@NamedQuery(name="AccountPlayer.findAll", query="SELECT a FROM AccountPlayer a")
public class AccountPlayer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="idf_account", unique=true, nullable=false)
	private int idfAccount;

	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date birthday;

	@Column(length=2)
	private String country;

	@Column(length=1)
	private String foot;

	@Column(nullable=false, length=1)
	private String gender;

	@Column(nullable=false)
	private Integer height;

	@Column(length=255)
	private String image;

	@Column(nullable=false, length=3)
	private String position;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	@Column(nullable=false)
	private Integer weight;

	//bi-directional one-to-one association to Account
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_account", nullable=false, insertable=false, updatable=false)
	private Account account;

	public AccountPlayer() {
	}

	public int getIdfAccount() {
		return this.idfAccount;
	}

	public void setIdfAccount(int idfAccount) {
		this.idfAccount = idfAccount;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFoot() {
		return this.foot;
	}

	public void setFoot(String foot) {
		this.foot = foot;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getHeight() {
		return this.height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Timestamp getRegDate() {
		return this.regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	public Timestamp getUpdDate() {
		return this.updDate;
	}

	public void setUpdDate(Timestamp updDate) {
		this.updDate = updDate;
	}

	public Integer getWeight() {
		return this.weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}