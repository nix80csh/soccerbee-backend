package com.soccerbee.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.soccerbee.entity.Account;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.AccountRepo;


@Component
public class UserDetailService implements UserDetailsService {

  @Autowired AccountRepo accountRepo;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    Account account = accountRepo.findByEmail(userId)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Admin));
    List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");
    return new User(account.getEmail(), account.getPassword(), authorityListUser);
  }
}
