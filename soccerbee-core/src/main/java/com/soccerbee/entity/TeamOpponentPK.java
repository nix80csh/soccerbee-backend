package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the team_opponent database table.
 * 
 */
@Embeddable
public class TeamOpponentPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="idf_team", insertable=false, updatable=false, unique=true, nullable=false)
	private int idfTeam;

	@Column(name="idf_team_opponent", unique=true, nullable=false)
	private int idfTeamOpponent;

	public TeamOpponentPK() {
	}
	public int getIdfTeam() {
		return this.idfTeam;
	}
	public void setIdfTeam(int idfTeam) {
		this.idfTeam = idfTeam;
	}
	public int getIdfTeamOpponent() {
		return this.idfTeamOpponent;
	}
	public void setIdfTeamOpponent(int idfTeamOpponent) {
		this.idfTeamOpponent = idfTeamOpponent;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof TeamOpponentPK)) {
			return false;
		}
		TeamOpponentPK castOther = (TeamOpponentPK)other;
		return 
			(this.idfTeam == castOther.idfTeam)
			&& (this.idfTeamOpponent == castOther.idfTeamOpponent);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idfTeam;
		hash = hash * prime + this.idfTeamOpponent;
		
		return hash;
	}
}