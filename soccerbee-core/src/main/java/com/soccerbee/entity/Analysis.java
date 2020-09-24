package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the analysis database table.
 * 
 */
@Entity
@Table(name="analysis")
@NamedQuery(name="Analysis.findAll", query="SELECT a FROM Analysis a")
public class Analysis implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=50)
	private String ubsp;

	@Column(nullable=false, length=100)
	private String point;

	@Column(nullable=false, length=100)
	private String point1;

	@Column(nullable=false, length=100)
	private String point2;

	@Column(nullable=false, length=100)
	private String point3;

	@Column(nullable=false, length=100)
	private String point4;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(nullable=false)
	private Integer session;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to Account
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_account", nullable=false)
	private Account account;

	//bi-directional many-to-one association to AnalysisSession
	@OneToMany(mappedBy="analysi")
	private List<AnalysisSession> analysisSessions;

	public Analysis() {
	}

	public String getUbsp() {
		return this.ubsp;
	}

	public void setUbsp(String ubsp) {
		this.ubsp = ubsp;
	}

	public String getPoint() {
		return this.point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public String getPoint1() {
		return this.point1;
	}

	public void setPoint1(String point1) {
		this.point1 = point1;
	}

	public String getPoint2() {
		return this.point2;
	}

	public void setPoint2(String point2) {
		this.point2 = point2;
	}

	public String getPoint3() {
		return this.point3;
	}

	public void setPoint3(String point3) {
		this.point3 = point3;
	}

	public String getPoint4() {
		return this.point4;
	}

	public void setPoint4(String point4) {
		this.point4 = point4;
	}

	public Timestamp getRegDate() {
		return this.regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	public Integer getSession() {
		return this.session;
	}

	public void setSession(Integer session) {
		this.session = session;
	}

	public Timestamp getUpdDate() {
		return this.updDate;
	}

	public void setUpdDate(Timestamp updDate) {
		this.updDate = updDate;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<AnalysisSession> getAnalysisSessions() {
		return this.analysisSessions;
	}

	public void setAnalysisSessions(List<AnalysisSession> analysisSessions) {
		this.analysisSessions = analysisSessions;
	}

	public AnalysisSession addAnalysisSession(AnalysisSession analysisSession) {
		getAnalysisSessions().add(analysisSession);
		analysisSession.setAnalysi(this);

		return analysisSession;
	}

	public AnalysisSession removeAnalysisSession(AnalysisSession analysisSession) {
		getAnalysisSessions().remove(analysisSession);
		analysisSession.setAnalysi(null);

		return analysisSession;
	}

}