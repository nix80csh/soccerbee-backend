package com.soccerbee.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.AccountDevice;
import com.soccerbee.entity.AccountDevicePK;

public interface AccountDeviceRepo extends JpaRepository<AccountDevice, AccountDevicePK> {

}
