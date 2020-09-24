package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the ability database table.
 * 
 */
@Entity
@Table(name="ability")
@NamedQuery(name="Ability.findAll", query="SELECT a FROM Ability a")
public class Ability implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idf_ability", unique=true, nullable=false)
	private Integer idfAbility;

	@Column(nullable=false, precision=10, scale=9)
	private BigDecimal acceleration;

	@Column(nullable=false, precision=10, scale=9)
	private BigDecimal activity;

	@Column(nullable=false, precision=10, scale=9)
	private BigDecimal agility;

	@Column(precision=10, scale=9)
	private BigDecimal attack;

	@Column(name="condition_", nullable=false, precision=10, scale=9)
	private BigDecimal condition;

	@Column(precision=10, scale=9)
	private BigDecimal defense;

	@Column(nullable=false, length=3)
	private String position;

	@Column(nullable=false, precision=10, scale=9)
	private BigDecimal rate;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(nullable=false, precision=10, scale=9)
	private BigDecimal speed;

	@Column(nullable=false, precision=10, scale=9)
	private BigDecimal stamina;

	@Column(precision=10, scale=9)
	private BigDecimal teamwork;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to Account
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_account")
	private Account account;

	//bi-directional many-to-one association to AnalysisSession
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="number_p", referencedColumnName="number"),
		@JoinColumn(name="ubsp", referencedColumnName="ubsp")
		})
	private AnalysisSession analysisSession;

	//bi-directional many-to-one association to MatchAnalysisSession
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="idf_match", referencedColumnName="idf_match"),
		@JoinColumn(name="number_t", referencedColumnName="number")
		})
	private MatchAnalysisSession matchAnalysisSession;

	public Ability() {
	}

	public Integer getIdfAbility() {
		return this.idfAbility;
	}

	public void setIdfAbility(Integer idfAbility) {
		this.idfAbility = idfAbility;
	}

	public BigDecimal getAcceleration() {
		return this.acceleration;
	}

	public void setAcceleration(BigDecimal acceleration) {
		this.acceleration = acceleration;
	}

	public BigDecimal getActivity() {
		return this.activity;
	}

	public void setActivity(BigDecimal activity) {
		this.activity = activity;
	}

	public BigDecimal getAgility() {
		return this.agility;
	}

	public void setAgility(BigDecimal agility) {
		this.agility = agility;
	}

	public BigDecimal getAttack() {
		return this.attack;
	}

	public void setAttack(BigDecimal attack) {
		this.attack = attack;
	}

	public BigDecimal getCondition() {
		return this.condition;
	}

	public void setCondition(BigDecimal condition) {
		this.condition = condition;
	}

	public BigDecimal getDefense() {
		return this.defense;
	}

	public void setDefense(BigDecimal defense) {
		this.defense = defense;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public BigDecimal getRate() {
		return this.rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Timestamp getRegDate() {
		return this.regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
	}

	public BigDecimal getSpeed() {
		return this.speed;
	}

	public void setSpeed(BigDecimal speed) {
		this.speed = speed;
	}

	public BigDecimal getStamina() {
		return this.stamina;
	}

	public void setStamina(BigDecimal stamina) {
		this.stamina = stamina;
	}

	public BigDecimal getTeamwork() {
		return this.teamwork;
	}

	public void setTeamwork(BigDecimal teamwork) {
		this.teamwork = teamwork;
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

	public AnalysisSession getAnalysisSession() {
		return this.analysisSession;
	}

	public void setAnalysisSession(AnalysisSession analysisSession) {
		this.analysisSession = analysisSession;
	}

	public MatchAnalysisSession getMatchAnalysisSession() {
		return this.matchAnalysisSession;
	}

	public void setMatchAnalysisSession(MatchAnalysisSession matchAnalysisSession) {
		this.matchAnalysisSession = matchAnalysisSession;
	}

}