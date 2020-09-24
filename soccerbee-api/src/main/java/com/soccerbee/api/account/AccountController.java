package com.soccerbee.api.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soccerbee.api.account.AccountDto.DeleteAccountDto;
import com.soccerbee.api.account.AccountDto.ModifyPasswordDto;
import com.soccerbee.api.account.AccountDto.PodDto;
import com.soccerbee.api.account.AccountDto.ProfileImageDto;
import com.soccerbee.api.account.AccountDto.TeamOwnerDto;
import com.soccerbee.common.EnumResponse;

@RestController
@RequestMapping("/account")
public class AccountController {
  @Autowired AccountService accountService;

  @GetMapping("/getPlayer/{idfAccount}")
  public AccountDto getPlayer(@PathVariable Integer idfAccount) {
    return accountService.getPlayer(idfAccount);
  }

  @GetMapping("/getPodInfo/{idfAccount}")
  public PodDto getPodInfo(@PathVariable Integer idfAccount) {
    return accountService.getPodInfo(idfAccount);
  }

  @PostMapping("/modify")
  public EnumResponse modify(@RequestBody AccountDto accountDto) {
    return accountService.modify(accountDto);
  }

  @PostMapping("/registProfileImage")
  public ProfileImageDto registProfileImage(ProfileImageDto profileImageDto) {
    return accountService.registProfileImage(profileImageDto);
  }

  @DeleteMapping("/deleteProfileImage/{idfAccount}")
  public EnumResponse deleteProfileImage(@PathVariable int idfAccount) {
    return accountService.deleteProfileImage(idfAccount);
  }

  @GetMapping("/sendAuthEmail/{email}/{locale}")
  public EnumResponse sendAuthEmail(@PathVariable String email, @PathVariable String locale) {
    return accountService.sendAuthEmail(email, locale);
  }

  @PostMapping("/modifyPassword")
  public EnumResponse modifyPassword(@RequestBody ModifyPasswordDto modifyPasswordDto) {
    return accountService.modifyPassword(modifyPasswordDto);
  }

  @DeleteMapping("/deleteAccount")
  public EnumResponse deleteAccount(@RequestBody DeleteAccountDto deleteAccountDto) {
    return accountService.deleteAccount(deleteAccountDto);
  }

  @GetMapping("/teamOwnerList/{idfAccount}")
  public List<TeamOwnerDto> teamOwnerList(@PathVariable Integer idfAccount) {
    return accountService.teamOwnerList(idfAccount);
  }
}

