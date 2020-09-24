package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the match_analysis_session database table.
 * 
 */
@Embeddable
public class MatchAnalysisSessionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="idf_match", insertable=false, updatable=false, unique=true, nullable=false)
	private int idfMatch;

	@Column(unique=true, nullable=false)
	private int number;

	public MatchAnalysisSessionPK() {
	}
	public int getIdfMatch() {
		return this.idfMatch;
	}
	public void setIdfMatch(int idfMatch) {
		this.idfMatch = idfMatch;
	}
	public int getNumber() {
		return this.number;
	}
	public void setNumber(int number) {
		this.number = number;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MatchAnalysisSessionPK)) {
			return false;
		}
		MatchAnalysisSessionPK castOther = (MatchAnalysisSessionPK)other;
		return 
			(this.idfMatch == castOther.idfMatch)
			&& (this.number == castOther.number);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idfMatch;
		hash = hash * prime + this.number;
		
		return hash;
	}
}