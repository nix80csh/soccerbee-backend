package com.soccerbee.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogicErrorList {

  DuplicateEntity_Account(101, "DuplicateEntity_Account"),

  DuplicateEntity_Admin(102, "DuplicateEntity_Admin"),

  DuplicateEntity_Pod(103, "DuplicateEntity_Pod"),

  DuplicateEntity_Firmware(104, "DuplicateEntity_Firmware"),

  DuplicateProperty_Mobile(151, "DuplicateProperty_Mobile"),

  DoesNotExist_Email(201, "DoesNotExist_Email"), DoesNotExist_Admin(202, "DoesNotExist_Admin"),

  DoesNotExist_Firmware(203, "DoesNotExist_Firmware"), DoesNotExist_Pod(204, "DoesNotExist_Pod"),

  DoesNotExist_NoticeNew(205, "DoesNotExist_NoticeNew"), DoesNotExist_NoticeSystem(206,
      "DoesNotExist_NoticeSystem"),

  NotMatched(901, "NotMatched"), SMSModuleException(902, "SMSModuleException"),

  MailModuleException(903, "MailModuleException"), AlreadyConfirmed(904, "AlreadyConfirmed"),

  NotConfirmed(905, "NotConfirmed"), NotVerifyEmail(909, "NotVerifyEmail");

  private final int errorCode;
  private final String errorMsg;

}
