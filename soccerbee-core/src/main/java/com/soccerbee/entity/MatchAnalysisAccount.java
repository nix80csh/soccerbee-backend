package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the match_analysis_account database table.
 * 
 */
@Entity
@Table(name="match_analysis_account")
@NamedQuery(name="MatchAnalysisAccount.findAll", query="SELECT m FROM MatchAnalysisAccount m")
public class MatchAnalysisAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MatchAnalysisAccountPK id;

	@Column(nullable=false)
	private Boolean analyzed;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to Account
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_account", nullable=false, insertable=false, updatable=false)
	private Account account;

	//bi-directional many-to-one association to MatchAnalysis
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_match", nullable=false, insertable=false, updatable=false)
	private MatchAnalysis matchAnalysis;

	public MatchAnalysisAccount() {
	}

	public MatchAnalysisAccountPK getId() {
		return this.id;
	}

	public void setId(MatchAnalysisAccountPK id) {
		this.id = id;
	}

	public Boolean getAnalyzed() {
		return this.analyzed;
	}

	public void setAnalyzed(Boolean analyzed) {
		this.analyzed = analyzed;
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

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public MatchAnalysis getMatchAnalysis() {
		return this.matchAnalysis;
	}

	public void setMatchAnalysis(MatchAnalysis matchAnalysis) {
		this.matchAnalysis = matchAnalysis;
	}

}