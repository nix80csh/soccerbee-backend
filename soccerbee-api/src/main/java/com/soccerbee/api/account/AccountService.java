package com.soccerbee.api.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.soccerbee.api.account.AccountDto.DeleteAccountDto;
import com.soccerbee.api.account.AccountDto.ModifyPasswordDto;
import com.soccerbee.api.account.AccountDto.PodDto;
import com.soccerbee.api.account.AccountDto.ProfileImageDto;
import com.soccerbee.api.account.AccountDto.TeamOwnerDto;
import com.soccerbee.common.EnumResponse;
import com.soccerbee.entity.Account;
import com.soccerbee.entity.AccountPlayer;
import com.soccerbee.entity.TeamAccount;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.AccountPlayerRepo;
import com.soccerbee.repo.AccountRepo;
import com.soccerbee.util.CloudinaryUtil;
import com.soccerbee.util.MailSenderUtil;
import com.soccerbee.util.RandomCharUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AccountService {
  @Value("${service-domain}") private String serviceDomain;
  @Value("${cloudinary.root-path}") private String rootPath;
  @Autowired AccountRepo accountRepo;
  @Autowired AccountPlayerRepo accountPlayerRepo;
  @Autowired PasswordEncoder passwordEncoder;

  public AccountDto getPlayer(Integer idfAccount) {
    AccountPlayer accountPlayer = accountPlayerRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_AccountPlayer));

    AccountDto accountDto = new AccountDto();
    BeanUtils.copyProperties(accountPlayer, accountDto);
    return accountDto;
  }

  public PodDto getPodInfo(Integer idfAccount) {
    Account account = accountRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));
    if (account.getPod() == null)
      throw new LogicException(LogicErrorList.DoesNotExist_Pod);

    PodDto podDto = new PodDto();
    BeanUtils.copyProperties(account.getPod(), podDto);
    return podDto;
  }

  public EnumResponse modify(AccountDto accountDto) {
    AccountPlayer accountPlayer = accountPlayerRepo.findById(accountDto.getIdfAccount())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_AccountPlayer));
    accountDto.setImage(accountPlayer.getImage());
    BeanUtils.copyProperties(accountDto, accountPlayer);
    BeanUtils.copyProperties(accountDto, accountPlayer.getAccount());
    accountRepo.save(accountPlayer.getAccount());
    accountPlayerRepo.save(accountPlayer);
    return EnumResponse.Modified;
  }


  public ProfileImageDto registProfileImage(ProfileImageDto profileImageDto) {
    AccountPlayer accountPlayer = accountPlayerRepo.findById(profileImageDto.getIdfAccount())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_AccountPlayer));

    if (accountPlayer.getImage() != null)
      CloudinaryUtil.delete(accountPlayer.getImage());

    String imageUrl = CloudinaryUtil.upload(rootPath + "/account", profileImageDto.getFileImage());
    accountPlayer.setImage(imageUrl);
    accountPlayerRepo.save(accountPlayer);

    BeanUtils.copyProperties(accountPlayer, profileImageDto);
    profileImageDto.setFileImage(null);
    return profileImageDto;
  }

  public EnumResponse deleteProfileImage(int idfAccount) {
    AccountPlayer accountPlayer = accountPlayerRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_AccountPlayer));

    if (accountPlayer.getImage() != null)
      CloudinaryUtil.delete(accountPlayer.getImage());

    accountPlayer.setImage(null);
    accountPlayerRepo.save(accountPlayer);
    return EnumResponse.Deleted;
  }

  public EnumResponse sendAuthEmail(String email, String locale) {
    Account account = accountRepo.findByEmail(email)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    String authCode = RandomCharUtil.getRandomString(13);
    String confirmedUrl = serviceDomain + "/confirmMail/" + authCode + "/" + email + "/" + locale;
    String title = "";
    String content = "";
    if (locale.equals("KO")) {
      title = "SoccerBee 메일 인증";
      content = confirmedUrl;
    } else {
      title = "SoccerBee Mail Authentication";
      content = confirmedUrl;
    }

    boolean isSend = MailSenderUtil.send(email, title, content);

    if (!isSend)
      throw new LogicException(LogicErrorList.MailModuleException);

    account.setAuthCode(authCode);
    accountRepo.save(account);
    return EnumResponse.Sent;
  }

  public EnumResponse modifyPassword(ModifyPasswordDto modifyPasswordDto) {
    Account account = accountRepo.findById(modifyPasswordDto.getIdfAccount())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    if (!passwordEncoder.matches(modifyPasswordDto.getOldPassword(), account.getPassword()))
      throw new LogicException(LogicErrorList.NotMatched);
    account.setPassword(passwordEncoder.encode(modifyPasswordDto.getNewPassword()));
    accountRepo.save(account);
    return EnumResponse.Modified;
  }

  public EnumResponse deleteAccount(DeleteAccountDto deleteAccountDto) {
    Account account = accountRepo.findById(deleteAccountDto.getIdfAccount())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    List<TeamAccount> teamAccountList = account.getTeamAccounts().stream().filter(Objects::nonNull)
        .filter(at -> at.getGrade().equals("O")).collect(Collectors.toList());

    if (teamAccountList.size() > 0)
      throw new LogicException(LogicErrorList.Exist_TeamOwner);

    if (!account.getEmail().equals(deleteAccountDto.getEmail())
        || !passwordEncoder.matches(deleteAccountDto.getPassword(), account.getPassword()))
      throw new LogicException(LogicErrorList.NotMatched);

    accountRepo.delete(account);
    return EnumResponse.Deleted;
  }

  public List<TeamOwnerDto> teamOwnerList(Integer idfAccount) {
    Account account = accountRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    List<TeamAccount> teamAccountList = account.getTeamAccounts().stream().filter(Objects::nonNull)
        .filter(at -> at.getGrade().equals("O")).collect(Collectors.toList());

    List<TeamOwnerDto> teamOwnerDtoList = new ArrayList<TeamOwnerDto>();
    for (TeamAccount teamAccount : teamAccountList) {
      TeamOwnerDto teamOwnerDto = new TeamOwnerDto();
      teamOwnerDto.setIdfTeam(teamAccount.getId().getIdfTeam());
      teamOwnerDto.setName(teamAccount.getTeam().getName());
      teamOwnerDtoList.add(teamOwnerDto);
    }
    return teamOwnerDtoList;
  }
}
