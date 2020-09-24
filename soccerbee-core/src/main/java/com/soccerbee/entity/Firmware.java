package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the firmware database table.
 * 
 */
@Entity
@Table(name="firmware")
@NamedQuery(name="Firmware.findAll", query="SELECT f FROM Firmware f")
public class Firmware implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FirmwarePK id;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	public Firmware() {
	}

	public FirmwarePK getId() {
		return this.id;
	}

	public void setId(FirmwarePK id) {
		this.id = id;
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

}