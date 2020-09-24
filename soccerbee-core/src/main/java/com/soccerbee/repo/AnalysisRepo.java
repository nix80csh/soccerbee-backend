package com.soccerbee.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.Analysis;

public interface AnalysisRepo extends JpaRepository<Analysis, String> {
}
