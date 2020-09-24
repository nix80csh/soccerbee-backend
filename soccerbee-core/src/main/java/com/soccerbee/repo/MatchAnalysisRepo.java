package com.soccerbee.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.MatchAnalysis;

public interface MatchAnalysisRepo extends JpaRepository<MatchAnalysis, Integer> {
  List<MatchAnalysis> findByMatchIdfMatchIn(List<Integer> idfMatch);
}
