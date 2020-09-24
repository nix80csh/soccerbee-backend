package com.soccerbee.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.Timeline;
import com.soccerbee.entity.TimelinePK;

public interface TimelineRepo extends JpaRepository<Timeline, TimelinePK> {

  List<Timeline> findByIdIdfMatchAndIdIdfTeamAndIdIdfAccountAndIdSessionNumber(int idfMatch,
      int idfTeam, int idfAccount, int sessionNumber);

  List<Timeline> findByIdIdfMatch(int idfMatch);
}
