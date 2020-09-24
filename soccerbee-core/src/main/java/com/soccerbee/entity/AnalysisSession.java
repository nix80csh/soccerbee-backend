package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the analysis_session database table.
 * 
 */
@Entity
@Table(name="analysis_session")
@NamedQuery(name="AnalysisSession.findAll", query="SELECT a FROM AnalysisSession a")
public class AnalysisSession implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AnalysisSessionPK id;

	@Column(precision=10, scale=2)
	private BigDecimal coverage;

	@Column(precision=10, scale=2)
	private BigDecimal distance;

	private Integer duration;

	@Lob
	@Column(name="pitch_x")
	private String pitchX;

	@Lob
	@Column(name="pitch_y")
	private String pitchY;

	@Column(nullable=false, length=3)
	private String position;

	@Column(precision=10, scale=2)
	private BigDecimal rate;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(name="speed_avg", precision=10, scale=2)
	private BigDecimal speedAvg;

	@Column(name="speed_max", precision=10, scale=2)
	private BigDecimal speedMax;

	private Integer sprint;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="time_finish", nullable=false)
	private Date timeFinish;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="time_start", nullable=false)
	private Date timeStart;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to Ability
	@OneToMany(mappedBy="analysisSession")
	private List<Ability> abilities;

	//bi-directional many-to-one association to Analysis
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ubsp", nullable=false, insertable=false, updatable=false)
	private Analysis analysi;

	//bi-directional many-to-one association to Hitmap
	@OneToMany(mappedBy="analysisSession")
	private List<Hitmap> hitmaps;

	//bi-directional many-to-one association to Sprint
	@OneToMany(mappedBy="analysisSession")
	private List<Sprint> sprints;

	public AnalysisSession() {
	}

	public AnalysisSessionPK getId() {
		return this.id;
	}

	public void setId(AnalysisSessionPK id) {
		this.id = id;
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

	public List<Ability> getAbilities() {
		return this.abilities;
	}

	public void setAbilities(List<Ability> abilities) {
		this.abilities = abilities;
	}

	public Ability addAbility(Ability ability) {
		getAbilities().add(ability);
		ability.setAnalysisSession(this);

		return ability;
	}

	public Ability removeAbility(Ability ability) {
		getAbilities().remove(ability);
		ability.setAnalysisSession(null);

		return ability;
	}

	public Analysis getAnalysi() {
		return this.analysi;
	}

	public void setAnalysi(Analysis analysi) {
		this.analysi = analysi;
	}

	public List<Hitmap> getHitmaps() {
		return this.hitmaps;
	}

	public void setHitmaps(List<Hitmap> hitmaps) {
		this.hitmaps = hitmaps;
	}

	public Hitmap addHitmap(Hitmap hitmap) {
		getHitmaps().add(hitmap);
		hitmap.setAnalysisSession(this);

		return hitmap;
	}

	public Hitmap removeHitmap(Hitmap hitmap) {
		getHitmaps().remove(hitmap);
		hitmap.setAnalysisSession(null);

		return hitmap;
	}

	public List<Sprint> getSprints() {
		return this.sprints;
	}

	public void setSprints(List<Sprint> sprints) {
		this.sprints = sprints;
	}

	public Sprint addSprint(Sprint sprint) {
		getSprints().add(sprint);
		sprint.setAnalysisSession(this);

		return sprint;
	}

	public Sprint removeSprint(Sprint sprint) {
		getSprints().remove(sprint);
		sprint.setAnalysisSession(null);

		return sprint;
	}

}