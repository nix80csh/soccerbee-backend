package com.soccerbee.api.auth;

import java.util.Arrays;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.soccerbee.api.auth.AuthDto.AuthCodeDto;
import com.soccerbee.api.auth.AuthDto.SendPasswordDto;
import com.soccerbee.common.EnumResponse;
import com.soccerbee.entity.Account;
import com.soccerbee.entity.AccountDevice;
import com.soccerbee.entity.AccountDevicePK;
import com.soccerbee.entity.AccountPlayer;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.AccountDeviceRepo;
import com.soccerbee.repo.AccountPlayerRepo;
import com.soccerbee.repo.AccountRepo;
import com.soccerbee.security.JWTProvider;
import com.soccerbee.util.AwsSESUtil;
import com.soccerbee.util.MailboxLayerUtil;
import com.soccerbee.util.RandomCharUtil;
import com.soccerbee.util.SlackNotificationUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AuthService {
  @Value("${service-domain}") private String serviceDomain;
  @Autowired AccountRepo accountRepo;
  @Autowired AccountDeviceRepo accountDeviceRepo;
  @Autowired AccountPlayerRepo acccountPlayerRepo;
  @Autowired PasswordEncoder passwordEncoder;
  @Value("${aws.ses.sender}") private String sesSender;
  @Autowired AwsSESUtil awsSESUtil;

  public EnumResponse signup(AuthDto authDto) {
    // 존재하는 회원체크
    if (accountRepo.existsByEmail(authDto.getEmail()))
      throw new LogicException(LogicErrorList.DuplicateEntity_Account);

    String authCode = RandomCharUtil.getRandomString(13);

    Account account = new Account();
    account.setName(authDto.getName());
    account.setEmail(authDto.getEmail());
    account.setPassword(passwordEncoder.encode(authDto.getPassword()));
    account.setAgreement(authDto.getAgreement());
    account.setAuthCode(authCode);
    accountRepo.save(account);

    AccountPlayer accountPlayer = new AccountPlayer();
    accountPlayer.setIdfAccount(account.getIdfAccount());
    accountPlayer.setBirthday(authDto.getBirthday());
    accountPlayer.setGender(authDto.getGender());
    accountPlayer.setHeight(authDto.getHeight());
    accountPlayer.setWeight(authDto.getWeight());
    accountPlayer.setPosition(authDto.getPosition());
    acccountPlayerRepo.save(accountPlayer);

    String confirmedUrl = serviceDomain + "/confirmMail/" + authCode + "/" + account.getEmail()
        + "/" + authDto.getLocale();
    awsSESUtil.sendTemplatedEmail(sesSender, Arrays.asList(account.getEmail()),
        "welcome_" + authDto.getLocale(), "{\"name\": \"" + account.getName() + "\", \"email\": \""
            + account.getEmail() + "\", \"verifyUrl\": \"" + confirmedUrl + "\"}");

    String message = serviceDomain.replace("http://", "") + "\n 총 계정 수: " + accountRepo.count()
        + "\n 가입한 이메일: " + account.getEmail();
    // Slack Notification
    if (!SlackNotificationUtil.send("notify", "SoccerBee Bot", message))
      throw new LogicException(LogicErrorList.FailedSlackNotification);

    AuthDto resAuthDto = new AuthDto();
    resAuthDto.setIdfAccount(account.getIdfAccount());
    resAuthDto.setEmail(account.getEmail());
    return EnumResponse.Registered;
  }

  public EnumResponse verifyEmail(String email) {
    // check duplicated email address
    if (accountRepo.existsByEmail(email))
      throw new LogicException(LogicErrorList.DuplicateEntity_Account);

    // verify email address
    if (!MailboxLayerUtil.checkValid(email))
      throw new LogicException(LogicErrorList.NotVerifyEmail);
    return EnumResponse.Verified;
  }

  public EnumResponse sendPassword(SendPasswordDto sendPasswordDto) {
    Account account = accountRepo.findByEmail(sendPasswordDto.getEmail())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    String password = RandomCharUtil.getRandomString(15);
    account.setPassword(passwordEncoder.encode(password));
    accountRepo.save(account);

    awsSESUtil.sendTemplatedEmail(sesSender, Arrays.asList(account.getEmail()),
        "sendTempPassword_" + sendPasswordDto.getLocale(), "{\"password\": \"" + password + "\"}");
    return EnumResponse.Sent;
  }

  public EnumResponse authCode(AuthCodeDto authCodeDto) {
    // 존재하지 않는 회원
    Account account = accountRepo.findByEmail(authCodeDto.getEmail())
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    // 이미 인증된 경우
    if (account.getAuthCode().equals("Authenticated"))
      throw new LogicException(LogicErrorList.NoLongerVaild);

    // AuthCode 불일치
    if (!authCodeDto.getAuthCode().equals(account.getAuthCode()))
      throw new LogicException(LogicErrorList.NotMatched);
    account.setAuthCode("Authenticated");
    accountRepo.saveAndFlush(account);

    Integer numberOfAuthUser = accountRepo.countByAuthCode("Authenticated");
    String message = serviceDomain.replace("http://", "") + "\n 이메일 인증 계정 수: " + numberOfAuthUser
        + "\n 인증된 이메일: " + account.getEmail();

    // Slack Notification
    if (!SlackNotificationUtil.send("notify", "SoccerBee Bot", message))
      throw new LogicException(LogicErrorList.FailedSlackNotification);

    return EnumResponse.Authenticated;
  }

  public EnumResponse deleteDeviceToken(int idfAccount, String idfDevice) {
    AccountDevicePK id = new AccountDevicePK();
    id.setIdfAccount(idfAccount);
    id.setIdfDevice(idfDevice);
    AccountDevice accountDevice = accountDeviceRepo.findById(id)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_AccountDevice));

    accountDeviceRepo.delete(accountDevice);
    return EnumResponse.Deleted;
  }

  public EnumResponse verifyJwt(String jwt) {
    JWTProvider.getEmail(jwt);
    return EnumResponse.Verified;
  }
}
