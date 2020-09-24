package com.soccerbee.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.TeamAccount;
import com.soccerbee.entity.TeamAccountPK;

public interface TeamAccountRepo extends JpaRepository<TeamAccount, TeamAccountPK> {

  List<TeamAccount> findByIdIdfTeam(int idfTeam);

  List<TeamAccount> findByIdIdfTeamAndGrade(int idfTeam, String grade);

  List<TeamAccount> findByIdIdfAccount(int idfAccount);
}
