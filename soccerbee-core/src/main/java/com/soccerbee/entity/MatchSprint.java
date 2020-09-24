package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the match_sprint database table.
 * 
 */
@Entity
@Table(name="match_sprint")
@NamedQuery(name="MatchSprint.findAll", query="SELECT m FROM MatchSprint m")
public class MatchSprint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idf_match_sprint", unique=true, nullable=false)
	private int idfMatchSprint;

	@Column(nullable=false)
	private Integer grade;

	@Column(name="point_finish", nullable=false, length=20)
	private String pointFinish;

	@Column(name="point_start", nullable=false, length=20)
	private String pointStart;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to MatchAnalysisSessionFormation
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="idf_account", referencedColumnName="idf_account", nullable=false),
		@JoinColumn(name="idf_match", referencedColumnName="idf_match", nullable=false),
		@JoinColumn(name="number", referencedColumnName="number", nullable=false)
		})
	private MatchAnalysisSessionFormation matchAnalysisSessionFormation;

	public MatchSprint() {
	}

	public int getIdfMatchSprint() {
		return this.idfMatchSprint;
	}

	public void setIdfMatchSprint(int idfMatchSprint) {
		this.idfMatchSprint = idfMatchSprint;
	}

	public Integer getGrade() {
		return this.grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getPointFinish() {
		return this.pointFinish;
	}

	public void setPointFinish(String pointFinish) {
		this.pointFinish = pointFinish;
	}

	public String getPointStart() {
		return this.pointStart;
	}

	public void setPointStart(String pointStart) {
		this.pointStart = pointStart;
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

	public MatchAnalysisSessionFormation getMatchAnalysisSessionFormation() {
		return this.matchAnalysisSessionFormation;
	}

	public void setMatchAnalysisSessionFormation(MatchAnalysisSessionFormation matchAnalysisSessionFormation) {
		this.matchAnalysisSessionFormation = matchAnalysisSessionFormation;
	}

}