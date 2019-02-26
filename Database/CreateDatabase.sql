-- MySQL Script generated by MySQL Workbench
-- Tue Feb 26 11:03:08 2019
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema matohmat
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `matohmat` ;

-- -----------------------------------------------------
-- Schema matohmat
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `matohmat` DEFAULT CHARACTER SET utf8mb4 ;
USE `matohmat` ;

-- -----------------------------------------------------
-- Table `matohmat`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `auth_hash` BINARY(20) NOT NULL COMMENT 'Use UNHEX() to use the Data',
  `balance` DECIMAL(13,2) NOT NULL DEFAULT 0.00,
  `last_seen` DATETIME NOT NULL DEFAULT NOW(),
  PRIMARY KEY (`id`),
  UNIQUE INDEX `auth_hash_UNIQUE` (`auth_hash` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`admins`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`admins` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) CHARACTER SET 'utf8mb4' NOT NULL,
  `password` BINARY(64) NOT NULL COMMENT 'use sha3 and salt to secure password',
  `email` VARCHAR(45) NOT NULL,
  `password_salt` BINARY(64) NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_Admins_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `matohmat`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`product_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`product_detail` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `image_url` VARCHAR(2083) NULL,
  `reorder_point` INT NULL,
  `stock` INT NOT NULL DEFAULT 0,
  `available` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`products` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `price` DECIMAL(13,2) NOT NULL,
  `valid_from` DATETIME NOT NULL DEFAULT NOW(),
  `product_detail_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_products_product_detail_idx` (`product_detail_id` ASC) VISIBLE,
  CONSTRAINT `fk_products_product_detail`
    FOREIGN KEY (`product_detail_id`)
    REFERENCES `matohmat`.`product_detail` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`transactions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`transactions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATETIME NOT NULL DEFAULT NOW(),
  `sender` INT NOT NULL,
  `recipient` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_transaction_sender_idx` (`sender` ASC) VISIBLE,
  INDEX `fk_transaction_recipient_idx` (`recipient` ASC) VISIBLE,
  CONSTRAINT `fk_transaction_sender`
    FOREIGN KEY (`sender`)
    REFERENCES `matohmat`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_transaction_recipient`
    FOREIGN KEY (`recipient`)
    REFERENCES `matohmat`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`purchase_amount_products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`purchase_amount_products` (
  `products_id` INT NOT NULL,
  `count` INT NULL,
  `transaction_id` INT NOT NULL,
  `id` INT NOT NULL,
  INDEX `fk_products_idx` (`products_id` ASC) VISIBLE,
  INDEX `fk_transaction_idx` (`transaction_id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_products`
    FOREIGN KEY (`products_id`)
    REFERENCES `matohmat`.`products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_transaction`
    FOREIGN KEY (`transaction_id`)
    REFERENCES `matohmat`.`transactions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`merge_metadata`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`merge_metadata` (
  `id` INT NOT NULL,
  `last_merge` DATETIME NOT NULL,
  `next_merge` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`transfer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`transfer` (
  `transaction_id` INT NOT NULL,
  `amount` DECIMAL(13,2) NOT NULL,
  PRIMARY KEY (`transaction_id`),
  CONSTRAINT `fk_transfer_transaction`
    FOREIGN KEY (`transaction_id`)
    REFERENCES `matohmat`.`transactions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`order`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`order` (
  `transaction_id` INT NOT NULL AUTO_INCREMENT,
  `state` ENUM('placed', 'payed', 'delivered') NULL,
  `cost` DECIMAL(13,2) NOT NULL,
  `admin_id` INT NOT NULL,
  PRIMARY KEY (`transaction_id`),
  CONSTRAINT `fk_order_transaction`
    FOREIGN KEY (`transaction_id`)
    REFERENCES `matohmat`.`transactions` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `matohmat`.`ordered_products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`ordered_products` (
  `order_transaction_id` INT NOT NULL,
  `product_detail_id` INT NOT NULL,
  `count` INT NOT NULL,
  PRIMARY KEY (`order_transaction_id`, `product_detail_id`),
  INDEX `fk_order_has_product_detail_product_detail_idx` (`product_detail_id` ASC) VISIBLE,
  INDEX `fk_order_has_product_detail_order_idx` (`order_transaction_id` ASC) VISIBLE,
  CONSTRAINT `fk_order_order`
    FOREIGN KEY (`order_transaction_id`)
    REFERENCES `matohmat`.`order` (`transaction_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_product_detail`
    FOREIGN KEY (`product_detail_id`)
    REFERENCES `matohmat`.`product_detail` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `matohmat` ;

-- -----------------------------------------------------
-- Placeholder table for view `matohmat`.`user_balance`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`user_balance` (`id` INT, `last_seen` INT, `balance` INT);

-- -----------------------------------------------------
-- Placeholder table for view `matohmat`.`products_current`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`products_current` (`prod_id` INT, `price` INT, `name` INT, `reorder_point` INT, `image_url` INT, `stock` INT, `available` INT);

-- -----------------------------------------------------
-- Placeholder table for view `matohmat`.`purchase_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`purchase_detail` (`id` INT, `date` INT, `sender` INT, `count` INT, `price` INT, `name` INT, `image_url` INT);

-- -----------------------------------------------------
-- Placeholder table for view `matohmat`.`transfer_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`transfer_detail` (`id` INT, `date` INT, `sender` INT, `recipient` INT, `amount` INT);

-- -----------------------------------------------------
-- Placeholder table for view `matohmat`.`transactions_total`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`transactions_total` (`id` INT, `date` INT, `sender` INT, `recipient` INT, `amount` INT);

-- -----------------------------------------------------
-- Placeholder table for view `matohmat`.`purchase_total`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`purchase_total` (`id` INT, `date` INT, `sender` INT, `recipient` INT, `amount` INT);

-- -----------------------------------------------------
-- Placeholder table for view `matohmat`.`products_all`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `matohmat`.`products_all` (`prod_id` INT, `price` INT, `name` INT, `reorder_point` INT, `image_url` INT, `stock` INT, `available` INT);

-- -----------------------------------------------------
-- procedure user_create
-- -----------------------------------------------------

DELIMITER $$
USE `matohmat`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `user_create`( IN auth char(40) )
BEGIN
-- Check if user already exists to not delete his money balance
	SET @existing = FALSE; 
	CALL user_hash_exists(auth, @existing);
	IF ( NOT @existing = true ) THEN
		-- create user. id, date and balance are filled with default values
		INSERT INTO users(auth_hash) 
		VALUES ( BINARY( UNHEX(auth) ) ); 
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure user_authenticate
-- -----------------------------------------------------

DELIMITER $$
USE `matohmat`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `user_authenticate`(
	IN auth_key CHAR(40), 
	OUT user_id_out INT, 
    OUT balance_out DECIMAL(13,2) )
BEGIN
    #CALL user_hash_exists(auth_key, @user_existing);
	#IF ( @user_existing = TRUE ) THEN
	#	CALL user_create(auth_key);
    #END IF;

    SELECT users.id, user_balance.balance 
    INTO user_id_out, balance_out 
    FROM users, user_balance 
    WHERE users.auth_hash = BINARY(UNHEX(auth_key)) AND users.id = user_balance.id;

END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure user_update_access_date
-- -----------------------------------------------------

DELIMITER $$
USE `matohmat`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `user_update_access_date`(IN user_id INT)
BEGIN
	UPDATE users SET `users`.`last_seen` = NOW() WHERE users.id = user_id;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure user_hash_exists
-- -----------------------------------------------------

DELIMITER $$
USE `matohmat`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `user_hash_exists`( IN auth_key CHAR(40), OUT existing BOOLEAN )
BEGIN
	IF (  EXISTS ( SELECT * FROM users WHERE auth_hash = BINARY(UNHEX(auth_key)) ) ) THEN
		SET existing = TRUE;
	ELSE 
		SET existing = FALSE;
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure user_id_exists
-- -----------------------------------------------------

DELIMITER $$
USE `matohmat`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `user_id_exists`(IN user_id int, OUT existing BOOLEAN)
BEGIN
	IF (  EXISTS ( SELECT * FROM users WHERE id = user_id ) ) THEN
		SET existing = TRUE;
	ELSE 
		SET existing = FALSE;
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure transaction_withdraw
-- -----------------------------------------------------

DELIMITER $$
USE `matohmat`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `transaction_withdraw`(IN user_id_in INT, IN amount DECIMAL(13,2) )
BEGIN
	INSERT INTO transactions(sender, recipient) 
    VALUES (user_id_in, 1);
    INSERT INTO transfer(transaction_id, amount)
    VALUES (LAST_INSERT_ID(), amount);
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure transaction_deposit
-- -----------------------------------------------------

DELIMITER $$
USE `matohmat`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `transaction_deposit`( IN user_id_in INT, IN in_amount DECIMAL(13,2) )
BEGIN
	INSERT INTO transactions(sender, recipient) 
    VALUES (1, user_id_in);
    INSERT INTO transfer(transaction_id, amount)
    VALUES (LAST_INSERT_ID(), in_amount);
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure product_add
-- -----------------------------------------------------

DELIMITER $$
USE `matohmat`$$
CREATE PROCEDURE `product_add` (
	IN product_name varchar(45),
    IN price DECIMAL(13,2),
    IN image_url VARCHAR(128), 
    IN reorder INT)
BEGIN
	INSERT INTO product_detail( name, image_url, reorder_point, stock, available )
    VALUES (product_name, image_url, reorder, 0, TRUE);
    
    INSERT INTO products(price, product_detail_id)
    VALUES (price, LAST_INSERT_ID());
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure product_update_price
-- -----------------------------------------------------

DELIMITER $$
USE `matohmat`$$
CREATE PROCEDURE `product_update_price` (IN prod_detail_id INT, IN new_price DECIMAL(13,2) )
BEGIN
	INSERT INTO products(price, product_detail_id)
    VALUES (new_price, prod_detail_id);
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure admin_create
-- -----------------------------------------------------

DELIMITER $$
USE `matohmat`$$
CREATE PROCEDURE `admin_create` (
IN username VARCHAR(45),
IN email VARCHAR(45),
IN pass CHAR(40),
IN salt CHAR(40)
)
BEGIN
	IF EXISTS (SELECT * FROM admins WHERE admins.username = username OR admins.email = email) THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'User already exists';
	ELSE
		CALL user_create(sha1(username));
        INSERT INTO admins(username, password, password_salt, email, user_id)
        VALUES (username, pass, salt, email, LAST_INSERT_ID());
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure transaction_purchase
-- -----------------------------------------------------

DELIMITER $$
USE `matohmat`$$
CREATE PROCEDURE `transaction_purchase` (
IN user INT
#IN product INT,
#IN count INT)
)
BEGIN
	INSERT INTO transactions(sender, recipient)
    VALUES (user, 2); 
    SELECT LAST_INSERT_ID();
    # INSERT INTO purchase_amount_products(products_id, count, transaction_id)
    # VALUES (product, count, LAST_INSERT_ID());
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure transaction_order
-- -----------------------------------------------------

DELIMITER $$
USE `matohmat`$$
CREATE PROCEDURE `transaction_order` (
IN adminId INT,
IN cost DECIMAL(13,2)
)
BEGIN
	INSERT INTO transactions(sender, recipient)
    VALUES (2, 1); 
    
    INSERT INTO matohmat.order() 
    SELECT LAST_INSERT_ID();
    # INSERT INTO purchase_amount_products(products_id, count, transaction_id)
    # VALUES (product, count, LAST_INSERT_ID());
END$$

DELIMITER ;

-- -----------------------------------------------------
-- View `matohmat`.`user_balance`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `matohmat`.`user_balance`;
USE `matohmat`;
CREATE  OR REPLACE VIEW `user_balance` AS
SELECT 
    id, last_seen, (balance + ifnull(in_amount, 0) - ifnull(out_amount,0) ) AS balance
FROM
    users
        LEFT JOIN
    (SELECT 
        sender, SUM(amount) AS out_amount
    FROM
        transactions_total
    GROUP BY sender) as in_transactions ON sender = users.id
    left join 
    (SELECT 
        recipient, SUM(amount) AS in_amount
    FROM
        transactions_total
    GROUP BY recipient) as out_transactions ON recipient = users.id;

-- -----------------------------------------------------
-- View `matohmat`.`products_current`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `matohmat`.`products_current`;
USE `matohmat`;
CREATE  OR REPLACE VIEW `products_current` AS
SELECT 
    products.id AS prod_id,
    price,
    product_detail.name AS name,
    product_detail.reorder_point AS reorder_point,
    image_url,
    stock,
    available
FROM
    products
        JOIN
    (SELECT 
        product_detail_id, MAX(valid_from) AS valid_date
    FROM
        products
    GROUP BY product_detail_id) AS t1 ON products.product_detail_id = t1.product_detail_id
        AND products.valid_from = t1.valid_date
        JOIN
    product_detail ON products.product_detail_id = product_detail.id
WHERE
    available = TRUE;

-- -----------------------------------------------------
-- View `matohmat`.`purchase_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `matohmat`.`purchase_detail`;
USE `matohmat`;
CREATE  OR REPLACE VIEW `purchase_detail` AS
    SELECT 
        transactions.id, date, sender, count, price, name, image_url
    FROM
        matohmat.purchase_amount_products
            JOIN
        transactions ON transactions.id = transaction_id
            JOIN
        products ON products.id = products_id
            JOIN
        product_detail ON products.product_detail_id = product_detail.id;

-- -----------------------------------------------------
-- View `matohmat`.`transfer_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `matohmat`.`transfer_detail`;
USE `matohmat`;
CREATE  OR REPLACE VIEW `transfer_detail` AS
SELECT transactions.id, date, sender, recipient, amount FROM matohmat.transactions
join transfer on transfer.transaction_id = transactions.id;

-- -----------------------------------------------------
-- View `matohmat`.`transactions_total`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `matohmat`.`transactions_total`;
USE `matohmat`;
CREATE  OR REPLACE VIEW `transactions_total` AS
SELECT 
    id, date, sender, recipient, amount
FROM
    transfer_detail
UNION ALL SELECT 
    id, date, sender, recipient, amount
FROM
    purchase_total;

-- -----------------------------------------------------
-- View `matohmat`.`purchase_total`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `matohmat`.`purchase_total`;
USE `matohmat`;
CREATE  OR REPLACE VIEW `purchase_total` AS
SELECT 
    transactions.id, date, sender, recipient, amount
FROM
    transactions
        RIGHT JOIN
    (SELECT 
        transaction_id, SUM(count * price) AS amount
    FROM
        transactions
    RIGHT JOIN purchase_amount_products ON purchase_amount_products.transaction_id = transactions.id
    RIGHT JOIN products ON purchase_amount_products.products_id = products.id
    GROUP BY transaction_id) AS total ON total.transaction_id = transactions.id;

-- -----------------------------------------------------
-- View `matohmat`.`products_all`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `matohmat`.`products_all`;
USE `matohmat`;
CREATE  OR REPLACE VIEW `products_all` AS
SELECT 
    products.id AS prod_id,
    price,
    product_detail.name AS name,
    product_detail.reorder_point AS reorder_point,
    image_url,
    stock,
    available
FROM
    products
        JOIN
    (SELECT 
        product_detail_id, MAX(valid_from) AS valid_date
    FROM
        products
    GROUP BY product_detail_id) AS t1 ON products.product_detail_id = t1.product_detail_id
        AND products.valid_from = t1.valid_date
        JOIN
    product_detail ON products.product_detail_id = product_detail.id;
CREATE USER 'matomat_system' IDENTIFIED BY 'test1234';

GRANT EXECUTE ON `matohmat`.* TO 'matomat_system'@'%';
GRANT SELECT, INSERT, TRIGGER ON TABLE `matohmat`.* TO 'matomat_system';
GRANT SELECT, INSERT, TRIGGER ON TABLE `matohmat`.* TO 'matomat_system';
GRANT INSERT, SELECT, UPDATE, TRIGGER ON TABLE `matohmat`.`admins` TO 'matomat_system';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `matohmat`.`users`
-- -----------------------------------------------------
START TRANSACTION;
USE `matohmat`;
INSERT INTO `matohmat`.`users` (`id`, `auth_hash`, `balance`, `last_seen`) VALUES (1, 0x7C211433F02071597741E6FF5A8EA34789ABBF43, 0, NOW());
INSERT INTO `matohmat`.`users` (`id`, `auth_hash`, `balance`, `last_seen`) VALUES (2, 0x88B0A95EA070652373AC2436EC1FB0ECB1E2F424, 0, NOW());
INSERT INTO `matohmat`.`users` (`id`, `auth_hash`, `balance`, `last_seen`) VALUES (3, 0x4E7AFEBCFBAE200B22C7C85E5560F89A2A0280B4, 0, NOW());
INSERT INTO `matohmat`.`admins` (`id`, `username`, `password`, `email`, `password_salt`, `user_id`) VALUES (1, 'Admin', 0x1195F4253D04DF4FACBC02CB25572CD1C46AF1A0, 'fachsachft-in@th-nuernberg.de', 0xC36A23B24114C01E387B976A88687411B4BBDD7A, 3);

COMMIT;



