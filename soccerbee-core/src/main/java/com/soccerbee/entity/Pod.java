package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the pod database table.
 * 
 */
@Entity
@Table(name="pod")
@NamedQuery(name="Pod.findAll", query="SELECT p FROM Pod p")
public class Pod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="idf_pod", unique=true, nullable=false, length=12)
	private String idfPod;

	@Column(name="reg_date", insertable=false, updatable=false)
	private Timestamp regDate;

	@Column(nullable=false, length=1)
	private String type;

	@Column(name="upd_date", insertable=false, updatable=false)
	private Timestamp updDate;

	//bi-directional one-to-one association to Account
	@OneToOne(mappedBy="pod", fetch=FetchType.LAZY)
	private Account account;

	public Pod() {
	}

	public String getIdfPod() {
		return this.idfPod;
	}

	public void setIdfPod(String idfPod) {
		this.idfPod = idfPod;
	}

	public Timestamp getRegDate() {
		return this.regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
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