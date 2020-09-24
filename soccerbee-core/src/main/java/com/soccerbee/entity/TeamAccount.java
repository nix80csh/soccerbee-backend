package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the team_account database table.
 * 
 */
@Entity
@Table(name="team_account")
@NamedQuery(name="TeamAccount.findAll", query="SELECT t FROM TeamAccount t")
public class TeamAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TeamAccountPK id;

	@Column(nullable=false, length=1)
	private String grade;

	@Column(nullable=false)
	private int number;

	@Column(nullable=false, length=3)
	private String position;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to ScheduleMatch
	@OneToMany(mappedBy="teamAccount")
	private List<ScheduleMatch> scheduleMatches;

	//bi-directional many-to-one association to Account
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_account", nullable=false, insertable=false, updatable=false)
	private Account account;

	//bi-directional many-to-one association to Team
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_team", nullable=false, insertable=false, updatable=false)
	private Team team;

	public TeamAccount() {
	}

	public TeamAccountPK getId() {
		return this.id;
	}

	public void setId(TeamAccountPK id) {
		this.id = id;
	}

	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
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

	public Timestamp getUpdDate() {
		return this.updDate;
	}

	public void setUpdDate(Timestamp updDate) {
		this.updDate = updDate;
	}

	public List<ScheduleMatch> getScheduleMatches() {
		return this.scheduleMatches;
	}

	public void setScheduleMatches(List<ScheduleMatch> scheduleMatches) {
		this.scheduleMatches = scheduleMatches;
	}

	public ScheduleMatch addScheduleMatch(ScheduleMatch scheduleMatch) {
		getScheduleMatches().add(scheduleMatch);
		scheduleMatch.setTeamAccount(this);

		return scheduleMatch;
	}

	public ScheduleMatch removeScheduleMatch(ScheduleMatch scheduleMatch) {
		getScheduleMatches().remove(scheduleMatch);
		scheduleMatch.setTeamAccount(null);

		return scheduleMatch;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Team getTeam() {
		return this.team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

}