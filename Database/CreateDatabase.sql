-- MySQL Script generated by MySQL Workbench
-- Di 27 Nov 2018 18:49:04 CET
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
  `Balance` DECIMAL(13,2) NOT NULL DEFAULT 0.00,
  `LastSeen` DATETIME NULL,
  `available` TINYINT(1) NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`Admins`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`Admins` (
  `ID` INT NOT NULL,
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
  `ID` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `price` DECIMAL(13,2) NULL,
  `stock` INT NULL,
  `image_url` VARCHAR(128) NULL,
  `reorder_point` INT NULL,
  `available` TINYINT(1) NULL,
  `product_hash` BINARY(64) NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`Transactions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`Transactions` (
  `ID` INT NOT NULL,
  `Date` DATETIME NULL,
  `Type` CHAR(1) NOT NULL,
  `charged_amount` DECIMAL(13,2) NULL COMMENT 'store the amount of money here',
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
-- Table `matohmat`.`transaction_amount_products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`transaction_amount_products` (
  `Transactions_ID` INT NOT NULL,
  `Products_ID` INT NOT NULL,
  `count` INT NULL,
  PRIMARY KEY (`Transactions_ID`, `Products_ID`),
  INDEX `fk_Transactions_has_Products_Products1_idx` (`Products_ID` ASC),
  INDEX `fk_Transactions_has_Products_Transactions1_idx` (`Transactions_ID` ASC),
  CONSTRAINT `fk_Transactions_has_Products_Transactions1`
    FOREIGN KEY (`Transactions_ID`)
    REFERENCES `matohmat`.`Transactions` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Transactions_has_Products_Products1`
    FOREIGN KEY (`Products_ID`)
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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
