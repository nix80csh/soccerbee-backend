/*
 Navicat MySQL Data Transfer

 Source Server         : soccerbee-dev-v2
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : soccerbee-dev-v2.cidy7xazjee0.ap-northeast-2.rds.amazonaws.com:3306
 Source Schema         : soccerbee

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 21/09/2020 23:44:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for @account
-- ----------------------------
DROP TABLE IF EXISTS `@account`;
CREATE TABLE `@account`  (
  `idf_account` int(11) NOT NULL,
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for _emblem
-- ----------------------------
DROP TABLE IF EXISTS `_emblem`;
CREATE TABLE `_emblem`  (
  `image` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`image`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for _formation
-- ----------------------------
DROP TABLE IF EXISTS `_formation`;
CREATE TABLE `_formation`  (
  `code` char(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for _position
-- ----------------------------
DROP TABLE IF EXISTS `_position`;
CREATE TABLE `_position`  (
  `code` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for _uniform
-- ----------------------------
DROP TABLE IF EXISTS `_uniform`;
CREATE TABLE `_uniform`  (
  `image` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`image`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ability
-- ----------------------------
DROP TABLE IF EXISTS `ability`;
CREATE TABLE `ability`  (
  `idf_ability` int(11) NOT NULL AUTO_INCREMENT,
  `idf_account` int(11) NULL DEFAULT NULL,
  `ubsp` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `number_p` int(3) NULL DEFAULT NULL,
  `idf_match` int(11) NULL DEFAULT NULL,
  `number_t` int(3) NULL DEFAULT NULL,
  `position` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `attack` decimal(11, 9) NULL DEFAULT NULL,
  `defense` decimal(11, 9) NULL DEFAULT NULL,
  `speed` decimal(11, 9) NOT NULL,
  `agility` decimal(11, 9) NOT NULL,
  `acceleration` decimal(11, 9) NOT NULL,
  `activity` decimal(11, 9) NOT NULL,
  `stamina` decimal(11, 9) NOT NULL,
  `condition_` decimal(11, 9) NOT NULL,
  `teamwork` decimal(11, 9) NULL DEFAULT NULL,
  `rate` decimal(11, 9) NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_ability`) USING BTREE,
  INDEX `fk_ability_account_1`(`idf_account`) USING BTREE,
  INDEX `fk_ability_analysis_session_1`(`ubsp`, `number_p`) USING BTREE,
  INDEX `fk_ability_match_analysis_session_1`(`idf_match`, `number_t`) USING BTREE,
  CONSTRAINT `fk_ability_account_1` FOREIGN KEY (`idf_account`) REFERENCES `account` (`idf_account`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_ability_analysis_session_1` FOREIGN KEY (`ubsp`, `number_p`) REFERENCES `analysis_session` (`ubsp`, `number`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_ability_match_analysis_session_1` FOREIGN KEY (`idf_match`, `number_t`) REFERENCES `match_analysis_session` (`idf_match`, `number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 9356 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `idf_account` int(11) NOT NULL AUTO_INCREMENT,
  `idf_pod` char(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `agreement` tinyint(1) NOT NULL,
  `auth_code` varchar(13) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_account`) USING BTREE,
  UNIQUE INDEX `unique_email`(`email`) USING BTREE,
  UNIQUE INDEX `unique_idf_pod`(`idf_pod`) USING BTREE,
  INDEX `idf_account`(`idf_account`) USING BTREE,
  CONSTRAINT `fk_account_pod_1` FOREIGN KEY (`idf_pod`) REFERENCES `pod` (`idf_pod`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 255 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for account_device
-- ----------------------------
DROP TABLE IF EXISTS `account_device`;
CREATE TABLE `account_device`  (
  `idf_account` int(11) NOT NULL,
  `idf_device` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Mac Address',
  `type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'OS type\r\nI: IOS\r\nA: Android',
  `token` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Device Token',
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_account`, `idf_device`) USING BTREE,
  INDEX `fk_account_device_account_1`(`idf_account`) USING BTREE,
  CONSTRAINT `fk_account_device_account_1` FOREIGN KEY (`idf_account`) REFERENCES `account` (`idf_account`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for account_player
-- ----------------------------
DROP TABLE IF EXISTS `account_player`;
CREATE TABLE `account_player`  (
  `idf_account` int(11) NOT NULL,
  `gender` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `birthday` date NOT NULL,
  `height` tinyint(4) UNSIGNED NOT NULL,
  `weight` tinyint(4) UNSIGNED NOT NULL,
  `position` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `foot` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `country` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_account`) USING BTREE,
  CONSTRAINT `fk_account_player_account_1` FOREIGN KEY (`idf_account`) REFERENCES `account` (`idf_account`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `idf_admin` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `authority` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_admin`) USING BTREE,
  UNIQUE INDEX `unique_email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for analysis
-- ----------------------------
DROP TABLE IF EXISTS `analysis`;
CREATE TABLE `analysis`  (
  `ubsp` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `idf_account` int(11) NOT NULL,
  `session` tinyint(3) UNSIGNED NOT NULL,
  `point` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `point1` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `point2` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `point3` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `point4` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`ubsp`) USING BTREE,
  INDEX `fk_analysis_account_1`(`idf_account`) USING BTREE,
  CONSTRAINT `fk_analysis_account_1` FOREIGN KEY (`idf_account`) REFERENCES `account` (`idf_account`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for analysis_session
-- ----------------------------
DROP TABLE IF EXISTS `analysis_session`;
CREATE TABLE `analysis_session`  (
  `ubsp` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `number` int(3) NOT NULL,
  `position` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `time_start` datetime(0) NOT NULL,
  `time_finish` datetime(0) NOT NULL,
  `duration` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '플레이시간',
  `rate` decimal(4, 2) UNSIGNED NULL DEFAULT NULL COMMENT '평점',
  `distance` decimal(4, 2) UNSIGNED NULL DEFAULT NULL COMMENT '이동거리',
  `speed_max` decimal(4, 2) UNSIGNED NULL DEFAULT NULL COMMENT '최고속도',
  `speed_avg` decimal(4, 2) UNSIGNED NULL DEFAULT NULL COMMENT '평균속도',
  `sprint` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '스프린트횟수',
  `coverage` decimal(4, 2) UNSIGNED NULL DEFAULT NULL COMMENT '커버리지',
  `pitch_x` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `pitch_y` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`ubsp`, `number`) USING BTREE,
  CONSTRAINT `fk_analysis_session_analysis_1` FOREIGN KEY (`ubsp`) REFERENCES `analysis` (`ubsp`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for firmware
-- ----------------------------
DROP TABLE IF EXISTS `firmware`;
CREATE TABLE `firmware`  (
  `type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `version` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`type`, `version`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for formation
-- ----------------------------
DROP TABLE IF EXISTS `formation`;
CREATE TABLE `formation`  (
  `idf_match` int(11) NOT NULL,
  `idf_team` int(11) NOT NULL,
  `idf_account` int(11) NOT NULL,
  `session_number` int(3) NOT NULL,
  `idf_account_shift` int(11) NULL DEFAULT NULL,
  `replacement` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `position` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `location` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `captain` tinyint(1) NOT NULL DEFAULT 0,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_match`, `idf_team`, `idf_account`, `session_number`) USING BTREE,
  INDEX `idf_match`(`idf_match`, `idf_team`, `session_number`) USING BTREE,
  INDEX `idf_account`(`idf_account`) USING BTREE,
  CONSTRAINT `fk_formation_schedule_match_1` FOREIGN KEY (`idf_match`, `idf_team`, `idf_account`) REFERENCES `schedule_match` (`idf_match`, `idf_team`, `idf_account`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hitmap
-- ----------------------------
DROP TABLE IF EXISTS `hitmap`;
CREATE TABLE `hitmap`  (
  `idf_hitmap` int(11) NOT NULL AUTO_INCREMENT,
  `ubsp` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `number` int(3) NOT NULL,
  `zone` tinyint(3) UNSIGNED NOT NULL,
  `frequency` tinyint(3) UNSIGNED NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_hitmap`) USING BTREE,
  INDEX `fk_hitmap_analysis_session_1`(`ubsp`, `number`) USING BTREE,
  CONSTRAINT `fk_hitmap_analysis_session_1` FOREIGN KEY (`ubsp`, `number`) REFERENCES `analysis_session` (`ubsp`, `number`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 24451 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for match_
-- ----------------------------
DROP TABLE IF EXISTS `match_`;
CREATE TABLE `match_`  (
  `idf_match` int(11) NOT NULL AUTO_INCREMENT,
  `idf_account` int(11) NULL DEFAULT NULL,
  `idf_team` int(11) NULL DEFAULT NULL,
  `idf_team_opponent` int(11) NULL DEFAULT NULL,
  `date_` date NULL DEFAULT NULL,
  `time_start` time(0) NOT NULL,
  `time_finish` time(0) NOT NULL,
  `lat_long` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `location` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `session` tinyint(4) UNSIGNED NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_match`) USING BTREE,
  INDEX `fk_match_account_1`(`idf_account`) USING BTREE,
  INDEX `fk_match_team_opponent_1`(`idf_team`, `idf_team_opponent`) USING BTREE,
  CONSTRAINT `fk_match_account_1` FOREIGN KEY (`idf_account`) REFERENCES `account` (`idf_account`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `fk_match_team_opponent_1` FOREIGN KEY (`idf_team`, `idf_team_opponent`) REFERENCES `team_opponent` (`idf_team`, `idf_team_opponent`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE = InnoDB AUTO_INCREMENT = 414 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for match_analysis
-- ----------------------------
DROP TABLE IF EXISTS `match_analysis`;
CREATE TABLE `match_analysis`  (
  `idf_match` int(11) NOT NULL,
  `session` tinyint(3) UNSIGNED NOT NULL,
  `time_start` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `time_finish` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `point` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `point1` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `point2` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `point3` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `point4` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_match`) USING BTREE,
  CONSTRAINT `fk_match_analysis_match__1` FOREIGN KEY (`idf_match`) REFERENCES `match_` (`idf_match`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for match_analysis_account
-- ----------------------------
DROP TABLE IF EXISTS `match_analysis_account`;
CREATE TABLE `match_analysis_account`  (
  `idf_match` int(11) NOT NULL,
  `idf_account` int(11) NOT NULL,
  `analyzed` tinyint(1) NOT NULL DEFAULT 0,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_match`, `idf_account`) USING BTREE,
  INDEX `fk_match_analysis_account_account_1`(`idf_account`) USING BTREE,
  CONSTRAINT `fk_match_analysis_account_account_1` FOREIGN KEY (`idf_account`) REFERENCES `account` (`idf_account`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_match_analysis_ubst_match_analysis_1` FOREIGN KEY (`idf_match`) REFERENCES `match_analysis` (`idf_match`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for match_analysis_session
-- ----------------------------
DROP TABLE IF EXISTS `match_analysis_session`;
CREATE TABLE `match_analysis_session`  (
  `idf_match` int(11) NOT NULL,
  `number` int(3) NOT NULL,
  `time_start` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `time_finish` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `attack_direction` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `attack_rate` decimal(6, 4) NULL DEFAULT NULL,
  `zone_attack` decimal(6, 4) NULL DEFAULT NULL,
  `zone_midfield` decimal(6, 4) NULL DEFAULT NULL,
  `zone_defense` decimal(6, 4) NULL DEFAULT NULL,
  `zone_right` decimal(6, 4) NULL DEFAULT NULL,
  `zone_center` decimal(6, 4) NULL DEFAULT NULL,
  `zone_left` decimal(6, 4) NULL DEFAULT NULL,
  `defense_line` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_match`, `number`) USING BTREE,
  CONSTRAINT `fk_match_analysis_session_match_analysis_1` FOREIGN KEY (`idf_match`) REFERENCES `match_analysis` (`idf_match`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for match_analysis_session_formation
-- ----------------------------
DROP TABLE IF EXISTS `match_analysis_session_formation`;
CREATE TABLE `match_analysis_session_formation`  (
  `idf_match` int(11) NOT NULL,
  `number` int(3) NOT NULL,
  `idf_account` int(11) NOT NULL,
  `idf_account_shift` int(11) NULL DEFAULT NULL,
  `replacement` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `position` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `location` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `captain` tinyint(1) NOT NULL DEFAULT 0,
  `duration` tinyint(3) UNSIGNED NULL DEFAULT NULL,
  `rate` decimal(4, 2) NULL DEFAULT NULL,
  `distance` decimal(4, 2) NULL DEFAULT NULL,
  `speed_max` decimal(4, 2) NULL DEFAULT NULL,
  `speed_avg` decimal(4, 2) NULL DEFAULT NULL,
  `sprint` tinyint(3) UNSIGNED NULL DEFAULT NULL,
  `coverage` decimal(4, 2) NULL DEFAULT NULL,
  `pitch_x` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `pitch_y` mediumtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_match`, `number`, `idf_account`) USING BTREE,
  INDEX `fk_match_analysis_session_formation_account_1`(`idf_account`) USING BTREE,
  CONSTRAINT `fk_match_analysis_session_formation_account_1` FOREIGN KEY (`idf_account`) REFERENCES `account` (`idf_account`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_match_analysis_session_formation_match_analysis_session_1` FOREIGN KEY (`idf_match`, `number`) REFERENCES `match_analysis_session` (`idf_match`, `number`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for match_hitmap
-- ----------------------------
DROP TABLE IF EXISTS `match_hitmap`;
CREATE TABLE `match_hitmap`  (
  `idf_match_hitmap` int(11) NOT NULL AUTO_INCREMENT,
  `idf_match` int(11) NOT NULL,
  `idf_account` int(11) NOT NULL,
  `number` int(3) NOT NULL,
  `zone` tinyint(3) UNSIGNED NOT NULL,
  `frequency` tinyint(3) UNSIGNED NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_match_hitmap`) USING BTREE,
  INDEX `fk_match_hitmap_match_analysis_session_formation_1`(`idf_match`, `number`, `idf_account`) USING BTREE,
  CONSTRAINT `fk_match_hitmap_match_analysis_session_formation_1` FOREIGN KEY (`idf_match`, `number`, `idf_account`) REFERENCES `match_analysis_session_formation` (`idf_match`, `number`, `idf_account`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 417877 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for match_sprint
-- ----------------------------
DROP TABLE IF EXISTS `match_sprint`;
CREATE TABLE `match_sprint`  (
  `idf_match_sprint` int(11) NOT NULL AUTO_INCREMENT,
  `idf_match` int(11) NOT NULL,
  `number` int(3) NOT NULL,
  `idf_account` int(11) NOT NULL,
  `point_start` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `point_finish` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `grade` tinyint(3) UNSIGNED NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_match_sprint`) USING BTREE,
  INDEX `fk_match_sprint_match_analysis_session_formation_1`(`idf_match`, `number`, `idf_account`) USING BTREE,
  CONSTRAINT `fk_match_sprint_match_analysis_session_formation_1` FOREIGN KEY (`idf_match`, `number`, `idf_account`) REFERENCES `match_analysis_session_formation` (`idf_match`, `number`, `idf_account`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 23973 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notice_new
-- ----------------------------
DROP TABLE IF EXISTS `notice_new`;
CREATE TABLE `notice_new`  (
  `idf_notice_new` int(11) NOT NULL AUTO_INCREMENT,
  `idf_admin` int(11) NOT NULL,
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `image` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `due_date` date NULL DEFAULT NULL,
  `upd_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_notice_new`) USING BTREE,
  INDEX `fk_notice_new_admin_1`(`idf_admin`) USING BTREE,
  CONSTRAINT `fk_notice_new_admin_1` FOREIGN KEY (`idf_admin`) REFERENCES `admin` (`idf_admin`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notice_system
-- ----------------------------
DROP TABLE IF EXISTS `notice_system`;
CREATE TABLE `notice_system`  (
  `idf_notice_system` int(11) NOT NULL AUTO_INCREMENT,
  `idf_admin` int(11) NOT NULL,
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_notice_system`) USING BTREE,
  INDEX `fk_notice_system_admin_1`(`idf_admin`) USING BTREE,
  CONSTRAINT `fk_notice_system_admin_1` FOREIGN KEY (`idf_admin`) REFERENCES `admin` (`idf_admin`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pod
-- ----------------------------
DROP TABLE IF EXISTS `pod`;
CREATE TABLE `pod`  (
  `idf_pod` char(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `upd_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_pod`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for schedule_match
-- ----------------------------
DROP TABLE IF EXISTS `schedule_match`;
CREATE TABLE `schedule_match`  (
  `idf_match` int(11) NOT NULL,
  `idf_team` int(11) NOT NULL,
  `idf_account` int(11) NOT NULL,
  `vote` tinyint(1) NULL DEFAULT NULL,
  `attendance` tinyint(1) NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_match`, `idf_team`, `idf_account`) USING BTREE,
  INDEX `fk_schedule_match_team_account_1`(`idf_team`, `idf_account`) USING BTREE,
  CONSTRAINT `fk_schedule_match_match__1` FOREIGN KEY (`idf_match`) REFERENCES `match_` (`idf_match`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_schedule_match_team_account_1` FOREIGN KEY (`idf_team`, `idf_account`) REFERENCES `team_account` (`idf_team`, `idf_account`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sprint
-- ----------------------------
DROP TABLE IF EXISTS `sprint`;
CREATE TABLE `sprint`  (
  `idf_sprint` int(11) NOT NULL AUTO_INCREMENT,
  `ubsp` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `number` int(3) NOT NULL,
  `point_start` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `point_finish` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `grade` tinyint(3) UNSIGNED NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_sprint`) USING BTREE,
  INDEX `fk_sprint_analysis_session_1`(`ubsp`, `number`) USING BTREE,
  CONSTRAINT `fk_sprint_analysis_session_1` FOREIGN KEY (`ubsp`, `number`) REFERENCES `analysis_session` (`ubsp`, `number`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 957 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for team
-- ----------------------------
DROP TABLE IF EXISTS `team`;
CREATE TABLE `team`  (
  `idf_team` int(11) NOT NULL AUTO_INCREMENT,
  `idf_account` int(11) NULL DEFAULT NULL,
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name_abbr` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `image_emblem` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `image_uniform_home` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `image_uniform_away` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `image_uniform_3rd` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_team`) USING BTREE,
  INDEX `fk_team_account_1`(`idf_account`) USING BTREE,
  CONSTRAINT `fk_team_account_1` FOREIGN KEY (`idf_account`) REFERENCES `account` (`idf_account`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE = InnoDB AUTO_INCREMENT = 97 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for team_account
-- ----------------------------
DROP TABLE IF EXISTS `team_account`;
CREATE TABLE `team_account`  (
  `idf_team` int(11) NOT NULL,
  `idf_account` int(11) NOT NULL,
  `number` tinyint(4) UNSIGNED NOT NULL,
  `grade` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'R: requester\r\nM: member\r\nO: owner\r\nA: admin\r\nL: left',
  `position` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_team`, `idf_account`) USING BTREE,
  INDEX `fk_team_account_account_1`(`idf_account`) USING BTREE,
  CONSTRAINT `fk_team_account_account_1` FOREIGN KEY (`idf_account`) REFERENCES `account` (`idf_account`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_team_account_team_1` FOREIGN KEY (`idf_team`) REFERENCES `team` (`idf_team`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for team_opponent
-- ----------------------------
DROP TABLE IF EXISTS `team_opponent`;
CREATE TABLE `team_opponent`  (
  `idf_team` int(11) NOT NULL,
  `idf_team_opponent` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `image` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `visible` tinyint(1) NOT NULL DEFAULT 1,
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_team`, `idf_team_opponent`) USING BTREE,
  UNIQUE INDEX `unique_idf_team_opponent`(`idf_team_opponent`) USING BTREE,
  CONSTRAINT `fk_team_opponent_team_1` FOREIGN KEY (`idf_team`) REFERENCES `team` (`idf_team`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for timeline
-- ----------------------------
DROP TABLE IF EXISTS `timeline`;
CREATE TABLE `timeline`  (
  `idf_match` int(11) NOT NULL,
  `idf_team` int(11) NOT NULL,
  `idf_account` int(11) NOT NULL,
  `session_number` int(3) NOT NULL,
  `timeline` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `type` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'GF: goals for 득점 우리팀\r\nGA: goals against 득점 상대팀\r\nAS: assist 어시스트\r\nOG: own goal 자책골',
  `upd_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `reg_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`idf_match`, `idf_team`, `idf_account`, `session_number`, `timeline`) USING BTREE,
  CONSTRAINT `fk_timeline_formation_1` FOREIGN KEY (`idf_match`, `idf_team`, `idf_account`, `session_number`) REFERENCES `formation` (`idf_match`, `idf_team`, `idf_account`, `session_number`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Triggers structure for table account
-- ----------------------------
DROP TRIGGER IF EXISTS `insert_account`;
delimiter ;;
CREATE TRIGGER `insert_account` AFTER DELETE ON `account` FOR EACH ROW INSERT INTO `@account` (idf_account, email, `name`)
VALUES (old.idf_account, old.email, old.`name`)
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
