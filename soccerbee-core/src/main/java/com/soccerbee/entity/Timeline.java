package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the timeline database table.
 * 
 */
@Entity
@Table(name="timeline")
@NamedQuery(name="Timeline.findAll", query="SELECT t FROM Timeline t")
public class Timeline implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TimelinePK id;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(nullable=false, length=2)
	private String type;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to Formation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="idf_account", referencedColumnName="idf_account", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="idf_match", referencedColumnName="idf_match", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="idf_team", referencedColumnName="idf_team", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="session_number", referencedColumnName="session_number", nullable=false, insertable=false, updatable=false)
		})
	private Formation formation;

	public Timeline() {
	}

	public TimelinePK getId() {
		return this.id;
	}

	public void setId(TimelinePK id) {
		this.id = id;
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

	public Formation getFormation() {
		return this.formation;
	}

	public void setFormation(Formation formation) {
		this.formation = formation;
	}

}