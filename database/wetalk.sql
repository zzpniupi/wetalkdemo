-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1
-- 生成日期： 2019-03-26 13:46:19
-- 服务器版本： 10.1.38-MariaDB
-- PHP 版本： 7.3.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `wetalk`
--

-- --------------------------------------------------------

--
-- 表的结构 `chatmsgs`
--

CREATE TABLE `chatmsgs` (
  `senderID` int(10) UNSIGNED NOT NULL,
  `receiveID` int(10) UNSIGNED NOT NULL,
  `msgs` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `chatmsgs`
--

INSERT INTO `chatmsgs` (`senderID`, `receiveID`, `msgs`) VALUES
(1009, 6666, 'hi'),
(1009, 6666, 'nice to meet you'),
(6666, 1009, 'dd'),
(6666, 1009, 'dd'),
(1010, 6666, 'hi'),
(6666, 1010, 'hi'),
(6666, 1010, 'URmsgs test'),
(6666, 1010, 'hjhjh'),
(6666, 1010, 'asdasd'),
(6666, 1010, 'fhj'),
(1008, 6666, 'hi'),
(6666, 1008, 'yeah'),
(1008, 6666, 'f***');

-- --------------------------------------------------------

--
-- 表的结构 `friendlist`
--

CREATE TABLE `friendlist` (
  `userID` int(10) UNSIGNED NOT NULL,
  `friendID` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `friendlist`
--

INSERT INTO `friendlist` (`userID`, `friendID`) VALUES
(6666, 1000),
(6666, 1001),
(6666, 1002),
(6666, 1003),
(6666, 1004),
(6666, 1005),
(6666, 1006),
(1000, 6666),
(1001, 6666),
(1002, 6666),
(1003, 6666),
(1004, 6666),
(1005, 6666),
(1006, 6666),
(1006, 1008),
(1008, 1006),
(1009, 6666),
(6666, 1009),
(6666, 1010),
(1010, 6666),
(6666, 1008),
(1008, 6666);

-- --------------------------------------------------------

--
-- 表的结构 `uschatmsgs`
--

CREATE TABLE `uschatmsgs` (
  `senderID` int(10) UNSIGNED NOT NULL,
  `receiveID` int(10) UNSIGNED NOT NULL,
  `msgs` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `uschatmsgs`
--

INSERT INTO `uschatmsgs` (`senderID`, `receiveID`, `msgs`) VALUES
(6666, 1010, 'hi'),
(6666, 1010, 'URmsgs test'),
(6666, 1010, 'hjhjh'),
(6666, 1010, 'asdasd'),
(6666, 1010, 'fhj');

-- --------------------------------------------------------

--
-- 表的结构 `userinfo`
--

CREATE TABLE `userinfo` (
  `userID` int(10) UNSIGNED NOT NULL,
  `phoneNumber` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `nickname` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `userinfo`
--

INSERT INTO `userinfo` (`userID`, `phoneNumber`, `password`, `nickname`) VALUES
(1000, '100', '1234', 'test00'),
(1001, '19923271816', 'Zzp19981206', 'Adam'),
(1002, '123', '123', 'Michael'),
(1003, '123', '123', 'Viola'),
(1004, '123', '123', 'bossXu'),
(1005, '123', '123', 'Randoll'),
(1006, '123', '123', 'Kevine'),
(1007, '123', '123', 'Lee'),
(1008, '110', '123', 'Mick'),
(1009, '110', '123', 'GEE'),
(1010, '110', '123', 'Alice'),
(6666, '12345678901', '1234567890', 'Manager');

--
-- 转储表的索引
--

--
-- 表的索引 `userinfo`
--
ALTER TABLE `userinfo`
  ADD PRIMARY KEY (`userID`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `userinfo`
--
ALTER TABLE `userinfo`
  MODIFY `userID` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6667;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
