package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the match_analysis database table.
 * 
 */
@Entity
@Table(name="match_analysis")
@NamedQuery(name="MatchAnalysis.findAll", query="SELECT m FROM MatchAnalysis m")
public class MatchAnalysis implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="idf_match", unique=true, nullable=false)
	private int idfMatch;

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="time_finish", nullable=false)
	private Date timeFinish;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="time_start", nullable=false)
	private Date timeStart;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional one-to-one association to Match
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_match", nullable=false, insertable=false, updatable=false)
	private Match match;

	//bi-directional many-to-one association to MatchAnalysisAccount
	@OneToMany(mappedBy="matchAnalysis")
	private List<MatchAnalysisAccount> matchAnalysisAccounts;

	//bi-directional many-to-one association to MatchAnalysisSession
	@OneToMany(mappedBy="matchAnalysis")
	private List<MatchAnalysisSession> matchAnalysisSessions;

	public MatchAnalysis() {
	}

	public int getIdfMatch() {
		return this.idfMatch;
	}

	public void setIdfMatch(int idfMatch) {
		this.idfMatch = idfMatch;
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

	public Date getTimeFinish() {
		return this.timeFinish;
	}

	public void setTimeFinish(Date timeFinish) {
		this.timeFinish = timeFinish;
	}

	public Date getTimeStart() {
		return this.timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public Timestamp getUpdDate() {
		return this.updDate;
	}

	public void setUpdDate(Timestamp updDate) {
		this.updDate = updDate;
	}

	public Match getMatch() {
		return this.match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public List<MatchAnalysisAccount> getMatchAnalysisAccounts() {
		return this.matchAnalysisAccounts;
	}

	public void setMatchAnalysisAccounts(List<MatchAnalysisAccount> matchAnalysisAccounts) {
		this.matchAnalysisAccounts = matchAnalysisAccounts;
	}

	public MatchAnalysisAccount addMatchAnalysisAccount(MatchAnalysisAccount matchAnalysisAccount) {
		getMatchAnalysisAccounts().add(matchAnalysisAccount);
		matchAnalysisAccount.setMatchAnalysis(this);

		return matchAnalysisAccount;
	}

	public MatchAnalysisAccount removeMatchAnalysisAccount(MatchAnalysisAccount matchAnalysisAccount) {
		getMatchAnalysisAccounts().remove(matchAnalysisAccount);
		matchAnalysisAccount.setMatchAnalysis(null);

		return matchAnalysisAccount;
	}

	public List<MatchAnalysisSession> getMatchAnalysisSessions() {
		return this.matchAnalysisSessions;
	}

	public void setMatchAnalysisSessions(List<MatchAnalysisSession> matchAnalysisSessions) {
		this.matchAnalysisSessions = matchAnalysisSessions;
	}

	public MatchAnalysisSession addMatchAnalysisSession(MatchAnalysisSession matchAnalysisSession) {
		getMatchAnalysisSessions().add(matchAnalysisSession);
		matchAnalysisSession.setMatchAnalysis(this);

		return matchAnalysisSession;
	}

	public MatchAnalysisSession removeMatchAnalysisSession(MatchAnalysisSession matchAnalysisSession) {
		getMatchAnalysisSessions().remove(matchAnalysisSession);
		matchAnalysisSession.setMatchAnalysis(null);

		return matchAnalysisSession;
	}

}