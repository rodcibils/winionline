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
  `resultado` int(11) DEFAULT NULL,
  KEY `fk_apelaciones_disputa_idx` (`id_disputa`),
  CONSTRAINT `fk_apelaciones_disputa` FOREIGN KEY (`id_disputa`) REFERENCES `disputas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `disputas`
--

DROP TABLE IF EXISTS `disputas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `disputas` (
  `id` int(11) NOT NULL,
  `id_partido` int(11) NOT NULL,
  `evidencia_uno` varchar(45) DEFAULT NULL,
  `evidencia_dos` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_partido_UNIQUE` (`id_partido`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  CONSTRAINT `fk_disputas_partido` FOREIGN KEY (`id_partido`) REFERENCES `partidos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `fecha_fin` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `partido_estado`
--

DROP TABLE IF EXISTS `partido_estado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `partido_estado` (
  `id_partido` int(11) NOT NULL,
  `id_estado` int(11) NOT NULL,
  KEY `fk_partido_estado_partido_idx` (`id_partido`),
  KEY `fk_partido_estado_estado_idx` (`id_estado`),
  CONSTRAINT `fk_partido_estado_estado` FOREIGN KEY (`id_estado`) REFERENCES `estados` (`id`),
  CONSTRAINT `fk_partido_estado_partido` FOREIGN KEY (`id_partido`) REFERENCES `partidos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `partidos`
--

DROP TABLE IF EXISTS `partidos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `partidos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_uno` int(11) NOT NULL,
  `id_dos` int(11) NOT NULL,
  `fecha` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_partidos_uno_idx` (`id_uno`),
  KEY `fk_partidos_dos_idx` (`id_dos`),
  CONSTRAINT `fk_partidos_dos` FOREIGN KEY (`id_dos`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_partidos_uno` FOREIGN KEY (`id_uno`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  KEY `fk_resultados_uno_idx` (`id_jugador`),
  KEY `fk_resultados_partido_idx` (`id_partido`),
  CONSTRAINT `fk_resultados_jugador` FOREIGN KEY (`id_jugador`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_resultados_partido` FOREIGN KEY (`id_partido`) REFERENCES `partidos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) DEFAULT NULL,
  `descripcion` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `solicitud_estado`
--

DROP TABLE IF EXISTS `solicitud_estado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `solicitud_estado` (
  `id_solicitud` int(11) NOT NULL,
  `id_estado` int(11) NOT NULL,
  KEY `fk_solicitud_estado_solicitud_idx` (`id_solicitud`),
  KEY `fk_solicitud_estado_estado_idx` (`id_estado`),
  CONSTRAINT `fk_solicitud_estado_estado` FOREIGN KEY (`id_estado`) REFERENCES `estados` (`id`),
  CONSTRAINT `fk_solicitud_estado_solicitud` FOREIGN KEY (`id_solicitud`) REFERENCES `solicitudes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `solicitudes`
--

DROP TABLE IF EXISTS `solicitudes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `solicitudes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_partido` int(11) DEFAULT NULL,
  `fecha` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_solicitudes_partido_idx` (`id_partido`),
  CONSTRAINT `fk_solicitudes_partido` FOREIGN KEY (`id_partido`) REFERENCES `partidos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario_apelacion`
--

DROP TABLE IF EXISTS `usuario_apelacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `usuario_apelacion` (
  `id_usuario` int(11) NOT NULL,
  `id_apelacion` int(11) NOT NULL,
  `voto` int(11) DEFAULT NULL,
  KEY `fk_usuario_apelacion_usuario_idx` (`id_usuario`),
  KEY `fk_usuario_apelacion_voto_idx` (`voto`),
  CONSTRAINT `fk_usuario_apelacion_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_usuario_apelacion_voto` FOREIGN KEY (`voto`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario_disputa`
--

DROP TABLE IF EXISTS `usuario_disputa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `usuario_disputa` (
  `id_usuario` int(11) NOT NULL,
  `id_partido` int(11) NOT NULL,
  `id_voto` int(11) NOT NULL,
  KEY `fk_usuario_disputa_usuario_idx` (`id_usuario`),
  KEY `fk_usuario_disputa_partido_idx` (`id_partido`),
  KEY `fk_usuario_disputa_voto_idx` (`id_voto`),
  CONSTRAINT `fk_usuario_disputa_partido` FOREIGN KEY (`id_partido`) REFERENCES `partidos` (`id`),
  CONSTRAINT `fk_usuario_disputa_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `fk_usuario_disputa_voto` FOREIGN KEY (`id_voto`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuario_rol`
--

DROP TABLE IF EXISTS `usuario_rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `usuario_rol` (
  `id_usuario` int(11) NOT NULL,
  `id_rol` int(11) NOT NULL,
  KEY `fk_rol_idx` (`id_rol`),
  KEY `fk_usuario_idx` (`id_usuario`),
  CONSTRAINT `fk_usuario_rol_rol` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_usuario_rol_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `fechanac` datetime DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `apodo` varchar(20) DEFAULT NULL,
  `ultima_conexion` datetime NOT NULL,
  `pais` varchar(30) DEFAULT NULL,
  `skype` varchar(45) DEFAULT NULL,
  `ip` varchar(40) DEFAULT NULL,
  `avatar` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `nombre_UNIQUE` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-06-22 14:23:26
