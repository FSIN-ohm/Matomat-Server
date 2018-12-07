-- MySQL Script generated by MySQL Workbench
-- Fr 07 Dez 2018 14:40:12 CET
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema matohmat
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema matohmat
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `matohmat` DEFAULT CHARACTER SET utf8 ;
USE `matohmat` ;

-- -----------------------------------------------------
-- Table `matohmat`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`Users` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `auth_hash` BINARY(64) NOT NULL COMMENT 'Use UNHEX() to use the Data',
  `start_balance` DECIMAL(13,2) NOT NULL DEFAULT 0.00,
  `LastSeen` DATETIME NULL,
  `available` TINYINT(1) NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`Admins`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`Admins` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NULL,
  `password` BINARY(64) NOT NULL COMMENT 'use sha3 and salt to secure password',
  `email` VARCHAR(45) NULL,
  `password_salt` BINARY(64) NOT NULL,
  `User_ID` INT NOT NULL,
  PRIMARY KEY (`ID`, `User_ID`),
  INDEX `fk_Admins_User1_idx` (`User_ID` ASC),
  CONSTRAINT `fk_Admins_User1`
    FOREIGN KEY (`User_ID`)
    REFERENCES `matohmat`.`Users` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`Products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`Products` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `price` DECIMAL(13,2) NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`Transactions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`Transactions` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Date` DATETIME NULL,
  `sender` INT NOT NULL,
  `recipient` INT NOT NULL,
  PRIMARY KEY (`ID`, `sender`, `recipient`),
  INDEX `fk_Transactions_User1_idx` (`sender` ASC),
  INDEX `fk_Transactions_User2_idx` (`recipient` ASC),
  CONSTRAINT `fk_Transactions_User1`
    FOREIGN KEY (`sender`)
    REFERENCES `matohmat`.`Users` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Transactions_User2`
    FOREIGN KEY (`recipient`)
    REFERENCES `matohmat`.`Users` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`Purchases`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`Purchases` (
  `Transaction_ID` INT NOT NULL,
  `Product_ID` INT NOT NULL,
  `count` INT NULL,
  PRIMARY KEY (`Transaction_ID`, `Product_ID`),
  INDEX `fk_Transactions_has_Products_Products1_idx` (`Product_ID` ASC),
  INDEX `fk_Transactions_has_Products_Transactions1_idx` (`Transaction_ID` ASC),
  CONSTRAINT `fk_Transactions_has_Products_Transactions1`
    FOREIGN KEY (`Transaction_ID`)
    REFERENCES `matohmat`.`Transactions` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Transactions_has_Products_Products1`
    FOREIGN KEY (`Product_ID`)
    REFERENCES `matohmat`.`Products` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`MergeMetadata`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`MergeMetadata` (
  `ID` INT NOT NULL,
  `LastMerge` DATETIME NOT NULL,
  `merge_threshold` INT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`Transfers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`Transfers` (
  `transaction_ID` INT NOT NULL,
  `charged_amount` DECIMAL(13,2) NULL,
  INDEX `fk_Transfare_amount_1_idx` (`transaction_ID` ASC),
  PRIMARY KEY (`transaction_ID`),
  CONSTRAINT `fk_Transfare_amount_1`
    FOREIGN KEY (`transaction_ID`)
    REFERENCES `matohmat`.`Transactions` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `matohmat`.`Product_infos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`Product_infos` (
  `product_ID` INT NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name` VARCHAR(45) NULL,
  `image_url` VARCHAR(128) NULL,
  `reorder_point` INT NULL,
  `product_hash` BINARY(64) NULL,
  `available` TINYINT(1) NULL,
  PRIMARY KEY (`product_ID`),
  CONSTRAINT `fk_Product_infos_1`
    FOREIGN KEY (`product_ID`)
    REFERENCES `matohmat`.`Products` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
