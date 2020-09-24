package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the match_hitmap database table.
 * 
 */
@Entity
@Table(name="match_hitmap")
@NamedQuery(name="MatchHitmap.findAll", query="SELECT m FROM MatchHitmap m")
public class MatchHitmap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idf_match_hitmap", unique=true, nullable=false)
	private int idfMatchHitmap;

	@Column(nullable=false)
	private Integer frequency;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	@Column(nullable=false)
	private Integer zone;

	//bi-directional many-to-one association to MatchAnalysisSessionFormation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="idf_account", referencedColumnName="idf_account", nullable=false),
		@JoinColumn(name="idf_match", referencedColumnName="idf_match", nullable=false),
		@JoinColumn(name="number", referencedColumnName="number", nullable=false)
		})
	private MatchAnalysisSessionFormation matchAnalysisSessionFormation;

	public MatchHitmap() {
	}

	public int getIdfMatchHitmap() {
		return this.idfMatchHitmap;
	}

	public void setIdfMatchHitmap(int idfMatchHitmap) {
		this.idfMatchHitmap = idfMatchHitmap;
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

	public MatchAnalysisSessionFormation getMatchAnalysisSessionFormation() {
		return this.matchAnalysisSessionFormation;
	}

	public void setMatchAnalysisSessionFormation(MatchAnalysisSessionFormation matchAnalysisSessionFormation) {
		this.matchAnalysisSessionFormation = matchAnalysisSessionFormation;
	}

}