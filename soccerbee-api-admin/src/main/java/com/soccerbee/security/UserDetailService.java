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

import com.soccerbee.entity.Admin;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.AdminRepo;


@Component
public class UserDetailService implements UserDetailsService {

  @Autowired AdminRepo adminRepo;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    Admin admin = adminRepo.findByEmail(userId)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Admin));
    List<GrantedAuthority> authorityListUser = AuthorityUtils.createAuthorityList("ROLE_USER");
    return new User(admin.getEmail(), admin.getPassword(), authorityListUser);
  }
}
