package com.soccerbee.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.AnalysisSession;
import com.soccerbee.entity.AnalysisSessionPK;

public interface AnalysisSessionRepo extends JpaRepository<AnalysisSession, AnalysisSessionPK> {
}
