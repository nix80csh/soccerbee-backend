package com.soccerbee.api.pod;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.soccerbee.api.pod.PodDto.AvailablePodDto;
import com.soccerbee.api.pod.PodDto.PodOwnerDto;
import com.soccerbee.api.pod.PodDto.TeamDto;
import com.soccerbee.api.pod.PodDto.UnanalyzedMatchDto;
import com.soccerbee.common.EnumResponse;

@RestController
@RequestMapping("/pod")
public class PodController {
  @Autowired PodService podService;

  @GetMapping("/checkPodOwner/{idfPod}")
  public PodOwnerDto checkPodOwner(@PathVariable String idfPod) {
    return podService.checkPodOwner(idfPod);
  }

  @GetMapping("/getPodInfo/{idfAccount}/{idfPod}")
  public PodDto getPodInfo(@PathVariable int idfAccount, @PathVariable String idfPod) {
    return podService.getPodInfo(idfAccount, idfPod);
  }

  @GetMapping("/getManagedTeamList/{idfAccount}")
  public List<TeamDto> getManagedTeamList(@PathVariable int idfAccount) {
    return podService.getManagedTeamList(idfAccount);
  }

  @GetMapping("/getAvailablePodList/{idfTeam}")
  public List<AvailablePodDto> getAvailablePodList(@PathVariable int idfTeam) {
    return podService.getAvailablePodList(idfTeam);
  }

  @DeleteMapping("/terminatePod/{idfAccount}")
  public EnumResponse terminatePod(@PathVariable int idfAccount) {
    return podService.terminatePod(idfAccount);
  }

  @PostMapping("/uploadUbs")
  public EnumResponse uploadUbs(MultipartFile mFile) {
    return podService.uploadUbs(mFile);
  }

  @GetMapping("/getUnanalyzedMatchList/{idfAccount}")
  public List<UnanalyzedMatchDto> getUnanalyzedMatchList(@PathVariable int idfAccount) {
    return podService.getUnanalyzedMatchList(idfAccount);
  }
}

