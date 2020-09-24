package com.soccerbee.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.Match;

public interface MatchRepo extends JpaRepository<Match, Integer> {

  List<Match> findByTeamOpponentIdIdfTeam(int idfTeam);

  List<Match> findByTeamOpponentIdIdfTeamIn(List<Integer> idfTeam);
}
