-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: gangnam_portal
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(150) NOT NULL,
  `birthday` date NOT NULL,
  `employee_no` bigint DEFAULT NULL,
  `gender` tinyint(1) NOT NULL,
  `join_date` date NOT NULL,
  `name_en` varchar(50) NOT NULL,
  `name_kr` varchar(20) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `profile_img` varchar(500) DEFAULT NULL,
  `state` tinyint(1) NOT NULL,
  `affiliation_id` bigint NOT NULL,
  `authority_id` bigint NOT NULL,
  `department_id` bigint NOT NULL,
  `ranks_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrspbqh6r0qdl98k8b9xg0pt68` (`affiliation_id`),
  KEY `FKj06f6n6cdj9ydjv8ygmtjt0a7` (`authority_id`),
  KEY `FKbejtwvg9bxus2mffsm3swj3u9` (`department_id`),
  KEY `FK6amc9sk2wtri7o57hhfktcrjn` (`ranks_id`),
  CONSTRAINT `FK6amc9sk2wtri7o57hhfktcrjn` FOREIGN KEY (`ranks_id`) REFERENCES `ranks` (`id`),
  CONSTRAINT `FKbejtwvg9bxus2mffsm3swj3u9` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`),
  CONSTRAINT `FKj06f6n6cdj9ydjv8ygmtjt0a7` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`id`),
  CONSTRAINT `FKrspbqh6r0qdl98k8b9xg0pt68` FOREIGN KEY (`affiliation_id`) REFERENCES `affiliation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'대전 동구 세천공원로 32-836 ㅎㅇㅎㅇ','1987-05-14',1,1,'2022-01-05','12ㅇㅇㄹㄴㄹ','일길동','010-2333-3333','https://cdn.crowdpic.net/list-thumb/thumb_l_4291713E6EC8D22461618B2107D30880.jpg',0,1,1,1,1),(2,'경기 성남시 분당구 대왕판교로 660','1987-05-15',2,2,'2022-01-06','two213','이길동','010-1233-2323','https://cdn.crowdpic.net/list-thumb/thumb_l_4291713E6EC8D22461618B2107D30880.jpg',0,4,2,6,2),(3,'충청북도 청주','1233-01-11',3,1,'2022-01-07','three','삼일길동','010-1111-1113','https://cdn.crowdpic.net/list-thumb/thumb_l_4291713E6EC8D22461618B2107D30880.jpg',0,1,1,1,3),(4,'경상북도 구미','1987-05-17',4,1,'2022-01-08','four','사길동','010-1111-1114','https://cdn.crowdpic.net/list-thumb/thumb_l_4291713E6EC8D22461618B2107D30880.jpg',0,1,1,2,4),(21,'경기도 성남시 분당구 수내동 108-13 번지','1994-02-04',148,1,'2022-06-15','KimEunYong','김은용','010-9584-9907','C:\\Users\\dusgh\\workspace\\gangnamPortal\\portal\\profileImg\\김은용-eunyong.kim@twolinecode.com.jpg',0,1,1,1,9),(22,'서울시 강동구 천호대로177길 48-11','1992-12-07',135,1,'2022-02-21','ParkJaeHyeon','박재현','010-2684-8749','C:\\Users\\dusgh\\workspace\\gangnamPortal\\portal\\profileImg\\박재현-pjh@twolinecode.com.jpg',0,1,1,1,9),(23,'경기도 시흥시 중심상가로 224','1991-12-02',138,2,'2022-03-02','SongJiHye','송지혜','010-6228-8799','C:\\Users\\dusgh\\workspace\\gangnamPortal\\portal\\profileImg\\송지혜-jihye.song@twolinecode.com.jpg',0,1,1,1,7),(24,'경기도 기흥구 신갈동 30-7번지 1동 301호','1992-05-17',101,1,'2021-01-04','SongPyeongHyeon','송평현','010-8871-7864','C:\\Users\\dusgh\\workspace\\gangnamPortal\\portal\\profileImg\\송평현-ph.song@twolinecode.com.jpg',0,1,1,1,7),(25,'서울시 강서구 까치산로 13 아델리아V 301호','1993-10-07',157,1,'2022-11-01','LeeDonghyeong','이동형','010-2728-3580','C:\\Users\\dusgh\\workspace\\gangnamPortal\\portal\\profileImg\\이동형-dh.lee@twolinecode.com.jpg',0,1,1,1,9),(26,'경기도 하남시 미사강변한강로 290','1986-01-02',142,2,'2022-03-21','LeeJinyi','이진이','010-2027-8325','C:\\Users\\dusgh\\workspace\\gangnamPortal\\portal\\profileImg\\이진이-jinyi.lee@twolinecode.com.jpg',0,1,1,1,7),(27,'sdfasdfsdfasfsadf','1997-08-17',161,1,'2023-01-02','bbbbb','정연호','010-1112-2322','C:\\Users\\dusgh\\workspace\\gangnamPortal\\portal\\profileImg\\정연호-yh.jung@twolinecode.com.jpg',0,4,2,6,9),(28,'경기도 광명시 신촌로 49 신촌휴먼시아 1단지 101동 1003호','1976-01-12',34,1,'2014-04-01','LeeJeongHun','이정훈','010-8509-1343','C:\\Users\\dusgh\\workspace\\gangnamPortal\\portal\\profileImg\\이정훈-jeonghun.lee@twolinecode.com.jpg',0,1,1,1,4),(29,'서울시 서대문구 연희로41나길 38, 나동 1층 101호(홍은동,백련빌라)','1993-11-06',158,1,'2022-11-16','ParkMinHo','박민호','010-5188-2240','C:\\Users\\dusgh\\workspace\\gangnamPortal\\portal\\profileImg\\박민호-minho.park@twolinecode.com.jpg',0,1,2,1,9),(30,'서울시 광진구 능동로23길 11-3, 402호(군자동)','1983-02-11',130,1,'2022-01-17','JeongJongHo','정종호','010-4034-8731','C:\\Users\\dusgh\\workspace\\gangnamPortal\\portal\\profileImg\\정종호-jh.jeong@twolinecode.com.jpg',0,1,1,1,7),(31,'서울시 광진구 능동로23길 11-3, 402호(군자동)','1983-02-11',0,1,'2022-01-17','YunJuHyun','윤주현','010-3685-9742','C:\\Users\\dusgh\\workspace\\gangnamPortal\\portal\\profileImg\\윤주현-juhyun.yun@twolinecode.com.jpg',0,1,1,1,2),(57,'서울 동대문구 사가정로 9 2312312','1111-11-11',236666,0,'1111-11-11','2222222222222','33333','666-6666-6666','http://image.dongascience.com/Photo/2020/03/5bddba7b6574b95d37b6079c199d7101.jpg',0,1,1,1,9),(58,'광주 광산구 2순환로 2476','1111-11-11',1231312312,2,'1111-11-11','12312312','123','213-2132-1312','http://image.dongascience.com/Photo/2020/03/5bddba7b6574b95d37b6079c199d7101.jpg',0,1,1,2,6),(59,'경북 구미시 1공단로 15-41 df','1111-11-11',1111,0,'1111-11-11','sdf','1111d','111-1111-1111','C:\\Users\\dusgh\\workspace\\gangnamPortal\\profile-image\\1111d-asdsdf123@twolinecode.com.jpg',0,1,1,1,4),(60,'부산 강서구 르노삼성대로 255 dsafsdf','1111-11-11',21312,1,'1111-11-11','sdfsdfsd','dsf','121-2123-1231',NULL,1,3,2,5,7),(61,'충남 천안시 서북구 1공단1길 49 ㅇㄹㄴㅇㄹㄴㅇㄹ','1111-11-11',213111,1,'1111-11-11','sfadsfdsdf','123123','232-3121-1123',NULL,0,1,1,1,9);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-20 16:13:25
