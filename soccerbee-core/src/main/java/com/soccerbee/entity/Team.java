package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the team database table.
 * 
 */
@Entity
@Table(name="team")
@NamedQuery(name="Team.findAll", query="SELECT t FROM Team t")
public class Team implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idf_team", unique=true, nullable=false)
	private Integer idfTeam;

	@Column(name="image_emblem", length=100)
	private String imageEmblem;

	@Column(name="image_uniform_3rd", length=100)
	private String imageUniform3rd;

	@Column(name="image_uniform_away", length=100)
	private String imageUniformAway;

	@Column(name="image_uniform_home", length=100)
	private String imageUniformHome;

	@Column(nullable=false, length=30)
	private String name;

	@Column(name="name_abbr", nullable=false, length=4)
	private String nameAbbr;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to Account
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_account")
	private Account account;

	//bi-directional many-to-one association to TeamAccount
	@OneToMany(mappedBy="team")
	private List<TeamAccount> teamAccounts;

	//bi-directional many-to-one association to TeamOpponent
	@OneToMany(mappedBy="team")
	private List<TeamOpponent> teamOpponents;

	public Team() {
	}

	public Integer getIdfTeam() {
		return this.idfTeam;
	}

	public void setIdfTeam(Integer idfTeam) {
		this.idfTeam = idfTeam;
	}

	public String getImageEmblem() {
		return this.imageEmblem;
	}

	public void setImageEmblem(String imageEmblem) {
		this.imageEmblem = imageEmblem;
	}

	public String getImageUniform3rd() {
		return this.imageUniform3rd;
	}

	public void setImageUniform3rd(String imageUniform3rd) {
		this.imageUniform3rd = imageUniform3rd;
	}

	public String getImageUniformAway() {
		return this.imageUniformAway;
	}

	public void setImageUniformAway(String imageUniformAway) {
		this.imageUniformAway = imageUniformAway;
	}

	public String getImageUniformHome() {
		return this.imageUniformHome;
	}

	public void setImageUniformHome(String imageUniformHome) {
		this.imageUniformHome = imageUniformHome;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameAbbr() {
		return this.nameAbbr;
	}

	public void setNameAbbr(String nameAbbr) {
		this.nameAbbr = nameAbbr;
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

	public List<TeamAccount> getTeamAccounts() {
		return this.teamAccounts;
	}

	public void setTeamAccounts(List<TeamAccount> teamAccounts) {
		this.teamAccounts = teamAccounts;
	}

	public TeamAccount addTeamAccount(TeamAccount teamAccount) {
		getTeamAccounts().add(teamAccount);
		teamAccount.setTeam(this);

		return teamAccount;
	}

	public TeamAccount removeTeamAccount(TeamAccount teamAccount) {
		getTeamAccounts().remove(teamAccount);
		teamAccount.setTeam(null);

		return teamAccount;
	}

	public List<TeamOpponent> getTeamOpponents() {
		return this.teamOpponents;
	}

	public void setTeamOpponents(List<TeamOpponent> teamOpponents) {
		this.teamOpponents = teamOpponents;
	}

	public TeamOpponent addTeamOpponent(TeamOpponent teamOpponent) {
		getTeamOpponents().add(teamOpponent);
		teamOpponent.setTeam(this);

		return teamOpponent;
	}

	public TeamOpponent removeTeamOpponent(TeamOpponent teamOpponent) {
		getTeamOpponents().remove(teamOpponent);
		teamOpponent.setTeam(null);

		return teamOpponent;
	}

}