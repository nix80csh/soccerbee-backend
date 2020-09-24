package com.soccerbee.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.Account;
import com.soccerbee.entity.AccountPlayer;

public interface AccountPlayerRepo extends JpaRepository<AccountPlayer, Integer> {

}
