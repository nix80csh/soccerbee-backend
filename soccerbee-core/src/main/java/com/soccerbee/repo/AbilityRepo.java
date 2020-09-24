package com.soccerbee.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.Ability;

public interface AbilityRepo extends JpaRepository<Ability, Integer> {

  List<Ability> findTop7ByAccountIdfAccountAndPositionOrderByIdfAbilityDesc(Integer idfAccount,
      String position);
}
