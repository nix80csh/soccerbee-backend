package com.soccerbee.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.MatchAnalysisAccount;
import com.soccerbee.entity.MatchAnalysisAccountPK;

public interface MatchAnalysisAccountRepo
    extends JpaRepository<MatchAnalysisAccount, MatchAnalysisAccountPK> {

  int countByIdIdfMatchAndAnalyzed(int idfMatch, boolean analyzed);

}
