package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the firmware database table.
 * 
 */
@Embeddable
public class FirmwarePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(unique=true, nullable=false, length=1)
	private String type;

	@Column(unique=true, nullable=false, length=5)
	private String version;

	public FirmwarePK() {
	}
	public String getType() {
		return this.type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return this.version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FirmwarePK)) {
			return false;
		}
		FirmwarePK castOther = (FirmwarePK)other;
		return 
			this.type.equals(castOther.type)
			&& this.version.equals(castOther.version);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.type.hashCode();
		hash = hash * prime + this.version.hashCode();
		
		return hash;
	}
}