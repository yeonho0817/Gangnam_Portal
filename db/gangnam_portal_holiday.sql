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
-- Table structure for table `holiday`
--

DROP TABLE IF EXISTS `holiday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `holiday` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_name` varchar(255) DEFAULT NULL,
  `holiday_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `holiday`
--

LOCK TABLES `holiday` WRITE;
/*!40000 ALTER TABLE `holiday` DISABLE KEYS */;
INSERT INTO `holiday` VALUES (1,'1월1일','2023-01-01'),(2,'설날','2023-01-21'),(3,'설날','2023-01-22'),(4,'설날','2023-01-23'),(5,'대체공휴일','2023-01-24'),(6,'삼일절','2023-03-01'),(7,'어린이날','2023-05-05'),(8,'부처님오신날','2023-05-27'),(9,'현충일','2023-06-06'),(10,'광복절','2023-08-15'),(11,'추석','2023-09-28'),(12,'추석','2023-09-29'),(13,'추석','2023-09-30'),(14,'개천절','2023-10-03'),(15,'한글날','2023-10-09'),(16,'기독탄신일','2023-12-25'),(17,'1월1일','2022-01-01'),(18,'설날','2022-01-31'),(19,'설날','2022-02-01'),(20,'설날','2022-02-02'),(21,'삼일절','2022-03-01'),(22,'대통령선거일','2022-03-09'),(23,'어린이날','2022-05-05'),(24,'부처님오신날','2022-05-08'),(25,'전국동시지방선거','2022-06-01'),(26,'현충일','2022-06-06'),(27,'광복절','2022-08-15'),(28,'추석','2022-09-09'),(29,'추석','2022-09-10'),(30,'추석','2022-09-11'),(31,'대체공휴일','2022-09-12'),(32,'개천절','2022-10-03'),(33,'한글날','2022-10-09'),(34,'대체공휴일','2022-10-10'),(35,'기독탄신일','2022-12-25');
/*!40000 ALTER TABLE `holiday` ENABLE KEYS */;
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
