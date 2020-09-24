package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the hitmap database table.
 * 
 */
@Entity
@Table(name="hitmap")
@NamedQuery(name="Hitmap.findAll", query="SELECT h FROM Hitmap h")
public class Hitmap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idf_hitmap", unique=true, nullable=false)
	private int idfHitmap;

	@Column(nullable=false)
	private Integer frequency;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	@Column(nullable=false)
	private Integer zone;

	//bi-directional many-to-one association to AnalysisSession
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="number", referencedColumnName="number", nullable=false),
		@JoinColumn(name="ubsp", referencedColumnName="ubsp", nullable=false)
		})
	private AnalysisSession analysisSession;

	public Hitmap() {
	}

	public int getIdfHitmap() {
		return this.idfHitmap;
	}

	public void setIdfHitmap(int idfHitmap) {
		this.idfHitmap = idfHitmap;
	}

	public Integer getFrequency() {
		return this.frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
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

	public Integer getZone() {
		return this.zone;
	}

	public void setZone(Integer zone) {
		this.zone = zone;
	}

	public AnalysisSession getAnalysisSession() {
		return this.analysisSession;
	}

	public void setAnalysisSession(AnalysisSession analysisSession) {
		this.analysisSession = analysisSession;
	}

}