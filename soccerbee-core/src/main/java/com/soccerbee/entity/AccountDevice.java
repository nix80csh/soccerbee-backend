package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the account_device database table.
 * 
 */
@Entity
@Table(name="account_device")
@NamedQuery(name="AccountDevice.findAll", query="SELECT a FROM AccountDevice a")
public class AccountDevice implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AccountDevicePK id;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(nullable=false, length=500)
	private String token;

	@Column(nullable=false, length=1)
	private String type;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to Account
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_account", nullable=false, insertable=false, updatable=false)
	private Account account;

	public AccountDevice() {
	}

	public AccountDevicePK getId() {
		return this.id;
	}

	public void setId(AccountDevicePK id) {
		this.id = id;
	}

	public Timestamp getRegDate() {
		return this.regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getUpdDate() {
		return this.updDate;
	}

	public void setUpdDate(Timestamp updDate) {
		this.updDate = updDate;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}