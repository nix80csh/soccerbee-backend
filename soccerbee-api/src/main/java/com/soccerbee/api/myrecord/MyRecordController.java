package com.soccerbee.api.myrecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soccerbee.api.myrecord.MyRecordDto.MyAbilityDto;
import com.soccerbee.api.myrecord.MyRecordDto.MyPositionDto;

@RestController
@RequestMapping("/myrecord")
public class MyRecordController {
  @Autowired MyRecordService myRecordService;

  @GetMapping("/getMyPositionList/{idfAccount}")
  public List<MyPositionDto> getMyPositionList(@PathVariable int idfAccount) {
    return myRecordService.getMyPositionList(idfAccount);
  }

  @GetMapping("/getMyAbility/{idfAccount}/{position}")
  public MyAbilityDto getMyAbility(@PathVariable Integer idfAccount,
      @PathVariable String position) {
    return myRecordService.getMyAbility(idfAccount, position);
  }
}

