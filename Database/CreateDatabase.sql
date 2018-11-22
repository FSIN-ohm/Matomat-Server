-- MySQL Script generated by MySQL Workbench
-- Do 22 Nov 2018 18:52:12 CET
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Users` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `auth_hash` BINARY(64) NOT NULL COMMENT 'Use UNHEX() to use the Data',
  `Balance` DECIMAL(13,2) NOT NULL DEFAULT 0.00,
  `LastSeen` DATETIME NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `RFID_UNIQUE` (`auth_hash` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Admins`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Admins` (
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
    REFERENCES `mydb`.`Users` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Products` (
  `ID` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  `price` DECIMAL(13,2) NULL,
  `stock` INT NULL,
  `image_url` VARCHAR(128) NULL,
  `reorder_point` INT NULL,
  `available` TINYINT(1) NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Transaction_Type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Transaction_Type` (
  `short` CHAR(1) NOT NULL,
  `description` VARCHAR(45) NULL,
  PRIMARY KEY (`short`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Transactions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Transactions` (
  `ID` INT NOT NULL,
  `Date` DATETIME NULL,
  `Type` CHAR(1) NOT NULL,
  `charged_amount` DECIMAL(13,2) NULL COMMENT 'store the amount of money here',
  `products` INT NULL,
  `sender` INT NOT NULL,
  `recipient` INT NOT NULL,
  PRIMARY KEY (`ID`, `sender`, `recipient`),
  INDEX `fk_Transactions_Transaction_Type_idx` (`Type` ASC),
  INDEX `fk_Transactions_User1_idx` (`sender` ASC),
  INDEX `fk_Transactions_User2_idx` (`recipient` ASC),
  CONSTRAINT `fk_Transactions_Transaction_Type`
    FOREIGN KEY (`Type`)
    REFERENCES `mydb`.`Transaction_Type` (`short`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Transactions_User1`
    FOREIGN KEY (`sender`)
    REFERENCES `mydb`.`Users` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Transactions_User2`
    FOREIGN KEY (`recipient`)
    REFERENCES `mydb`.`Users` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`transaction_amount_products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`transaction_amount_products` (
  `Transactions_ID` INT NOT NULL,
  `Products_ID` INT NOT NULL,
  `count` INT NULL,
  PRIMARY KEY (`Transactions_ID`, `Products_ID`),
  INDEX `fk_Transactions_has_Products_Products1_idx` (`Products_ID` ASC),
  INDEX `fk_Transactions_has_Products_Transactions1_idx` (`Transactions_ID` ASC),
  CONSTRAINT `fk_Transactions_has_Products_Transactions1`
    FOREIGN KEY (`Transactions_ID`)
    REFERENCES `mydb`.`Transactions` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Transactions_has_Products_Products1`
    FOREIGN KEY (`Products_ID`)
    REFERENCES `mydb`.`Products` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`MergeMetadata`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`MergeMetadata` (
  `ID` INT NOT NULL,
  `LastMerge` DATETIME NOT NULL,
  `merge_threshold` INT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;

USE `mydb` ;

-- -----------------------------------------------------
-- Placeholder table for view `mydb`.`user_in_transactions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`user_in_transactions` (`deposit` INT);

-- -----------------------------------------------------
-- Placeholder table for view `mydb`.`user_out_transactions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`user_out_transactions` (`withdrawal` INT);

-- -----------------------------------------------------
-- Placeholder table for view `mydb`.`user_balance`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`user_balance` (`(User.balance + user_in_transactions - user_out_transactions)` INT);

-- -----------------------------------------------------
-- View `mydb`.`user_in_transactions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`user_in_transactions`;
USE `mydb`;
CREATE  OR REPLACE VIEW `user_in_transactions` AS  select SUM(charged_amount) as deposit from transactions where transactions.Date < MergeMetadata.LastMerge and transactions.Type = 'I';

-- -----------------------------------------------------
-- View `mydb`.`user_out_transactions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`user_out_transactions`;
USE `mydb`;
CREATE  OR REPLACE VIEW `user_out_transactions` AS select SUM(charged_amount) as withdrawal from transactions where transactions.Date < Merge.LastMerge and transactions.Type = 'T' or transactions.Type = 'O';

-- -----------------------------------------------------
-- View `mydb`.`user_balance`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`user_balance`;
USE `mydb`;
CREATE  OR REPLACE VIEW `user_balance` AS select (User.balance + user_in_transactions - user_out_transactions) from User, user_in_balance, user_out_balance where user = targetUser;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
