package com.soccerbee.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.ScheduleMatch;
import com.soccerbee.entity.ScheduleMatchPK;

public interface ScheduleMatchRepo extends JpaRepository<ScheduleMatch, ScheduleMatchPK> {
  boolean existsByIdIdfMatchAndIdIdfTeam(Integer idfMatch, Integer idfTeam);

  List<ScheduleMatch> findByIdIdfMatchAndIdIdfTeam(Integer idfMatch, Integer idfTeam);

  List<ScheduleMatch> findByIdIdfAccount(Integer idfAccount);
}
