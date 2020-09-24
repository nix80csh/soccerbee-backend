package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the account_device database table.
 * 
 */
@Embeddable
public class AccountDevicePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="idf_account", insertable=false, updatable=false, unique=true, nullable=false)
	private int idfAccount;

	@Column(name="idf_device", unique=true, nullable=false, length=100)
	private String idfDevice;

	public AccountDevicePK() {
	}
	public int getIdfAccount() {
		return this.idfAccount;
	}
	public void setIdfAccount(int idfAccount) {
		this.idfAccount = idfAccount;
	}
	public String getIdfDevice() {
		return this.idfDevice;
	}
	public void setIdfDevice(String idfDevice) {
		this.idfDevice = idfDevice;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AccountDevicePK)) {
			return false;
		}
		AccountDevicePK castOther = (AccountDevicePK)other;
		return 
			(this.idfAccount == castOther.idfAccount)
			&& this.idfDevice.equals(castOther.idfDevice);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idfAccount;
		hash = hash * prime + this.idfDevice.hashCode();
		
		return hash;
	}
}