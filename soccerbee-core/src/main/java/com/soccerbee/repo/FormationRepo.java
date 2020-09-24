package com.soccerbee.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.Formation;
import com.soccerbee.entity.FormationPK;

public interface FormationRepo extends JpaRepository<Formation, FormationPK> {
  List<Formation> findByIdIdfMatch(Integer idfMatch);

  List<Formation> findByIdIdfMatchAndIdIdfTeam(Integer idfMatch, Integer idfTeam);

  List<Formation> findByIdIdfMatchAndIdIdfTeamAndIdIdfAccount(Integer idfMatch, Integer idfTeam,
      Integer idfAccount);
}
