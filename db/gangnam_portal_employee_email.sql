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
-- Table structure for table `employee_email`
--

DROP TABLE IF EXISTS `employee_email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_email` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `provider` varchar(255) NOT NULL,
  `employee_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiv5cxgeknvx5jlhuoki5clq1s` (`employee_id`),
  CONSTRAINT `FKiv5cxgeknvx5jlhuoki5clq1s` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_email`
--

LOCK TABLES `employee_email` WRITE;
/*!40000 ALTER TABLE `employee_email` DISABLE KEYS */;
INSERT INTO `employee_email` VALUES (1,'yh.jun1g@twolinecode.com','google',1),(2,'dusgh123123@twolinecode.com','google',3),(4,'minho.p1ark@twolinecode.com','google',2),(5,'alsgh21240@naver.com','kakao',1),(6,'eunyong.kim@twolinecode.com','google',21),(7,'pjh@twolinecode.com','google',22),(8,'jihye.song@twolinecode.com','google',23),(12,'ph.song@twolinecode.com','google',24),(13,'dh.lee@twolinecode.com','google',25),(14,'jinyi.lee@twolinecode.com','google',26),(15,'yh.jung@twolinecode.com','google',27),(16,'jeonghun.lee@twolinecode.com','google',28),(17,'minho.park@twolinecode.com','google',29),(18,'jh.jeong@twolinecode.com','google',30),(19,'juhyun.yun@twolinecode.com','google',31),(32,'dfsadf12313@twolinecode.com','google',57),(34,'123123123123@twolinecode.com','google',58),(35,'dusgh123123@gmail.com','kakao',1),(43,'asdsdf123@twolinecode.com','google',59),(44,'fdsfb213@twolinecode.com','google',60),(45,'dsaf123@google.com','kakao',60),(47,'12313dfsa@twolinecode.com','google',61);
/*!40000 ALTER TABLE `employee_email` ENABLE KEYS */;
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
