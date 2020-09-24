package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the schedule_match database table.
 * 
 */
@Embeddable
public class ScheduleMatchPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="idf_match", insertable=false, updatable=false, unique=true, nullable=false)
	private int idfMatch;

	@Column(name="idf_team", insertable=false, updatable=false, unique=true, nullable=false)
	private int idfTeam;

	@Column(name="idf_account", insertable=false, updatable=false, unique=true, nullable=false)
	private int idfAccount;

	public ScheduleMatchPK() {
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

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ScheduleMatchPK)) {
			return false;
		}
		ScheduleMatchPK castOther = (ScheduleMatchPK)other;
		return 
			(this.idfMatch == castOther.idfMatch)
			&& (this.idfTeam == castOther.idfTeam)
			&& (this.idfAccount == castOther.idfAccount);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idfMatch;
		hash = hash * prime + this.idfTeam;
		hash = hash * prime + this.idfAccount;
		
		return hash;
	}
}