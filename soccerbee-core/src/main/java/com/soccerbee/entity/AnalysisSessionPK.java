package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the analysis_session database table.
 * 
 */
@Embeddable
public class AnalysisSessionPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(insertable=false, updatable=false, unique=true, nullable=false, length=50)
	private String ubsp;

	@Column(unique=true, nullable=false)
	private int number;

	public AnalysisSessionPK() {
	}
	public String getUbsp() {
		return this.ubsp;
	}
	public void setUbsp(String ubsp) {
		this.ubsp = ubsp;
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
		if (!(other instanceof AnalysisSessionPK)) {
			return false;
		}
		AnalysisSessionPK castOther = (AnalysisSessionPK)other;
		return 
			this.ubsp.equals(castOther.ubsp)
			&& (this.number == castOther.number);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ubsp.hashCode();
		hash = hash * prime + this.number;
		
		return hash;
	}
}