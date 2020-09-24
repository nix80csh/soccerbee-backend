package com.soccerbee.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.TeamOpponent;
import com.soccerbee.entity.TeamOpponentPK;

public interface TeamOpponentRepo extends JpaRepository<TeamOpponent, TeamOpponentPK> {

}
