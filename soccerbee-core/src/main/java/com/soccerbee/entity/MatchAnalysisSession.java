package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the match_analysis_session database table.
 * 
 */
@Entity
@Table(name="match_analysis_session")
@NamedQuery(name="MatchAnalysisSession.findAll", query="SELECT m FROM MatchAnalysisSession m")
public class MatchAnalysisSession implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MatchAnalysisSessionPK id;

	@Column(name="attack_direction", length=1)
	private String attackDirection;

	@Column(name="attack_rate", precision=10, scale=4)
	private BigDecimal attackRate;

	@Lob
	@Column(name="defense_line")
	private String defenseLine;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="time_finish", nullable=false)
	private Date timeFinish;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="time_start", nullable=false)
	private Date timeStart;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	@Column(name="zone_attack", precision=10, scale=4)
	private BigDecimal zoneAttack;

	@Column(name="zone_center", precision=10, scale=4)
	private BigDecimal zoneCenter;

	@Column(name="zone_defense", precision=10, scale=4)
	private BigDecimal zoneDefense;

	@Column(name="zone_left", precision=10, scale=4)
	private BigDecimal zoneLeft;

	@Column(name="zone_midfield", precision=10, scale=4)
	private BigDecimal zoneMidfield;

	@Column(name="zone_right", precision=10, scale=4)
	private BigDecimal zoneRight;

	//bi-directional many-to-one association to Ability
	@OneToMany(mappedBy="matchAnalysisSession")
	private List<Ability> abilities;

	//bi-directional many-to-one association to MatchAnalysis
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_match", nullable=false, insertable=false, updatable=false)
	private MatchAnalysis matchAnalysis;

	//bi-directional many-to-one association to MatchAnalysisSessionFormation
	@OneToMany(mappedBy="matchAnalysisSession")
	private List<MatchAnalysisSessionFormation> matchAnalysisSessionFormations;

	public MatchAnalysisSession() {
	}

	public MatchAnalysisSessionPK getId() {
		return this.id;
	}

	public void setId(MatchAnalysisSessionPK id) {
		this.id = id;
	}

	public String getAttackDirection() {
		return this.attackDirection;
	}

	public void setAttackDirection(String attackDirection) {
		this.attackDirection = attackDirection;
	}

	public BigDecimal getAttackRate() {
		return this.attackRate;
	}

	public void setAttackRate(BigDecimal attackRate) {
		this.attackRate = attackRate;
	}

	public String getDefenseLine() {
		return this.defenseLine;
	}

	public void setDefenseLine(String defenseLine) {
		this.defenseLine = defenseLine;
	}

	public Timestamp getRegDate() {
		return this.regDate;
	}

	public void setRegDate(Timestamp regDate) {
		this.regDate = regDate;
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

	public BigDecimal getZoneAttack() {
		return this.zoneAttack;
	}

	public void setZoneAttack(BigDecimal zoneAttack) {
		this.zoneAttack = zoneAttack;
	}

	public BigDecimal getZoneCenter() {
		return this.zoneCenter;
	}

	public void setZoneCenter(BigDecimal zoneCenter) {
		this.zoneCenter = zoneCenter;
	}

	public BigDecimal getZoneDefense() {
		return this.zoneDefense;
	}

	public void setZoneDefense(BigDecimal zoneDefense) {
		this.zoneDefense = zoneDefense;
	}

	public BigDecimal getZoneLeft() {
		return this.zoneLeft;
	}

	public void setZoneLeft(BigDecimal zoneLeft) {
		this.zoneLeft = zoneLeft;
	}

	public BigDecimal getZoneMidfield() {
		return this.zoneMidfield;
	}

	public void setZoneMidfield(BigDecimal zoneMidfield) {
		this.zoneMidfield = zoneMidfield;
	}

	public BigDecimal getZoneRight() {
		return this.zoneRight;
	}

	public void setZoneRight(BigDecimal zoneRight) {
		this.zoneRight = zoneRight;
	}

	public List<Ability> getAbilities() {
		return this.abilities;
	}

	public void setAbilities(List<Ability> abilities) {
		this.abilities = abilities;
	}

	public Ability addAbility(Ability ability) {
		getAbilities().add(ability);
		ability.setMatchAnalysisSession(this);

		return ability;
	}

	public Ability removeAbility(Ability ability) {
		getAbilities().remove(ability);
		ability.setMatchAnalysisSession(null);

		return ability;
	}

	public MatchAnalysis getMatchAnalysis() {
		return this.matchAnalysis;
	}

	public void setMatchAnalysis(MatchAnalysis matchAnalysis) {
		this.matchAnalysis = matchAnalysis;
	}

	public List<MatchAnalysisSessionFormation> getMatchAnalysisSessionFormations() {
		return this.matchAnalysisSessionFormations;
	}

	public void setMatchAnalysisSessionFormations(List<MatchAnalysisSessionFormation> matchAnalysisSessionFormations) {
		this.matchAnalysisSessionFormations = matchAnalysisSessionFormations;
	}

	public MatchAnalysisSessionFormation addMatchAnalysisSessionFormation(MatchAnalysisSessionFormation matchAnalysisSessionFormation) {
		getMatchAnalysisSessionFormations().add(matchAnalysisSessionFormation);
		matchAnalysisSessionFormation.setMatchAnalysisSession(this);

		return matchAnalysisSessionFormation;
	}

	public MatchAnalysisSessionFormation removeMatchAnalysisSessionFormation(MatchAnalysisSessionFormation matchAnalysisSessionFormation) {
		getMatchAnalysisSessionFormations().remove(matchAnalysisSessionFormation);
		matchAnalysisSessionFormation.setMatchAnalysisSession(null);

		return matchAnalysisSessionFormation;
	}

}