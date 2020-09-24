package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the match_ database table.
 * 
 */
@Entity
@Table(name="match_")
@NamedQuery(name="Match.findAll", query="SELECT m FROM Match m")
public class Match implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idf_match", unique=true, nullable=false)
	private Integer idfMatch;

	@Temporal(TemporalType.DATE)
	@Column(name="date_")
	private Date date;

	@Column(name="lat_long", nullable=false, length=100)
	private String latLong;

	@Column(nullable=false, length=30)
	private String location;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(nullable=false)
	private Integer session;

	@Column(name="time_finish", nullable=false)
	private Time timeFinish;

	@Column(name="time_start", nullable=false)
	private Time timeStart;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to Account
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_account")
	private Account account;

	//bi-directional many-to-one association to TeamOpponent
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="idf_team", referencedColumnName="idf_team"),
		@JoinColumn(name="idf_team_opponent", referencedColumnName="idf_team_opponent")
		})
	private TeamOpponent teamOpponent;

	//bi-directional one-to-one association to MatchAnalysis
	@OneToOne(mappedBy="match", fetch=FetchType.LAZY)
	private MatchAnalysis matchAnalysis;

	//bi-directional many-to-one association to ScheduleMatch
	@OneToMany(mappedBy="match")
	private List<ScheduleMatch> scheduleMatches;

	public Match() {
	}

	public Integer getIdfMatch() {
		return this.idfMatch;
	}

	public void setIdfMatch(Integer idfMatch) {
		this.idfMatch = idfMatch;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLatLong() {
		return this.latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public Time getTimeFinish() {
		return this.timeFinish;
	}

	public void setTimeFinish(Time timeFinish) {
		this.timeFinish = timeFinish;
	}

	public Time getTimeStart() {
		return this.timeStart;
	}

	public void setTimeStart(Time timeStart) {
		this.timeStart = timeStart;
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

	public TeamOpponent getTeamOpponent() {
		return this.teamOpponent;
	}

	public void setTeamOpponent(TeamOpponent teamOpponent) {
		this.teamOpponent = teamOpponent;
	}

	public MatchAnalysis getMatchAnalysis() {
		return this.matchAnalysis;
	}

	public void setMatchAnalysis(MatchAnalysis matchAnalysis) {
		this.matchAnalysis = matchAnalysis;
	}

	public List<ScheduleMatch> getScheduleMatches() {
		return this.scheduleMatches;
	}

	public void setScheduleMatches(List<ScheduleMatch> scheduleMatches) {
		this.scheduleMatches = scheduleMatches;
	}

	public ScheduleMatch addScheduleMatch(ScheduleMatch scheduleMatch) {
		getScheduleMatches().add(scheduleMatch);
		scheduleMatch.setMatch(this);

		return scheduleMatch;
	}

	public ScheduleMatch removeScheduleMatch(ScheduleMatch scheduleMatch) {
		getScheduleMatches().remove(scheduleMatch);
		scheduleMatch.setMatch(null);

		return scheduleMatch;
	}

}