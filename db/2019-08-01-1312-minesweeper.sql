/*
Navicat MySQL Data Transfer

Source Server         : Minesweeper-Amazon
Source Server Version : 50722
Source Host           : minesweeperdb.cdrlmrmeqih0.us-east-2.rds.amazonaws.com:3306
Source Database       : minesweeper

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-08-01 13:12:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for Game
-- ----------------------------
DROP TABLE IF EXISTS `Game`;
CREATE TABLE `Game` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `boardState` longtext,
  `difficulty` enum('EASY','MEDIUM','HARD') NOT NULL,
  `maxPlayers` int(11) NOT NULL,
  `height` int(11) NOT NULL,
  `width` int(11) NOT NULL,
  `gameState` enum('NOT_STARTED','STARTED','ENDED_WON','ENDED_LOST') NOT NULL,
  `token` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `token` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for Session
-- ----------------------------
DROP TABLE IF EXISTS `Session`;
CREATE TABLE `Session` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `gameID` int(10) unsigned NOT NULL,
  `gameToken` varchar(255) NOT NULL,
  `partialStateWidth` int(10) unsigned NOT NULL,
  `partialStateHeight` int(10) unsigned NOT NULL,
  `playerName` varchar(255) NOT NULL,
  `points` int(11) NOT NULL DEFAULT '0',
  `positionCol` int(11) NOT NULL,
  `positionRow` int(10) unsigned NOT NULL DEFAULT '0',
  `sessionID` varchar(255) NOT NULL,
  `spectator` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `gameID` (`gameID`),
  KEY `gameToken` (`gameToken`),
  CONSTRAINT `gameID` FOREIGN KEY (`gameID`) REFERENCES `Game` (`id`),
  CONSTRAINT `gameToken` FOREIGN KEY (`gameToken`) REFERENCES `Game` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
