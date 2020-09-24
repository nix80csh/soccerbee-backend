package com.soccerbee.api.pod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.soccerbee.api.pod.PodDto.AvailablePodDto;
import com.soccerbee.api.pod.PodDto.PodOwnerDto;
import com.soccerbee.api.pod.PodDto.TeamDto;
import com.soccerbee.api.pod.PodDto.UnanalyzedMatchDto;
import com.soccerbee.common.EnumResponse;
import com.soccerbee.entity.Account;
import com.soccerbee.entity.Match;
import com.soccerbee.entity.Pod;
import com.soccerbee.entity.Team;
import com.soccerbee.entity.TeamAccount;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.AccountRepo;
import com.soccerbee.repo.FirmwareRepo;
import com.soccerbee.repo.MatchRepo;
import com.soccerbee.repo.PodRepo;
import com.soccerbee.repo.TeamRepo;
import com.soccerbee.util.AmazonS3Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class PodService {
  @Value("${spring.profiles.active}") private String deployType;
  @Autowired AccountRepo accountRepo;
  @Autowired TeamRepo teamRepo;
  @Autowired PodRepo podRepo;
  @Autowired MatchRepo matchRepo;
  @Autowired FirmwareRepo firmwareRepo;

  public PodOwnerDto checkPodOwner(String idfPod) {
    Pod pod = podRepo.findById(idfPod)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Pod));

    PodOwnerDto podOwnerDto = new PodOwnerDto();
    podOwnerDto.setIdfPod(pod.getIdfPod());
    podOwnerDto.setType(pod.getType());
    if (pod.getAccount() != null)
      podOwnerDto.setEmail(pod.getAccount().getEmail());
    return podOwnerDto;
  }

  public PodDto getPodInfo(int idfAccount, String idfPod) {
    Account account = accountRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    // 소유하고 있는 Pod이 있는 경우
    PodDto podDto = new PodDto();
    if (account.getPod() != null) {
      podDto.setIdfPod(account.getPod().getIdfPod());

      // 소유하고 있는 Pod이 없는 경우 >> 요청한 idfPod으로 정상출고 Pod인지
      // 해당 Pod을 소유하고 계정이 있는지 있다면 >> Exception
      // 없다면 해당 유저와 요청한 idfPod을 연결등록
    } else {
      // 정상출고 체크
      Pod pod = podRepo.findById(idfPod)
          .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Pod));
      // 이미 소유하고 있는 PodOwner유무 체크
      if (pod.getAccount() != null)
        throw new LogicException(LogicErrorList.Exist_PodOwner);
      account.setPod(pod);
      accountRepo.save(account);
      podDto.setIdfPod(account.getPod().getIdfPod());
    }

    podDto.setType(account.getPod().getType());
    String maxVersion = firmwareRepo.findByIdType(podDto.getType()).stream()
        .map(fw -> fw.getId().getVersion()).map(version -> new BigDecimal(version))
        .max(Comparator.naturalOrder()).orElse(null).toString();
    podDto.setVersion(maxVersion);
    return podDto;
  }

  public List<TeamDto> getManagedTeamList(int idfAccount) {
    Account account = accountRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));
    List<TeamAccount> teamAccountList = account.getTeamAccounts().stream()
        .filter(ta -> ta.getGrade().equals("O") || ta.getGrade().equals("A"))
        .collect(Collectors.toList());

    List<TeamDto> teamList = new ArrayList<TeamDto>();
    for (TeamAccount teamAccount : teamAccountList) {
      TeamDto teamDto = new TeamDto();
      BeanUtils.copyProperties(teamAccount.getTeam(), teamDto);
      teamList.add(teamDto);
    }
    return teamList;
  }

  public List<AvailablePodDto> getAvailablePodList(int idfTeam) {
    Team team = teamRepo.findById(idfTeam)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Team));

    List<Account> accountList = new ArrayList<Account>();
    for (TeamAccount teamAccount : team.getTeamAccounts()) {
      accountList.add(teamAccount.getAccount());
    }

    List<Account> filteredAccountList =
        accountList.stream().filter(fa -> fa.getPod() != null).collect(Collectors.toList());
    List<AvailablePodDto> availablePodDtoList = new ArrayList<AvailablePodDto>();
    for (Account account : filteredAccountList) {
      AvailablePodDto availablePodDto = new AvailablePodDto();
      BeanUtils.copyProperties(account.getPod(), availablePodDto);
      availablePodDto.setIdfAccount(account.getIdfAccount());
      availablePodDto.setName(account.getName());
      availablePodDtoList.add(availablePodDto);
    }
    return availablePodDtoList;
  }

  public EnumResponse terminatePod(int idfAccount) {
    Account account = accountRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    account.setPod(null);
    accountRepo.save(account);
    return EnumResponse.Terminated;
  }

  public EnumResponse uploadUbs(MultipartFile mFile) {
    if (!mFile.getOriginalFilename().contains("zip"))
      throw new LogicException(LogicErrorList.NotMatched_Extension);

    uploadMultiZip(mFile);
    return EnumResponse.Uploaded;
  }

  private void uploadSingleUbs(MultipartFile ubs) {
    Pod pod = podRepo.findById(ubs.getOriginalFilename().split("\\_")[1])
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Pod));
    if (pod.getAccount().getIdfAccount() != null) {
      String ubsPath = "gpsdata/" + ubs.getOriginalFilename();
      ObjectMetadata metadata = new ObjectMetadata();
      AmazonS3Util.uploadFile("soccerbee-" + deployType, ubsPath, ubs, "application/json");
    }
  }

  private void uploadMultiZip(MultipartFile zipFile) {
    try {
      ZipInputStream zis = new ZipInputStream(zipFile.getInputStream());
      ZipEntry ze = zis.getNextEntry();
      while (ze != null) {
        String entryName = ze.getName();
        File f = new File(entryName);
        FileOutputStream fos = new FileOutputStream(f);
        int len;
        byte buffer[] = new byte[1024];
        while ((len = zis.read(buffer)) > 0) {
          fos.write(buffer, 0, len);
        }
        fos.close();
        String ext = f.getName().split("\\.")[1];
        Integer idfAccount = Integer.parseInt(f.getName().split("\\_")[0]);
        String idfPod = f.getName().split("\\_")[1];
        if (ext.equals("ubst")) {
          idfPod = f.getName().split("\\_")[2];
          idfAccount = Integer.parseInt(f.getName().split("\\_")[1]);
        }

        // 정상출고 체크
        podRepo.findById(idfPod)
            .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Pod));

        // 정상소유자 체크
        Account account = accountRepo.findById(idfAccount)
            .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));
        if (account.getPod() == null)
          throw new LogicException(LogicErrorList.HaveNotGotPod);

        if (f.getName().split("\\.")[1].contains("ubs")) {
          String ubsPath = "gpsdata/" + f.getName();
          AmazonS3Util.uploadFile("soccerbee-" + deployType, ubsPath, f, "application/json");
        }
        f.delete();
        ze = zis.getNextEntry();
      }
      zis.closeEntry();
      zis.close();
    } catch (IOException e) {
      e.getStackTrace();
    }
  }

  public List<UnanalyzedMatchDto> getUnanalyzedMatchList(int idfAccount) {
    Account account = accountRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    List<Integer> idfTeamList = account.getTeamAccounts().stream()
        .filter(ta -> ta.getGrade().equals("O") || ta.getGrade().equals("A"))
        .map(ta -> ta.getTeam().getIdfTeam()).collect(Collectors.toList());

    List<UnanalyzedMatchDto> unanalyzedMatchDtoList = new ArrayList<UnanalyzedMatchDto>();
    for (Integer idfTeam : idfTeamList) {
      List<Match> matchList = matchRepo.findByTeamOpponentIdIdfTeam(idfTeam);
      for (Match match : matchList) {
        if (match.getMatchAnalysis() == null) {
          UnanalyzedMatchDto unanalyzedMatchDto = new UnanalyzedMatchDto();
          BeanUtils.copyProperties(match, unanalyzedMatchDto);
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(match.getDate());
          unanalyzedMatchDto.setDay(calendar.get(Calendar.DAY_OF_WEEK));
          unanalyzedMatchDto.setIdfTeam(match.getTeamOpponent().getTeam().getIdfTeam());
          unanalyzedMatchDto.setName(match.getTeamOpponent().getTeam().getName());
          unanalyzedMatchDto.setNameAbbr(match.getTeamOpponent().getTeam().getNameAbbr());
          unanalyzedMatchDto.setNameOpponent(match.getTeamOpponent().getName());
          unanalyzedMatchDto.setEmblem(match.getTeamOpponent().getTeam().getImageEmblem());
          unanalyzedMatchDto.setEmblemOpponent(match.getTeamOpponent().getImage());
          unanalyzedMatchDtoList.add(unanalyzedMatchDto);
        }
      }
    }
    return unanalyzedMatchDtoList;
  }
}
