package com.soccerbee.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.Account;

public interface AccountRepo extends JpaRepository<Account, Integer> {
  Optional<Account> findByEmail(String email);

  List<Account> findByName(String name);

  Boolean existsByEmail(String email);

  Integer countByAuthCode(String code);
}
