package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the formation database table.
 * 
 */
@Entity
@Table(name="formation")
@NamedQuery(name="Formation.findAll", query="SELECT f FROM Formation f")
public class Formation implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FormationPK id;

	@Column(nullable=false)
	private Boolean captain;

	@Column(name="idf_account_shift")
	private Integer idfAccountShift;

	@Column(length=15)
	private String location;

	@Column(length=3)
	private String position;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(length=1)
	private String replacement;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to ScheduleMatch
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="idf_account", referencedColumnName="idf_account", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="idf_match", referencedColumnName="idf_match", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="idf_team", referencedColumnName="idf_team", nullable=false, insertable=false, updatable=false)
		})
	private ScheduleMatch scheduleMatch;

	//bi-directional many-to-one association to Timeline
	@OneToMany(mappedBy="formation")
	private List<Timeline> timelines;

	public Formation() {
	}

	public FormationPK getId() {
		return this.id;
	}

	public void setId(FormationPK id) {
		this.id = id;
	}

	public Boolean getCaptain() {
		return this.captain;
	}

	public void setCaptain(Boolean captain) {
		this.captain = captain;
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

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
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

	public Timestamp getUpdDate() {
		return this.updDate;
	}

	public void setUpdDate(Timestamp updDate) {
		this.updDate = updDate;
	}

	public ScheduleMatch getScheduleMatch() {
		return this.scheduleMatch;
	}

	public void setScheduleMatch(ScheduleMatch scheduleMatch) {
		this.scheduleMatch = scheduleMatch;
	}

	public List<Timeline> getTimelines() {
		return this.timelines;
	}

	public void setTimelines(List<Timeline> timelines) {
		this.timelines = timelines;
	}

	public Timeline addTimeline(Timeline timeline) {
		getTimelines().add(timeline);
		timeline.setFormation(this);

		return timeline;
	}

	public Timeline removeTimeline(Timeline timeline) {
		getTimelines().remove(timeline);
		timeline.setFormation(null);

		return timeline;
	}

}