package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the formation database table.
 * 
 */
@Embeddable
public class FormationPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="idf_match", insertable=false, updatable=false, unique=true, nullable=false)
	private int idfMatch;

	@Column(name="idf_team", insertable=false, updatable=false, unique=true, nullable=false)
	private int idfTeam;

	@Column(name="idf_account", insertable=false, updatable=false, unique=true, nullable=false)
	private int idfAccount;

	@Column(name="session_number", unique=true, nullable=false)
	private int sessionNumber;

	public FormationPK() {
	}
	public int getIdfMatch() {
		return this.idfMatch;
	}
	public void setIdfMatch(int idfMatch) {
		this.idfMatch = idfMatch;
	}
	public int getIdfTeam() {
		return this.idfTeam;
	}
	public void setIdfTeam(int idfTeam) {
		this.idfTeam = idfTeam;
	}
	public int getIdfAccount() {
		return this.idfAccount;
	}
	public void setIdfAccount(int idfAccount) {
		this.idfAccount = idfAccount;
	}
	public int getSessionNumber() {
		return this.sessionNumber;
	}
	public void setSessionNumber(int sessionNumber) {
		this.sessionNumber = sessionNumber;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FormationPK)) {
			return false;
		}
		FormationPK castOther = (FormationPK)other;
		return 
			(this.idfMatch == castOther.idfMatch)
			&& (this.idfTeam == castOther.idfTeam)
			&& (this.idfAccount == castOther.idfAccount)
			&& (this.sessionNumber == castOther.sessionNumber);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idfMatch;
		hash = hash * prime + this.idfTeam;
		hash = hash * prime + this.idfAccount;
		hash = hash * prime + this.sessionNumber;
		
		return hash;
	}
}