package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the match_analysis_session_formation database table.
 * 
 */
@Entity
@Table(name="match_analysis_session_formation")
@NamedQuery(name="MatchAnalysisSessionFormation.findAll", query="SELECT m FROM MatchAnalysisSessionFormation m")
public class MatchAnalysisSessionFormation implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MatchAnalysisSessionFormationPK id;

	@Column(nullable=false)
	private Boolean captain;

	@Column(precision=10, scale=2)
	private BigDecimal coverage;

	@Column(precision=10, scale=2)
	private BigDecimal distance;

	private Integer duration;

	@Column(name="idf_account_shift")
	private Integer idfAccountShift;

	@Column(length=15)
	private String location;

	@Lob
	@Column(name="pitch_x")
	private String pitchX;

	@Lob
	@Column(name="pitch_y")
	private String pitchY;

	@Column(length=3)
	private String position;

	@Column(precision=10, scale=2)
	private BigDecimal rate;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(length=1)
	private String replacement;

	@Column(name="speed_avg", precision=10, scale=2)
	private BigDecimal speedAvg;

	@Column(name="speed_max", precision=10, scale=2)
	private BigDecimal speedMax;

	private Integer sprint;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to Account
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_account", nullable=false, insertable=false, updatable=false)
	private Account account;

	//bi-directional many-to-one association to MatchAnalysisSession
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="idf_match", referencedColumnName="idf_match", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="number", referencedColumnName="number", nullable=false, insertable=false, updatable=false)
		})
	private MatchAnalysisSession matchAnalysisSession;

	//bi-directional many-to-one association to MatchHitmap
	@OneToMany(mappedBy="matchAnalysisSessionFormation")
	private List<MatchHitmap> matchHitmaps;

	//bi-directional many-to-one association to MatchSprint
	@OneToMany(mappedBy="matchAnalysisSessionFormation")
	private List<MatchSprint> matchSprints;

	public MatchAnalysisSessionFormation() {
	}

	public MatchAnalysisSessionFormationPK getId() {
		return this.id;
	}

	public void setId(MatchAnalysisSessionFormationPK id) {
		this.id = id;
	}

	public Boolean getCaptain() {
		return this.captain;
	}

	public void setCaptain(Boolean captain) {
		this.captain = captain;
	}

	public BigDecimal getCoverage() {
		return this.coverage;
	}

	public void setCoverage(BigDecimal coverage) {
		this.coverage = coverage;
	}

	public BigDecimal getDistance() {
		return this.distance;
	}

	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	public Integer getDuration() {
		return this.duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getIdfAccountShift() {
		return this.idfAccountShift;
	}

	public void setIdfAccountShift(Integer idfAccountShift) {
		this.idfAccountShift = idfAccountShift;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPitchX() {
		return this.pitchX;
	}

	public void setPitchX(String pitchX) {
		this.pitchX = pitchX;
	}

	public String getPitchY() {
		return this.pitchY;
	}

	public void setPitchY(String pitchY) {
		this.pitchY = pitchY;
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

	public String getReplacement() {
		return this.replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	public BigDecimal getSpeedAvg() {
		return this.speedAvg;
	}

	public void setSpeedAvg(BigDecimal speedAvg) {
		this.speedAvg = speedAvg;
	}

	public BigDecimal getSpeedMax() {
		return this.speedMax;
	}

	public void setSpeedMax(BigDecimal speedMax) {
		this.speedMax = speedMax;
	}

	public Integer getSprint() {
		return this.sprint;
	}

	public void setSprint(Integer sprint) {
		this.sprint = sprint;
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

	public MatchAnalysisSession getMatchAnalysisSession() {
		return this.matchAnalysisSession;
	}

	public void setMatchAnalysisSession(MatchAnalysisSession matchAnalysisSession) {
		this.matchAnalysisSession = matchAnalysisSession;
	}

	public List<MatchHitmap> getMatchHitmaps() {
		return this.matchHitmaps;
	}

	public void setMatchHitmaps(List<MatchHitmap> matchHitmaps) {
		this.matchHitmaps = matchHitmaps;
	}

	public MatchHitmap addMatchHitmap(MatchHitmap matchHitmap) {
		getMatchHitmaps().add(matchHitmap);
		matchHitmap.setMatchAnalysisSessionFormation(this);

		return matchHitmap;
	}

	public MatchHitmap removeMatchHitmap(MatchHitmap matchHitmap) {
		getMatchHitmaps().remove(matchHitmap);
		matchHitmap.setMatchAnalysisSessionFormation(null);

		return matchHitmap;
	}

	public List<MatchSprint> getMatchSprints() {
		return this.matchSprints;
	}

	public void setMatchSprints(List<MatchSprint> matchSprints) {
		this.matchSprints = matchSprints;
	}

	public MatchSprint addMatchSprint(MatchSprint matchSprint) {
		getMatchSprints().add(matchSprint);
		matchSprint.setMatchAnalysisSessionFormation(this);

		return matchSprint;
	}

	public MatchSprint removeMatchSprint(MatchSprint matchSprint) {
		getMatchSprints().remove(matchSprint);
		matchSprint.setMatchAnalysisSessionFormation(null);

		return matchSprint;
	}

}