package com.soccerbee.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.MatchSprint;

public interface MatchSprintRepo extends JpaRepository<MatchSprint, Integer> {
  List<MatchSprint> findByMatchAnalysisSessionFormationIdIdfMatchAndMatchAnalysisSessionFormationIdIdfAccount(
      Integer idfMatch, Integer idfAccount);

  void deleteByMatchAnalysisSessionFormationIdIdfMatchAndMatchAnalysisSessionFormationIdIdfAccount(
      Integer idfMatch, Integer idfAccount);
}
