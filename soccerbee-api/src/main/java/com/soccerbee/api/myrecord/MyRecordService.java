package com.soccerbee.api.myrecord;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soccerbee.api.myrecord.MyRecordDto.MyAbilityDto;
import com.soccerbee.api.myrecord.MyRecordDto.MyPositionDto;
import com.soccerbee.entity.Ability;
import com.soccerbee.entity.Account;
import com.soccerbee.exception.LogicErrorList;
import com.soccerbee.exception.LogicException;
import com.soccerbee.repo.AbilityRepo;
import com.soccerbee.repo.AccountRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class MyRecordService {
  @Autowired AccountRepo accountRepo;
  @Autowired AbilityRepo abilityRepo;

  public List<MyPositionDto> getMyPositionList(int idfAccount) {
    Account account = accountRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));
    List<String> positionList = account.getAbilities().stream().map(a -> a.getPosition()).distinct()
        .collect(Collectors.toList());

    List<MyPositionDto> myPositionDtoList = new ArrayList<MyPositionDto>();
    for (String position : positionList) {
      MyPositionDto myPositionDto = new MyPositionDto();

      List<Ability> abilityList = abilityRepo
          .findTop7ByAccountIdfAccountAndPositionOrderByIdfAbilityDesc(idfAccount, position);
      BigDecimal rate = BigDecimal.valueOf(abilityList.stream().map(al -> al.getRate())
          .filter(Objects::nonNull).collect(Collectors.averagingDouble(as -> as.doubleValue())));
      myPositionDto.setRate(rate);
      myPositionDto.setPosition(position);
      myPositionDtoList.add(myPositionDto);
    }
    return myPositionDtoList;
  }

  public MyAbilityDto getMyAbility(Integer idfAccount, String position) {
    Account account = accountRepo.findById(idfAccount)
        .orElseThrow(() -> new LogicException(LogicErrorList.DoesNotExist_Account));

    List<Ability> abilityList = abilityRepo
        .findTop7ByAccountIdfAccountAndPositionOrderByIdfAbilityDesc(idfAccount, position);

    MyAbilityDto myAbilityDto = new MyAbilityDto();
    myAbilityDto.setName(account.getName());
    myAbilityDto.setImage(account.getAccountPlayer().getImage());
    myAbilityDto.setPosition(account.getAccountPlayer().getPosition());
    BigDecimal acceleration =
        BigDecimal.valueOf(abilityList.stream().map(al -> al.getAcceleration())
            .filter(Objects::nonNull).collect(Collectors.averagingDouble(as -> as.doubleValue())));
    myAbilityDto.setAcceleration(acceleration);
    BigDecimal activity = BigDecimal.valueOf(abilityList.stream().map(al -> al.getActivity())
        .filter(Objects::nonNull).collect(Collectors.averagingDouble(as -> as.doubleValue())));
    myAbilityDto.setActivity(activity);
    BigDecimal agility = BigDecimal.valueOf(abilityList.stream().map(al -> al.getAgility())
        .filter(Objects::nonNull).collect(Collectors.averagingDouble(as -> as.doubleValue())));
    myAbilityDto.setAgility(agility);
    BigDecimal attack = BigDecimal.valueOf(abilityList.stream().map(al -> al.getAttack())
        .filter(Objects::nonNull).collect(Collectors.averagingDouble(as -> as.doubleValue())));
    myAbilityDto.setAttack(attack);
    BigDecimal condition = BigDecimal.valueOf(abilityList.stream().map(al -> al.getCondition())
        .filter(Objects::nonNull).collect(Collectors.averagingDouble(as -> as.doubleValue())));
    myAbilityDto.setCondition(condition);
    BigDecimal defense = BigDecimal.valueOf(abilityList.stream().map(al -> al.getDefense())
        .filter(Objects::nonNull).collect(Collectors.averagingDouble(as -> as.doubleValue())));
    myAbilityDto.setDefense(defense);
    BigDecimal speed = BigDecimal.valueOf(abilityList.stream().map(al -> al.getSpeed())
        .filter(Objects::nonNull).collect(Collectors.averagingDouble(as -> as.doubleValue())));
    myAbilityDto.setSpeed(speed);
    BigDecimal stamina = BigDecimal.valueOf(abilityList.stream().map(al -> al.getStamina())
        .filter(Objects::nonNull).collect(Collectors.averagingDouble(as -> as.doubleValue())));
    myAbilityDto.setStamina(stamina);
    BigDecimal teamwork = BigDecimal.valueOf(abilityList.stream().map(al -> al.getTeamwork())
        .filter(Objects::nonNull).collect(Collectors.averagingDouble(as -> as.doubleValue())));
    myAbilityDto.setTeamwork(teamwork);

    return myAbilityDto;
  }
}
