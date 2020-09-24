package com.soccerbee.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.MatchAnalysisSessionFormation;
import com.soccerbee.entity.MatchAnalysisSessionFormationPK;

public interface MatchAnalysisSessionFormationRepo
    extends JpaRepository<MatchAnalysisSessionFormation, MatchAnalysisSessionFormationPK> {
  List<MatchAnalysisSessionFormation> findByIdIdfMatch(Integer idfMatch);

  List<MatchAnalysisSessionFormation> findByIdIdfMatchAndIdIdfAccount(Integer idfMatch,
      Integer idfAccount);
}
