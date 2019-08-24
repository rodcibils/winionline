CREATE DATABASE  IF NOT EXISTS `winionline` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `winionline`;
-- MySQL dump 10.13  Distrib 8.0.16, for Linux (x86_64)
--
-- Host: localhost    Database: winionline
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `apelaciones`
--

DROP TABLE IF EXISTS `apelaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `apelaciones` (
  `id_disputa` int(11) NOT NULL,
  `fecha` datetime DEFAULT NULL,
  `cierre` datetime DEFAULT NULL,
  `estado` int(11) NOT NULL,
  PRIMARY KEY (`id_disputa`),
  KEY `fk_apelaciones_disputa_idx` (`id_disputa`),
  CONSTRAINT `fk_apelaciones_disputa` FOREIGN KEY (`id_disputa`) REFERENCES `disputas` (`id_partido`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apelaciones`
--

LOCK TABLES `apelaciones` WRITE;
/*!40000 ALTER TABLE `apelaciones` DISABLE KEYS */;
/*!40000 ALTER TABLE `apelaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disputas`
--

DROP TABLE IF EXISTS `disputas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `disputas` (
  `id_partido` int(11) NOT NULL,
  `fecha` date NOT NULL,
  `vencimiento` date NOT NULL,
  `estado` int(11) NOT NULL,
  PRIMARY KEY (`id_partido`),
  KEY `fk_disputas_estado_idx` (`estado`),
  CONSTRAINT `fk_disputas_estado` FOREIGN KEY (`estado`) REFERENCES `estados` (`id`),
  CONSTRAINT `fk_disputas_partido` FOREIGN KEY (`id_partido`) REFERENCES `partidos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disputas`
--

LOCK TABLES `disputas` WRITE;
/*!40000 ALTER TABLE `disputas` DISABLE KEYS */;
INSERT INTO `disputas` VALUES (15,'2019-08-10','2019-08-20',12),(16,'2019-08-10','2019-08-20',12),(17,'2019-08-10','2019-08-20',12),(18,'2019-08-10','2019-08-20',12),(19,'2019-08-10','2019-08-20',12),(20,'2019-08-10','2019-08-20',12),(21,'2019-08-10','2019-08-20',12),(22,'2019-08-10','2019-08-20',12),(30,'2019-08-10','2019-08-20',12),(31,'2019-08-10','2019-08-20',12),(32,'2019-08-10','2019-08-20',12),(34,'2019-08-10','2019-08-20',12);
/*!40000 ALTER TABLE `disputas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estados`
--

DROP TABLE IF EXISTS `estados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `estados` (
  `id` int(11) NOT NULL,
  `descripcion` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `descripcion_UNIQUE` (`descripcion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estados`
--

LOCK TABLES `estados` WRITE;
/*!40000 ALTER TABLE `estados` DISABLE KEYS */;
INSERT INTO `estados` VALUES (16,'Apelacion Cerrada'),(15,'Apelacion En Curso'),(14,'Disputa Apelada'),(13,'Disputa Cerrada'),(12,'Disputa En Curso'),(4,'Liga Iniciada'),(3,'Liga No Iniciada'),(5,'Liga Terminada'),(11,'Partido Disputado'),(9,'Partido Finalizado'),(8,'Partido Pendiente'),(10,'Partido Rechazado'),(7,'Solicitud Aceptada'),(6,'Solicitud Pendiente'),(1,'Usuario Activo'),(2,'Usuario Eliminado');
/*!40000 ALTER TABLE `estados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ligas`
--

DROP TABLE IF EXISTS `ligas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ligas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `temporada` int(11) NOT NULL,
  `fecha_inicio` datetime NOT NULL,
  `fecha_fin` datetime NOT NULL,
  `estado` int(11) NOT NULL,
  PRIMARY KEY (`id`,`nombre`,`temporada`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_ligas_estado_idx` (`estado`),
  CONSTRAINT `fk_ligas_estado` FOREIGN KEY (`estado`) REFERENCES `estados` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ligas`
--

LOCK TABLES `ligas` WRITE;
/*!40000 ALTER TABLE `ligas` DISABLE KEYS */;
INSERT INTO `ligas` VALUES (8,'asd',2019,'2019-08-13 00:00:00','2019-08-28 00:00:00',5),(9,'asd',2018,'2019-08-13 00:00:00','2019-08-28 00:00:00',4),(10,'asd',2015,'2019-08-13 00:00:00','2019-08-28 00:00:00',4),(11,'asd',2013,'2019-08-13 00:00:00','2019-08-28 00:00:00',4);
/*!40000 ALTER TABLE `ligas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paises`
--

DROP TABLE IF EXISTS `paises`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `paises` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paises`
--

LOCK TABLES `paises` WRITE;
/*!40000 ALTER TABLE `paises` DISABLE KEYS */;
INSERT INTO `paises` VALUES (1,'Argentina'),(2,'Brasil'),(3,'Chile'),(5,'Colombia'),(7,'Paraguay'),(4,'Peru'),(6,'Uruguay');
/*!40000 ALTER TABLE `paises` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parametros`
--

DROP TABLE IF EXISTS `parametros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `parametros` (
  `id` int(11) NOT NULL,
  `parametro` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `parametroid_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parametros`
--

LOCK TABLES `parametros` WRITE;
/*!40000 ALTER TABLE `parametros` DISABLE KEYS */;
INSERT INTO `parametros` VALUES (1,'1407661933'),(2,'/media/datos/eclipse-java/winionline/avatars'),(3,'/media/datos/eclipse-java/winionline/evidencias'),(4,'/media/datos/eclipse-java/winionline/reports'),(5,'/media/datos/eclipse-java/winionline/log');
/*!40000 ALTER TABLE `parametros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `partidos`
--

DROP TABLE IF EXISTS `partidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `partidos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` datetime DEFAULT NULL,
  `estado` int(11) NOT NULL,
  `solicitud` int(11) NOT NULL,
  `registro` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_partidos_solicitudes_idx` (`solicitud`),
  KEY `fk_partidos_registro_idx` (`registro`),
  CONSTRAINT `fk_partidos_registro` FOREIGN KEY (`registro`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_partidos_solicitudes` FOREIGN KEY (`solicitud`) REFERENCES `solicitudes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partidos`
--

LOCK TABLES `partidos` WRITE;
/*!40000 ALTER TABLE `partidos` DISABLE KEYS */;
INSERT INTO `partidos` VALUES (15,'2019-08-03 00:00:00',11,1,51),(16,'2019-08-03 00:00:00',11,2,51),(17,'2019-08-03 00:00:00',11,3,51),(18,'2019-08-03 00:00:00',11,4,51),(19,'2019-08-03 00:00:00',11,5,51),(20,'2019-08-03 00:00:00',11,6,51),(21,'2019-08-03 00:00:00',11,7,51),(22,'2019-08-03 00:00:00',11,8,51),(23,NULL,8,9,NULL),(24,NULL,8,10,NULL),(25,NULL,8,11,NULL),(26,NULL,8,12,NULL),(27,NULL,8,13,NULL),(28,NULL,8,14,NULL),(29,NULL,8,15,NULL),(30,'2019-08-03 00:00:00',11,16,51),(31,'2019-08-03 00:00:00',11,17,51),(32,'2019-08-03 00:00:00',11,18,51),(33,NULL,8,19,NULL),(34,'2019-08-03 00:00:00',11,20,51),(35,NULL,8,21,NULL),(36,NULL,8,22,NULL),(37,NULL,8,23,NULL),(38,NULL,8,24,NULL),(39,NULL,8,25,NULL),(40,NULL,8,26,NULL),(41,NULL,8,27,NULL),(42,NULL,8,28,NULL),(43,NULL,8,29,NULL),(44,NULL,8,30,NULL);
/*!40000 ALTER TABLE `partidos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resultados`
--

DROP TABLE IF EXISTS `resultados`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `resultados` (
  `id_jugador` int(11) NOT NULL,
  `id_partido` int(11) NOT NULL,
  `goles` int(11) NOT NULL,
  PRIMARY KEY (`id_jugador`,`id_partido`,`goles`),
  KEY `fk_resultados_uno_idx` (`id_jugador`),
  KEY `fk_resultados_partido_idx` (`id_partido`),
  CONSTRAINT `fk_resultados_jugador` FOREIGN KEY (`id_jugador`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_resultados_partido` FOREIGN KEY (`id_partido`) REFERENCES `partidos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resultados`
--

LOCK TABLES `resultados` WRITE;
/*!40000 ALTER TABLE `resultados` DISABLE KEYS */;
INSERT INTO `resultados` VALUES (45,15,4),(45,16,1),(45,17,3),(45,18,2),(45,19,1),(45,20,5),(45,21,3),(45,22,11),(45,30,10),(45,31,10),(45,32,3),(45,34,7),(50,15,1),(50,16,3),(50,17,3),(50,18,2),(50,19,5),(50,20,1),(50,21,2),(50,22,10),(50,30,11),(50,31,10),(50,32,3),(50,34,3);
/*!40000 ALTER TABLE `resultados` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `descripcion` varchar(126) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'Admin','Con permisos de administrador'),(2,'Jugador','Sin permisos de administrador');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `solicitudes`
--

DROP TABLE IF EXISTS `solicitudes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `solicitudes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fecha` datetime NOT NULL,
  `vencimiento` datetime NOT NULL,
  `estado` int(11) NOT NULL,
  `jugador_uno` int(11) NOT NULL,
  `jugador_dos` int(11) NOT NULL,
  `liga` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_solicitudes_estados_idx` (`estado`),
  KEY `fk_solicitudes_jugador_uno_idx` (`jugador_uno`),
  KEY `fk_solicitudes_jugador_dos_idx` (`jugador_dos`),
  KEY `fk_solicitudes_liga_idx` (`liga`),
  CONSTRAINT `fk_solicitudes_estados` FOREIGN KEY (`estado`) REFERENCES `estados` (`id`),
  CONSTRAINT `fk_solicitudes_jugador_dos` FOREIGN KEY (`jugador_dos`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_solicitudes_jugador_uno` FOREIGN KEY (`jugador_uno`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_solicitudes_liga` FOREIGN KEY (`liga`) REFERENCES `ligas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `solicitudes`
--

LOCK TABLES `solicitudes` WRITE;
/*!40000 ALTER TABLE `solicitudes` DISABLE KEYS */;
INSERT INTO `solicitudes` VALUES (1,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(2,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(3,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(4,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(5,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(6,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(7,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(8,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(9,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(10,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(11,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(12,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(13,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(14,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(15,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(16,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(17,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(18,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(19,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(20,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(21,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(22,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(23,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(24,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(25,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(26,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(27,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(28,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(29,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(30,'2019-08-03 00:00:00','2019-08-20 00:00:00',7,45,50,NULL),(31,'2019-08-03 00:00:00','2019-08-20 00:00:00',6,45,51,NULL),(32,'2019-08-03 00:00:00','2019-08-20 00:00:00',6,45,51,NULL),(33,'2019-08-03 00:00:00','2019-08-20 00:00:00',6,45,51,NULL),(34,'2019-08-03 00:00:00','2019-08-20 00:00:00',6,51,45,NULL),(35,'2019-08-03 00:00:00','2019-08-20 00:00:00',6,51,45,NULL),(36,'2019-08-03 00:00:00','2019-08-20 00:00:00',6,51,45,NULL);
/*!40000 ALTER TABLE `solicitudes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_apelacion`
--

DROP TABLE IF EXISTS `usuario_apelacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `usuario_apelacion` (
  `id_usuario` int(11) NOT NULL,
  `id_disputa` int(11) NOT NULL,
  `voto` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_usuario`,`id_disputa`),
  KEY `fk_usuario_apelacion_usuario_idx` (`id_usuario`),
  KEY `fk_usuario_apelacion_voto_idx` (`voto`),
  KEY `fk_usuario_apelacion_disputa_idx` (`id_disputa`),
  CONSTRAINT `fk_usuario_apelacion_disputa` FOREIGN KEY (`id_disputa`) REFERENCES `disputas` (`id_partido`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_usuario_apelacion_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_usuario_apelacion_voto` FOREIGN KEY (`voto`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_apelacion`
--

LOCK TABLES `usuario_apelacion` WRITE;
/*!40000 ALTER TABLE `usuario_apelacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario_apelacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_disputa`
--

DROP TABLE IF EXISTS `usuario_disputa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `usuario_disputa` (
  `id_usuario` int(11) NOT NULL,
  `id_disputa` int(11) NOT NULL,
  `id_voto` int(11) NOT NULL,
  PRIMARY KEY (`id_usuario`,`id_disputa`,`id_voto`),
  KEY `fk_usuario_disputa_usuario_idx` (`id_usuario`),
  KEY `fk_usuario_disputa_partido_idx` (`id_disputa`),
  KEY `fk_usuario_disputa_voto_idx` (`id_voto`),
  CONSTRAINT `fk_usuario_disputa_partido` FOREIGN KEY (`id_disputa`) REFERENCES `partidos` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_usuario_disputa_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_usuario_disputa_voto` FOREIGN KEY (`id_voto`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_disputa`
--

LOCK TABLES `usuario_disputa` WRITE;
/*!40000 ALTER TABLE `usuario_disputa` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario_disputa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_liga`
--

DROP TABLE IF EXISTS `usuario_liga`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `usuario_liga` (
  `id_usuario` int(11) NOT NULL,
  `id_liga` int(11) NOT NULL,
  PRIMARY KEY (`id_usuario`,`id_liga`),
  KEY `fk_usuario_liga_liga_idx` (`id_liga`),
  CONSTRAINT `fk_usuario_liga_liga` FOREIGN KEY (`id_liga`) REFERENCES `ligas` (`id`),
  CONSTRAINT `fk_usuario_liga_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_liga`
--

LOCK TABLES `usuario_liga` WRITE;
/*!40000 ALTER TABLE `usuario_liga` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario_liga` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `password` varchar(64) NOT NULL,
  `fechanac` datetime DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `apodo` varchar(20) DEFAULT NULL,
  `ultima_conexion` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `skype` varchar(45) DEFAULT NULL,
  `ip` varchar(40) DEFAULT NULL,
  `avatar` varchar(200) DEFAULT NULL,
  `pais` int(11) NOT NULL,
  `rol` int(11) NOT NULL,
  `estado` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`),
  KEY `fk_usuarios_pais_idx` (`pais`),
  KEY `fk_usuarios_rol_idx` (`rol`),
  KEY `fk_usuarios_estado_idx` (`estado`),
  CONSTRAINT `fk_usuarios_estado` FOREIGN KEY (`estado`) REFERENCES `estados` (`id`),
  CONSTRAINT `fk_usuarios_pais` FOREIGN KEY (`pais`) REFERENCES `paises` (`id`),
  CONSTRAINT `fk_usuarios_rol` FOREIGN KEY (`rol`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (45,'admin','1B0D820CF2EEA20000CC81EE9C6D50E690300AFBDAB3641B064153FA15462017','1993-03-17 00:00:00','admin@admin.com','admin','2019-08-11 19:21:02.649016','admin','admin','/media/datos/eclipse-java/winionline/avatars/admin.png',1,1,1),(49,'prueba','1B0D820CF2EEA20000CC81EE9C6D50E690300AFBDAB3641B064153FA15462017','1212-12-12 00:00:00','asd@asd.com','asdasd','2019-07-25 01:37:22.057200','','',NULL,1,2,1),(50,'asd','1B0D820CF2EEA20000CC81EE9C6D50E690300AFBDAB3641B064153FA15462017','1212-12-12 00:00:00','asd@asd.com','asd','2019-08-03 23:42:07.361665','','',NULL,1,2,1),(51,'rodcibils','1B0D820CF2EEA20000CC81EE9C6D50E690300AFBDAB3641B064153FA15462017','2008-10-01 00:00:00','asd@asd.com','rodri','2019-08-11 19:24:43.796747','','',NULL,1,2,1),(52,'ghjghj','1B0D820CF2EEA20000CC81EE9C6D50E690300AFBDAB3641B064153FA15462017','2018-12-31 00:00:00','asd@asd.com','','2019-07-24 00:48:44.462160','','',NULL,1,2,1),(55,'qwe','1B0D820CF2EEA20000CC81EE9C6D50E690300AFBDAB3641B064153FA15462017','2019-08-01 00:00:00','qwe@qwe.com','','2019-08-10 17:43:52.450493','','','/media/datos/eclipse-java/winionline/avatars/qwe.jpeg',1,2,1);
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'winionline'
--
/*!50106 SET @save_time_zone= @@TIME_ZONE */ ;
/*!50106 DROP EVENT IF EXISTS `cambiarEstadoLiga` */;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = utf8mb4 */ ;;
/*!50003 SET character_set_results = utf8mb4 */ ;;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`root`@`localhost`*/ /*!50106 EVENT `cambiarEstadoLiga` ON SCHEDULE EVERY 1 MINUTE STARTS '2019-07-12 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
     
   UPDATE ligas SET estado = 4 /*cambiar a estado iniciada*/
   WHERE fecha_inicio <= (select CURRENT_DATE) AND (select CURRENT_DATE) < fecha_fin;
   
   UPDATE ligas SET estado = 5 /*cambiar a estado terminada*/
   WHERE fecha_fin <= (select CURRENT_DATE);
   
END */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
/*!50106 DROP EVENT IF EXISTS `limpiarSolicitudesAmistoso` */;;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = utf8mb4 */ ;;
/*!50003 SET character_set_results = utf8mb4 */ ;;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'SYSTEM' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`root`@`localhost`*/ /*!50106 EVENT `limpiarSolicitudesAmistoso` ON SCHEDULE EVERY 1 DAY STARTS '2019-07-23 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO delete from solicitudes where
		vencimiento < CURRENT_DATE() and estado = 6; */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
DELIMITER ;
/*!50106 SET TIME_ZONE= @save_time_zone */ ;

--
-- Dumping routines for database 'winionline'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-08-24 16:15:00
