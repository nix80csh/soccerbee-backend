package com.soccerbee.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.Hitmap;

public interface HitmapRepo extends JpaRepository<Hitmap, Integer> {
  List<Hitmap> findByAnalysisSessionIdUbsp(String ubsp);

  void deleteByAnalysisSessionIdUbsp(String ubsp);
}
