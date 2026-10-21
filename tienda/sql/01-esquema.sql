-- Ejecutar con una cuenta administradora en MySQL Workbench.
-- No borra tablas ni bases de datos existentes.
CREATE DATABASE IF NOT EXISTS tienda CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE tienda;
CREATE TABLE IF NOT EXISTS productos (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  precio DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT chk_precio CHECK (precio >= 0),
  CONSTRAINT chk_stock CHECK (stock >= 0)
) ENGINE=InnoDB;
CREATE TABLE IF NOT EXISTS usuarios (
  id INT NOT NULL AUTO_INCREMENT,
  usuario VARCHAR(50) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_usuario (usuario)
) ENGINE=InnoDB;
