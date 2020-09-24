package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the schedule_match database table.
 * 
 */
@Entity
@Table(name="schedule_match")
@NamedQuery(name="ScheduleMatch.findAll", query="SELECT s FROM ScheduleMatch s")
public class ScheduleMatch implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ScheduleMatchPK id;

	@Column(nullable=false)
	private boolean attendance;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	private Boolean vote;

	//bi-directional many-to-one association to Formation
	@OneToMany(mappedBy="scheduleMatch")
	private List<Formation> formations;

	//bi-directional many-to-one association to Match
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_match", nullable=false, insertable=false, updatable=false)
	private Match match;

	//bi-directional many-to-one association to TeamAccount
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="idf_account", referencedColumnName="idf_account", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="idf_team", referencedColumnName="idf_team", nullable=false, insertable=false, updatable=false)
		})
	private TeamAccount teamAccount;

	public ScheduleMatch() {
	}

	public ScheduleMatchPK getId() {
		return this.id;
	}

	public void setId(ScheduleMatchPK id) {
		this.id = id;
	}

	public boolean getAttendance() {
		return this.attendance;
	}

	public void setAttendance(boolean attendance) {
		this.attendance = attendance;
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

	public Boolean getVote() {
		return this.vote;
	}

	public void setVote(Boolean vote) {
		this.vote = vote;
	}

	public List<Formation> getFormations() {
		return this.formations;
	}

	public void setFormations(List<Formation> formations) {
		this.formations = formations;
	}

	public Formation addFormation(Formation formation) {
		getFormations().add(formation);
		formation.setScheduleMatch(this);

		return formation;
	}

	public Formation removeFormation(Formation formation) {
		getFormations().remove(formation);
		formation.setScheduleMatch(null);

		return formation;
	}

	public Match getMatch() {
		return this.match;
	}

	public void setMatch(Match match) {
		this.match = match;
	}

	public TeamAccount getTeamAccount() {
		return this.teamAccount;
	}

	public void setTeamAccount(TeamAccount teamAccount) {
		this.teamAccount = teamAccount;
	}

}