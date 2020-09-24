package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the sprint database table.
 * 
 */
@Entity
@Table(name="sprint")
@NamedQuery(name="Sprint.findAll", query="SELECT s FROM Sprint s")
public class Sprint implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idf_sprint", unique=true, nullable=false)
	private int idfSprint;

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

	//bi-directional many-to-one association to AnalysisSession
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="number", referencedColumnName="number", nullable=false),
		@JoinColumn(name="ubsp", referencedColumnName="ubsp", nullable=false)
		})
	private AnalysisSession analysisSession;

	public Sprint() {
	}

	public int getIdfSprint() {
		return this.idfSprint;
	}

	public void setIdfSprint(int idfSprint) {
		this.idfSprint = idfSprint;
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

	public AnalysisSession getAnalysisSession() {
		return this.analysisSession;
	}

	public void setAnalysisSession(AnalysisSession analysisSession) {
		this.analysisSession = analysisSession;
	}

}