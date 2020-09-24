package com.soccerbee.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.Sprint;

public interface SprintRepo extends JpaRepository<Sprint, Integer> {
  List<Sprint> findByAnalysisSessionIdUbsp(String ubsp);

  void deleteByAnalysisSessionIdUbsp(String ubsp);
}
