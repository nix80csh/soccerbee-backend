package com.soccerbee.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogicErrorList {

  DuplicateEntity_Account(101, "DuplicateEntity_Account"),

  DuplicateEntity_Admin(102, "DuplicateEntity_Admin"),

  DuplicateEntity_TeamAccount(103, "DuplicateEntity_TeamAccount"),

  DuplicateProperty_Mobile(151, "DuplicateProperty_Mobile"),

  DuplicateProperty_IdfAccount(152, "DuplicateProperty_IdfAccount"),

  DoesNotExist_Email(201, "DoesNotExist_Email"), DoesNotExist_Admin(202, "DoesNotExist_Admin"),

  DoesNotExist_Account(203, "DoesNotExist_Account"),

  DoesNotExist_AccountPlayer(204, "DoesNotExist_AccountPlayer"),

  DoesNotExist_Team(205, "DoesNotExist_Team"),

  DoesNotExist_TeamAccount(206, "DoesNotExist_TeamAccount"),

  DoesNotExist_TeamMercenary(207, "DoesNotExist_TeamMercenary"),

  DoesNotExist_Opponent(208, "DoesNotExist_Opponent"),

  DoesNotExist_Match(209, "DoesNotExist_Match"),

  DoesNotExist_ScheduleMatch(210, "DoesNotExist_ScheduleMatch"),

  DoesNotExist_Formation(211, "DoesNotExist_Formation"),

  DoesNotExist_Timeline(212, "DoesNotExist_Timeline"),

  DoesNotExist_Pod(213, "DoesNotExist_Pod"), DoesNotExist_Ubst(214, "DoesNotExist_Ubst"),

  DoesNotExist_AnalysisSession(215, "DoesNotExist_AnalysisSession"),

  DoesNotExist_Analysis(216, "DoesNotExist_Analysis"),

  DoesNotExist_Ubs(217, "DoesNotExist_Ubs"),

  DoesNotExist_Ubsp(218, "DoesNotExist_Ubsp"),

  DoesNotExist_MatchAnalysis(219, "DoesNotExist_MatchAnalysis"),

  DoesNotExist_MatchAnalysisSessionFormation(220, "DoesNotExist_MatchAnalysisSessionFormation"),

  DoesNotExist_MatchAnalysisAccount(221, "DoesNotExist_MatchAnalysisAccount"),

  DoesNotExist_MatchAnalysisSession(222, "DoesNotExist_MatchAnalysisSession"),

  DoesNotExist_NoticeNew(223, "DoesNotExist_NoticeNew"),

  DoesNotExist_NoticeSystem(224, "DoesNotExist_NoticeSystem"),

  DoesNotExist_AccountDevice(225, "DoesNotExist_AccountDevice"),

  DoesNotExist_Forecast(226, "DoesNotExist_Forecast"),

  Exist_PodOwner(801, "Exist_PodOwner"),

  Exist_Ubsp(802, "Exist_Ubsp"), Exist_TeamOwner(803, "Exist_TeamOwner"),

  NotMatched(901, "NotMatched"), SMSModuleException(902, "SMSModuleException"),

  MailModuleException(903, "MailModuleException"), AlreadyConfirmed(904, "AlreadyConfirmed"),

  NotConfirmed(905, "NotConfirmed"), NoLongerVaild(906, "NoLongerVaild"),

  FailedSlackNotification(907, "FailedSlackNotification"),

  Unauthorized(908, "Unauthorized"),

  NotVerifyEmail(909, "NotVerifyEmail"),

  NotMatched_GradeR(910, "NotMatched_GradeR"), NotMatched_GradeO(911, "NotMatched_GradeO"),

  NotMatched_JerseyNumber(912, "NotMatched_JerseyNumber"),

  NotMatched_Extension(913, "NotMatched_Extension"),

  HaveNotGotPod(914, "HaveNotGotPod"), Previous_Match(915, "Previous_Match");

  private final int errorCode;
  private final String errorMsg;
}
