package com.soccerbee.api.firmware;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.soccerbee.common.EnumResponse;
import com.soccerbee.entity.Firmware;
import com.soccerbee.entity.FirmwarePK;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.FirmwareRepo;
import com.soccerbee.util.AmazonS3Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class FirmwareService {
  @Value("${spring.profiles.active}") private String deployType;
  @Autowired FirmwareRepo firmwareRepo;

  public EnumResponse regist(FirmwareDto firmwareDto) {
    FirmwarePK id = new FirmwarePK();
    id.setType(firmwareDto.getType());
    id.setVersion(firmwareDto.getVersion());
    if (firmwareRepo.existsById(id))
      throw new LogicException(LogicErrorList.DuplicateEntity_Firmware);

    AmazonS3Util.uploadFile("soccerbee-" + deployType,
        "firmware/" + firmwareDto.getType() + firmwareDto.getVersion() + ".zip",
        firmwareDto.getFile(), "application/json");
    Firmware firmware = new Firmware();
    firmware.setId(id);
    firmwareRepo.save(firmware);
    return EnumResponse.Registered;
  }

  public List<FirmwareDto> getList() {
    List<Firmware> firmwareList = firmwareRepo.findAll();
    List<FirmwareDto> firmwareDtoList = new ArrayList<FirmwareDto>();
    for (Firmware firmware : firmwareList) {
      FirmwareDto firmwareDto = new FirmwareDto();
      firmwareDto.setType(firmware.getId().getType());
      firmwareDto.setVersion(firmware.getId().getVersion());
      firmwareDtoList.add(firmwareDto);
    }
    return firmwareDtoList;
  }

  public EnumResponse delete(String type, String version) {
    FirmwarePK id = new FirmwarePK();
    id.setType(type);
    id.setVersion(version);
    Firmware firmware = firmwareRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Firmware));

    AmazonS3Util.deleteFile("soccerbee-" + deployType, "firmware/" + type + version + ".zip");
    firmwareRepo.delete(firmware);
    return EnumResponse.Deleted;
  }
}
