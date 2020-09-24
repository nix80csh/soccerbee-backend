package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the team_opponent database table.
 * 
 */
@Entity
@Table(name="team_opponent")
@NamedQuery(name="TeamOpponent.findAll", query="SELECT t FROM TeamOpponent t")
public class TeamOpponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private TeamOpponentPK id;

	@Column(length=255)
	private String description;

	@Column(length=100)
	private String image;

	@Column(nullable=false, length=30)
	private String name;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	@Column(nullable=false)
	private Boolean visible;

	//bi-directional many-to-one association to Match
	@OneToMany(mappedBy="teamOpponent")
	private List<Match> matches;

	//bi-directional many-to-one association to Team
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_team", nullable=false, insertable=false, updatable=false)
	private Team team;

	public TeamOpponent() {
	}

	public TeamOpponentPK getId() {
		return this.id;
	}

	public void setId(TeamOpponentPK id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Boolean getVisible() {
		return this.visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public List<Match> getMatches() {
		return this.matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	public Match addMatch(Match match) {
		getMatches().add(match);
		match.setTeamOpponent(this);

		return match;
	}

	public Match removeMatch(Match match) {
		getMatches().remove(match);
		match.setTeamOpponent(null);

		return match;
	}

	public Team getTeam() {
		return this.team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

}