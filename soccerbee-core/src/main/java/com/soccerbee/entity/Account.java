package com.soccerbee.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the account database table.
 * 
 */
@Entity
@Table(name="account")
@NamedQuery(name="Account.findAll", query="SELECT a FROM Account a")
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="idf_account", unique=true, nullable=false)
	private Integer idfAccount;

	@Column(nullable=false)
	private Boolean agreement;

	@Column(name="auth_code", length=13)
	private String authCode;

	@Column(nullable=false, length=30)
	private String email;

	@Column(nullable=false, length=30)
	private String name;

	@Column(nullable=false, length=64)
	private String password;

	@Column(name="reg_date", insertable=false, updatable=false, nullable=false)
	private Timestamp regDate;

	@Column(name="upd_date", insertable=false, updatable=false, nullable=false)
	private Timestamp updDate;

	//bi-directional many-to-one association to Ability
	@OneToMany(mappedBy="account")
	private List<Ability> abilities;

	//bi-directional one-to-one association to Pod
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idf_pod")
	private Pod pod;

	//bi-directional many-to-one association to AccountDevice
	@OneToMany(mappedBy="account")
	private List<AccountDevice> accountDevices;

	//bi-directional one-to-one association to AccountPlayer
	@OneToOne(mappedBy="account", fetch=FetchType.LAZY)
	private AccountPlayer accountPlayer;

	//bi-directional many-to-one association to Analysis
	@OneToMany(mappedBy="account")
	private List<Analysis> analysis;

	//bi-directional many-to-one association to Match
	@OneToMany(mappedBy="account")
	private List<Match> matches;

	//bi-directional many-to-one association to MatchAnalysisAccount
	@OneToMany(mappedBy="account")
	private List<MatchAnalysisAccount> matchAnalysisAccounts;

	//bi-directional many-to-one association to MatchAnalysisSessionFormation
	@OneToMany(mappedBy="account")
	private List<MatchAnalysisSessionFormation> matchAnalysisSessionFormations;

	//bi-directional many-to-one association to Team
	@OneToMany(mappedBy="account")
	private List<Team> teams;

	//bi-directional many-to-one association to TeamAccount
	@OneToMany(mappedBy="account")
	private List<TeamAccount> teamAccounts;

	public Account() {
	}

	public Integer getIdfAccount() {
		return this.idfAccount;
	}

	public void setIdfAccount(Integer idfAccount) {
		this.idfAccount = idfAccount;
	}

	public Boolean getAgreement() {
		return this.agreement;
	}

	public void setAgreement(Boolean agreement) {
		this.agreement = agreement;
	}

	public String getAuthCode() {
		return this.authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public List<Ability> getAbilities() {
		return this.abilities;
	}

	public void setAbilities(List<Ability> abilities) {
		this.abilities = abilities;
	}

	public Ability addAbility(Ability ability) {
		getAbilities().add(ability);
		ability.setAccount(this);

		return ability;
	}

	public Ability removeAbility(Ability ability) {
		getAbilities().remove(ability);
		ability.setAccount(null);

		return ability;
	}

	public Pod getPod() {
		return this.pod;
	}

	public void setPod(Pod pod) {
		this.pod = pod;
	}

	public List<AccountDevice> getAccountDevices() {
		return this.accountDevices;
	}

	public void setAccountDevices(List<AccountDevice> accountDevices) {
		this.accountDevices = accountDevices;
	}

	public AccountDevice addAccountDevice(AccountDevice accountDevice) {
		getAccountDevices().add(accountDevice);
		accountDevice.setAccount(this);

		return accountDevice;
	}

	public AccountDevice removeAccountDevice(AccountDevice accountDevice) {
		getAccountDevices().remove(accountDevice);
		accountDevice.setAccount(null);

		return accountDevice;
	}

	public AccountPlayer getAccountPlayer() {
		return this.accountPlayer;
	}

	public void setAccountPlayer(AccountPlayer accountPlayer) {
		this.accountPlayer = accountPlayer;
	}

	public List<Analysis> getAnalysis() {
		return this.analysis;
	}

	public void setAnalysis(List<Analysis> analysis) {
		this.analysis = analysis;
	}

	public Analysis addAnalysi(Analysis analysi) {
		getAnalysis().add(analysi);
		analysi.setAccount(this);

		return analysi;
	}

	public Analysis removeAnalysi(Analysis analysi) {
		getAnalysis().remove(analysi);
		analysi.setAccount(null);

		return analysi;
	}

	public List<Match> getMatches() {
		return this.matches;
	}

	public void setMatches(List<Match> matches) {
		this.matches = matches;
	}

	public Match addMatch(Match match) {
		getMatches().add(match);
		match.setAccount(this);

		return match;
	}

	public Match removeMatch(Match match) {
		getMatches().remove(match);
		match.setAccount(null);

		return match;
	}

	public List<MatchAnalysisAccount> getMatchAnalysisAccounts() {
		return this.matchAnalysisAccounts;
	}

	public void setMatchAnalysisAccounts(List<MatchAnalysisAccount> matchAnalysisAccounts) {
		this.matchAnalysisAccounts = matchAnalysisAccounts;
	}

	public MatchAnalysisAccount addMatchAnalysisAccount(MatchAnalysisAccount matchAnalysisAccount) {
		getMatchAnalysisAccounts().add(matchAnalysisAccount);
		matchAnalysisAccount.setAccount(this);

		return matchAnalysisAccount;
	}

	public MatchAnalysisAccount removeMatchAnalysisAccount(MatchAnalysisAccount matchAnalysisAccount) {
		getMatchAnalysisAccounts().remove(matchAnalysisAccount);
		matchAnalysisAccount.setAccount(null);

		return matchAnalysisAccount;
	}

	public List<MatchAnalysisSessionFormation> getMatchAnalysisSessionFormations() {
		return this.matchAnalysisSessionFormations;
	}

	public void setMatchAnalysisSessionFormations(List<MatchAnalysisSessionFormation> matchAnalysisSessionFormations) {
		this.matchAnalysisSessionFormations = matchAnalysisSessionFormations;
	}

	public MatchAnalysisSessionFormation addMatchAnalysisSessionFormation(MatchAnalysisSessionFormation matchAnalysisSessionFormation) {
		getMatchAnalysisSessionFormations().add(matchAnalysisSessionFormation);
		matchAnalysisSessionFormation.setAccount(this);

		return matchAnalysisSessionFormation;
	}

	public MatchAnalysisSessionFormation removeMatchAnalysisSessionFormation(MatchAnalysisSessionFormation matchAnalysisSessionFormation) {
		getMatchAnalysisSessionFormations().remove(matchAnalysisSessionFormation);
		matchAnalysisSessionFormation.setAccount(null);

		return matchAnalysisSessionFormation;
	}

	public List<Team> getTeams() {
		return this.teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public Team addTeam(Team team) {
		getTeams().add(team);
		team.setAccount(this);

		return team;
	}

	public Team removeTeam(Team team) {
		getTeams().remove(team);
		team.setAccount(null);

		return team;
	}

	public List<TeamAccount> getTeamAccounts() {
		return this.teamAccounts;
	}

	public void setTeamAccounts(List<TeamAccount> teamAccounts) {
		this.teamAccounts = teamAccounts;
	}

	public TeamAccount addTeamAccount(TeamAccount teamAccount) {
		getTeamAccounts().add(teamAccount);
		teamAccount.setAccount(this);

		return teamAccount;
	}

	public TeamAccount removeTeamAccount(TeamAccount teamAccount) {
		getTeamAccounts().remove(teamAccount);
		teamAccount.setAccount(null);

		return teamAccount;
	}

}