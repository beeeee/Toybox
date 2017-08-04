-- MySQL dump 10.9
--
-- Host: localhost    Database: toybox
-- ------------------------------------------------------
-- Server version	4.1.12a-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `series`
--

DROP TABLE IF EXISTS `series`;
CREATE TABLE `series` (
  `series_id` int(10) unsigned NOT NULL auto_increment,
  `series_name` varchar(45) NOT NULL default '',
  `series_description` varchar(45) NOT NULL default '',
  PRIMARY KEY  (`series_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `series`
--


/*!40000 ALTER TABLE `series` DISABLE KEYS */;
LOCK TABLES `series` WRITE;
INSERT INTO `series` VALUES (0,'s1','Season One'),(1,'s2','Season Two'),(2,'s3','Season Three'),(3,'','Season Four');
UNLOCK TABLES;
/*!40000 ALTER TABLE `series` ENABLE KEYS */;

--
-- Table structure for table `toy`
--

DROP TABLE IF EXISTS `toy`;
CREATE TABLE `toy` (
  `toy_id` int(10) unsigned NOT NULL auto_increment,
  `name` varchar(45) NOT NULL default '',
  `series_id` int(10) unsigned NOT NULL default '0',
  `release_year` varchar(7) NOT NULL default '',
  `release_price` double NOT NULL default '0',
  `current_price` double NOT NULL default '0',
  `comments` varchar(100) NOT NULL default '',
  `type_id` varchar(45) NOT NULL default '',
  `toy_cond` varchar(15) NOT NULL default '0',
  `cond_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`toy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `toy`
--


/*!40000 ALTER TABLE `toy` DISABLE KEYS */;
LOCK TABLES `toy` WRITE;
INSERT INTO `toy` VALUES (1,'Megatron',1,'1985',5.98999977111816,19.9899997711182,'This is a test entry.','0','0',0),(2,'Galvatron',1,'1985',2.55,110.77,'blah!','0','1',1),(3,'ShockWave',2,'1986',3.99000000953674,59.9900016784668,'test test 123','0','2',2),(4,'Snake Eyes',3,'1987',6.98999977111816,27.9899997711182,'type test','1','3',3),(5,'Beast Man',1,'1985',6.99,21.99,'type test 2','2','1',2),(7,'Shredder',2,'1997',1.99,11.99,'missing right hand','3','0',0),(8,'destro',1,'1981',1.99,99.99,'nope.','1','2',0),(9,'She-ra',1,'1986',1,111.99,'still in original packaging','2','3',0),(10,'Donatello',1,'1981',5.99,21.99,'slight visible scratches','3','2',0),(11,'Michaelangelo',2,'1992',1.99,26.99,'missing accessories, cracked','3','2',0);
UNLOCK TABLES;
/*!40000 ALTER TABLE `toy` ENABLE KEYS */;

--
-- Table structure for table `toy_type`
--

DROP TABLE IF EXISTS `toy_type`;
CREATE TABLE `toy_type` (
  `type_id` int(10) unsigned NOT NULL auto_increment,
  `type_name` varchar(45) NOT NULL default '',
  PRIMARY KEY  (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `toy_type`
--


/*!40000 ALTER TABLE `toy_type` DISABLE KEYS */;
LOCK TABLES `toy_type` WRITE;
INSERT INTO `toy_type` VALUES (0,'Transformers'),(1,'G.I. Joe'),(2,'He-Man'),(3,'Teenage Mutant Ninja Turtles');
UNLOCK TABLES;
/*!40000 ALTER TABLE `toy_type` ENABLE KEYS */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

