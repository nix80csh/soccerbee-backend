package com.soccerbee.api.pod;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soccerbee.common.EnumResponse;
import com.soccerbee.entity.Pod;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.PodRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class PodService {
  @Autowired PodRepo podRepo;

  public EnumResponse regist(PodDto podDto) {
    for (String idfPod : podDto.getIdfPods()) {
      if (podRepo.existsById(idfPod))
        throw new LogicException(LogicErrorList.DuplicateEntity_Pod);

      Pod pod = new Pod();
      pod.setIdfPod(idfPod);
      pod.setType(podDto.getType());
      podRepo.save(pod);
    }
    return EnumResponse.Registered;
  }

  public List<PodDto> getList() {
    List<PodDto> podDtoList = new ArrayList<PodDto>();
    for (Pod pod : podRepo.findAll()) {
      PodDto podDto = new PodDto();
      podDto.setIdfPod(pod.getIdfPod());
      podDto.setType(pod.getType());
      podDto.setRegDate(pod.getRegDate());
      podDtoList.add(podDto);
    }
    return podDtoList;
  }

  public EnumResponse delete(String idfPod) {
    Pod pod = podRepo.findById(idfPod)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Pod));

    podRepo.delete(pod);
    return EnumResponse.Deleted;
  }
}
