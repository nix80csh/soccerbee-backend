package com.soccerbee.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soccerbee.entity.Firmware;
import com.soccerbee.entity.FirmwarePK;

public interface FirmwareRepo extends JpaRepository<Firmware, FirmwarePK> {

  List<Firmware> findByIdType(String type);
}
