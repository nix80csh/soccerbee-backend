package com.soccerbee.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.MatchAnalysisSession;
import com.soccerbee.entity.MatchAnalysisSessionPK;

public interface MatchAnalysisSessionRepo
    extends JpaRepository<MatchAnalysisSession, MatchAnalysisSessionPK> {

}
