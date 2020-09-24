package com.soccerbee.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.Team;

public interface TeamRepo extends JpaRepository<Team, Integer> {

}
