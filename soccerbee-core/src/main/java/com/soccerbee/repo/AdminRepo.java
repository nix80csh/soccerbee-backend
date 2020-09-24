package com.soccerbee.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.Admin;

public interface AdminRepo extends JpaRepository<Admin, Integer> {
  Optional<Admin> findByEmail(String email);

  Boolean existsByEmail(String email);

}
