package com.soccerbee.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.MatchHitmap;

public interface MatchHitmapRepo extends JpaRepository<MatchHitmap, Integer> {
  List<MatchHitmap> findByMatchAnalysisSessionFormationIdIdfMatchAndMatchAnalysisSessionFormationIdIdfAccount(
      Integer idfMatch, Integer idfAccount);

  void deleteByMatchAnalysisSessionFormationIdIdfMatchAndMatchAnalysisSessionFormationIdIdfAccount(
      Integer idfMatch, Integer idfAccount);
}
