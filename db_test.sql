drop database if exists test_rest_api;
create database test_rest_api;

use test_rest_api;
  
--
-- Table  `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `image_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table `user_email`
--

DROP TABLE IF EXISTS `user_email`;
CREATE TABLE `user_email` (
  `id` bigint NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp90qkg1c3twn3he448x0cpqkp` (`user_id`),
  CONSTRAINT `FKp90qkg1c3twn3he448x0cpqkp` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

