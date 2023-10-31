-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: Bank
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `AccountNumber` int NOT NULL,
  `CustomerID` int DEFAULT NULL,
  `AccountType` varchar(150) DEFAULT NULL,
  `Balance` double DEFAULT NULL,
  `DateOpened` date DEFAULT NULL,
  `UserUpiId` varchar(50) DEFAULT NULL,
  `card_no` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`AccountNumber`),
  KEY `CustomerID` (`CustomerID`),
  CONSTRAINT `accounts_ibfk_1` FOREIGN KEY (`CustomerID`) REFERENCES `customers` (`CustomerID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accounts`
--

LOCK TABLES `accounts` WRITE;
/*!40000 ALTER TABLE `accounts` DISABLE KEYS */;
INSERT INTO `accounts` VALUES (120365,9,'Current',7000,'2021-10-31','Shera@upi','5646544654'),(123654,1,'Savings',5949.25,'2023-09-28','Up1@upi','8524965286'),(147852,2,'Savings,Fix-Deposit',33700.3,'2020-12-09','Up2@upi','8548585128'),(458754,4,'Savings',5492.5,'2018-10-28','upi3@upi','1234567898'),(552555,6,'Savings',14496.25,'2023-10-30','honey@upi','2454620352'),(556366,8,'Current,Fix-Deposit',55999,'2018-11-06','Krish@upi','5623652655'),(565566,3,'Fix-Deposit',2064,'2018-01-05',NULL,NULL),(656985,7,'Savings,Current',489001,'2015-05-15','Rmehara@upi','5698464448'),(852865,5,'Savings',9000,'2023-10-30','archi@upi','2578963132');
/*!40000 ALTER TABLE `accounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `CustomerID` int NOT NULL AUTO_INCREMENT,
  `Customerpass` varchar(20) DEFAULT NULL,
  `FirstName` varchar(50) DEFAULT NULL,
  `LastName` varchar(50) DEFAULT NULL,
  `DateOfBirth` date DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `ContactNumber` varchar(15) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`CustomerID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'p','Jonny','Doe','1985-10-15','123 Main St, City, USA','1234567890','john.doe@example.com'),(2,'p','Joe','biden','1992-01-26','WhiteHouse , Washington , USA','1010110101','joe@biden.gov.in'),(3,'p','Harry','makes','2000-05-21','A,70 Short Heights , USA','98426529','Haryy@smail.com'),(4,'pass','Rahul','Mehta','1980-06-02','A,50 Rajhans,Kandivali,Mumbai','125789646','rahul@gmail.com'),(5,'pass@123','Archita','Sharma','2006-01-21','Rai ja < Malad East , Mumbai','932875651','arachit@gmia.com'),(6,'dhoni07','Honey','Chauhan','2006-06-01','hospital,malad east,mumbai','256878922','honey@gmail.com'),(7,'rohan123','rohan','mehara','1999-12-08','Mansion Palace , Worli , Mumbai ','094520653','Rohanmehra@gmail.com'),(8,'krish','krish','vicky','2000-02-06','Near Bandra Band Stand , Mumbai','852365265','Krish@mail.com'),(9,'Shreya','Sherya','Dholu','2006-01-22','Borivali West','655565646','Sherya@gmail.com');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `TransactionID` int NOT NULL AUTO_INCREMENT,
  `AccountNumber` int DEFAULT NULL,
  `TransactionType` varchar(20) DEFAULT NULL,
  `Amount` decimal(15,2) DEFAULT NULL,
  `TransactionDateTime` datetime DEFAULT NULL,
  `Balancetype` tinyint(1) DEFAULT NULL,
  `Remark` varchar(255) DEFAULT NULL,
  `Bank_Type` tinyint(1) DEFAULT NULL,
  `ifsc` varchar(50) DEFAULT NULL,
  `Senders_acc_no` int DEFAULT NULL,
  `Senders_Upi_id` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`TransactionID`),
  KEY `AccountNumber` (`AccountNumber`),
  CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`AccountNumber`) REFERENCES `accounts` (`AccountNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transactions`
--

LOCK TABLES `transactions` WRITE;
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` VALUES (1,123654,'Upi',50.00,'2023-10-07 17:12:29',0,'UPI Payment',1,NULL,NULL,'Up2@upi'),(2,147852,'Upi',50.00,'2023-10-07 17:12:29',1,'UPI Payment',1,NULL,NULL,'Up1@upi'),(3,123654,'Upi',50.00,'2023-10-07 17:15:26',0,'UPI Payment',1,NULL,NULL,'Up2@upi'),(4,147852,'Upi',50.00,'2023-10-07 17:15:26',1,'UPI Payment',1,NULL,NULL,'Up1@upi'),(5,123654,'Upi',5.00,'2023-10-07 17:23:11',0,NULL,0,NULL,NULL,'hs@upi'),(6,147852,'Upi',200.00,'2023-10-07 17:27:55',0,'UPI Payment',1,NULL,NULL,'Up1@upi'),(7,123654,'Upi',200.00,'2023-10-07 17:27:55',1,'UPI Payment',1,NULL,NULL,'Up2@upi'),(8,123654,'Upi',50.00,'2023-10-07 18:50:37',0,NULL,0,NULL,NULL,'bjds@upi'),(9,123654,'Upi',95.00,'2023-10-07 18:55:05',0,'UPI Payment',1,NULL,NULL,'Up2@upi'),(10,147852,'Upi',95.00,'2023-10-07 18:55:05',1,'UPI Payment',1,NULL,NULL,'Up1@upi'),(11,123654,'Bank-Transfer',50.00,'2023-10-07 19:41:21',0,'Bank-Transfer',1,'NA0909',147852,NULL),(12,147852,'Upi',50.00,'2023-10-07 19:41:21',1,'Bank-Transfer',1,'NA0909',123654,NULL),(13,123654,'Bank-Transfer',5.30,'2023-10-07 19:46:11',0,'Bank-Transfer',1,'NA0909',147852,NULL),(14,147852,'Bank-Transfer',5.30,'2023-10-07 19:46:11',1,'BANK-TRANSFER',1,'NA09090',123654,NULL),(15,123654,'Bank-Transfer',800.00,'2023-10-07 19:47:02',0,NULL,0,'ICICI0825',928235,NULL),(16,123654,'Upi',500.00,'2023-10-07 23:34:05',0,NULL,0,NULL,NULL,'km@upi'),(17,147852,'Bank-Transfer',500.00,'2023-10-07 23:38:34',0,'Bank-Transfer',1,'NA0909',147852,NULL),(18,147852,'Bank-Transfer',500.00,'2023-10-07 23:38:34',1,'BANK-TRANSFER',1,'NA09090',147852,NULL),(19,147852,'Bank-Transfer',150.00,'2023-10-07 23:42:04',0,'Bank-Transfer',1,'NA0909',123654,NULL),(20,123654,'Bank-Transfer',150.00,'2023-10-07 23:42:04',1,'BANK-TRANSFER',1,'NA09090',147852,NULL),(21,123654,'CARD',500.00,'2023-10-08 00:42:54',0,'MacD',0,NULL,NULL,NULL),(22,147852,'CARD',5000.00,'2023-10-08 00:43:10',0,'MacD',0,NULL,NULL,NULL),(23,147852,'CARD',5800.00,'2023-10-08 00:43:33',0,'gucci',0,NULL,NULL,NULL),(24,123654,'CARD',800.00,'2023-10-08 00:45:23',0,'Tshirt',0,NULL,NULL,NULL),(25,123654,'CARD',500.00,'2023-10-09 19:45:51',0,'Dominos',NULL,NULL,NULL,NULL),(26,123654,'CARD',600.00,'2023-10-09 19:46:12',0,'SIP',NULL,NULL,NULL,NULL),(27,123654,'CARD',50.00,'2023-10-09 22:00:18',0,'SIP',NULL,NULL,NULL,NULL),(28,123654,'CARD',100.00,'2023-10-09 22:00:30',0,'SIP',NULL,NULL,NULL,NULL),(29,123654,'CARD',1000.00,'2023-10-09 22:02:32',0,'SIP',NULL,NULL,NULL,NULL),(30,147852,'Upi',700.00,'2023-10-10 14:48:51',0,'UPI Payment',1,NULL,NULL,'Up1@upi'),(31,123654,'Upi',700.00,'2023-10-10 14:48:51',1,'UPI Payment',1,NULL,NULL,'Up2@upi'),(32,147852,'Bank-Transfer',3000.00,'2023-10-10 14:49:51',0,'Bank-Transfer',1,'NA0909',123654,NULL),(33,123654,'Bank-Transfer',3000.00,'2023-10-10 14:49:51',1,'BANK-TRANSFER',1,'NA09090',147852,NULL),(34,123654,'Upi',32.00,'2023-10-10 14:53:43',0,NULL,0,NULL,NULL,'jhdj@upi'),(35,123654,'Upi',6000.00,'2023-10-11 04:31:16',0,'UPI Payment',1,NULL,NULL,'Up2@upi'),(36,147852,'Upi',6000.00,'2023-10-11 04:31:16',1,'UPI Payment',1,NULL,NULL,'Up1@upi'),(37,147852,'Upi',8000.00,'2023-10-11 05:38:32',0,NULL,0,NULL,NULL,'jdaj@upi'),(38,123654,'Upi',500.00,'2023-10-11 05:42:58',0,NULL,0,NULL,NULL,'Up3@upi'),(39,147852,'Upi',5000.00,'2023-10-27 21:43:59',0,'UPI Payment',1,NULL,NULL,'Up1@upi'),(40,123654,'Upi',5000.00,'2023-10-27 21:43:59',1,'UPI Payment',1,NULL,NULL,'Up2@upi'),(41,458754,'Upi',1000.00,'2023-10-28 10:57:48',0,'UPI Payment',1,NULL,NULL,'up2@upi'),(42,147852,'Upi',1000.00,'2023-10-28 10:57:48',1,'UPI Payment',1,NULL,NULL,'upi3@upi'),(43,458754,'Bank-Transfer',1000.00,'2023-10-28 10:59:23',0,'Bank-Transfer',1,'NA0909',123654,NULL),(44,123654,'Bank-Transfer',1000.00,'2023-10-28 10:59:23',1,'BANK-TRANSFER',1,'NA09090',458754,NULL),(45,123654,'Upi',200.00,'2023-10-28 12:15:32',0,'UPI Payment',1,NULL,NULL,'up2@upi'),(46,147852,'Upi',200.00,'2023-10-28 12:15:32',1,'UPI Payment',1,NULL,NULL,'Up1@upi'),(47,458754,'BANK-Transfer',2000.00,'2023-10-30 09:53:14',0,NULL,0,'jbuksus',84512054,NULL),(48,458754,'BANK-Transfer',500.00,'2023-10-30 09:55:36',0,NULL,0,'NSDNS',75415,NULL),(49,852865,'Upi',1000.00,'2023-10-30 11:13:06',0,NULL,0,NULL,NULL,'jill@upi'),(50,552555,'BANK-Transfer',500.00,'2023-10-30 11:20:42',0,NULL,0,'pytm0123',954826,NULL),(51,656985,'Upi',5999.00,'2023-10-30 11:46:50',0,'UPI Payment',1,NULL,NULL,'krish@upi'),(52,556366,'Upi',5999.00,'2023-10-30 11:46:50',1,'UPI Payment',1,NULL,NULL,'Rmehara@upi'),(53,656985,'Bank-Transfer',5000.00,'2023-10-30 11:47:58',0,'Bank-Transfer',1,'NA09090',552555,NULL),(54,552555,'Bank-Transfer',5000.00,'2023-10-30 11:47:58',1,'Bank-Transfer',1,'NA09090',656985,NULL),(55,123654,'Upi',1.00,'2023-10-30 12:15:58',0,NULL,0,NULL,NULL,'jill@upi'),(56,120365,'Upi',1000.00,'2023-10-30 12:45:42',0,NULL,0,NULL,NULL,'daksh@upi'),(57,147852,'Upi',500.00,'2023-10-30 14:44:18',0,NULL,0,NULL,NULL,'aefhe@upi'),(58,123654,'Upi',50.00,'2023-10-30 15:52:43',0,NULL,0,NULL,NULL,'jh@upi'),(59,123654,'Upi',1000.00,'2023-10-30 15:57:24',0,NULL,0,NULL,NULL,'mahoar@upi');
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-31 22:49:44
