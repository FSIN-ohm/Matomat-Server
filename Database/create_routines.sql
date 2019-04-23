USE matohmat;

DROP FUNCTION IF EXISTS random_name;
DELIMITER $$
CREATE FUNCTION random_name(s INT) RETURNS VARCHAR(45)
  DETERMINISTIC
BEGIN
  DECLARE name VARCHAR(45);
  SET name = concat(
                substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=s)*36+1, 1),
                substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=s+1)*36+1, 1),
                substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=s+2)*36+1, 1),
                substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=s+3)*36+1, 1),
                substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=s+4)*36+1, 1),
                substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=s+5)*36+1, 1),
                substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=s+6)*36+1, 1),
                substring('ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789', rand(@seed:=s+7)*36+1, 1));
  RETURN name;
END$$

DROP PROCEDURE IF EXISTS user_create;
CREATE PROCEDURE `user_create`( IN auth char(40) )
BEGIN
-- Check if user already exists to not delete his money balance
	SET @existing = FALSE;
	CALL user_hash_exists(auth, @existing);
	IF ( NOT @existing = true ) THEN
		-- create user. id, date and balance are filled with default values
		INSERT INTO users(auth_hash)
		VALUES ( BINARY( UNHEX(auth) ));

		UPDATE users
		SET name = random_name(LAST_INSERT_ID())
	    WHERE id = LAST_INSERT_ID();
    END IF;
END;

DROP PROCEDURE IF EXISTS admin_create;
CREATE PROCEDURE `admin_create` (
   IN username VARCHAR(45),
   IN email VARCHAR(45),
   IN pass BINARY(64),
   IN salt BINARY(64))
BEGIN
	IF EXISTS (SELECT * FROM admins WHERE admins.username = username OR admins.email = email) THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'User already exists';
	ELSE
		CALL user_create(sha1(username));
        INSERT INTO admins(id, username, password, password_salt, email)
        VALUES (LAST_INSERT_ID(), username, pass, salt, email);
    END IF;
END;

DROP PROCEDURE IF EXISTS admin_update;
CREATE PROCEDURE `admin_update` (
   IN aid INT,
   IN uname VARCHAR(45),
   IN mail VARCHAR(45),
   IN pass BINARY(64),
   IN salt BINARY(64),
   IN av BOOLEAN)
BEGIN
	UPDATE admins
	SET username = uname, email = mail, password = pass, password_salt = salt
	WHERE id = aid;

	UPDATE users
	SET available = av
	WHERE id = aid;
END;

DROP PROCEDURE IF EXISTS product_add;
CREATE PROCEDURE `product_add` (
	IN product_name varchar(45),
    IN prce DECIMAL(13,2),
    IN img_url VARCHAR(2083),
    IN reorder INT,
    IN bcode VARCHAR(256),
    IN items_crate INT)
BEGIN
	INSERT INTO products(name, image_url, reorder_point, available, barcode, items_per_crate )
    VALUES (product_name, img_url, reorder, TRUE, bcode, items_crate);
    
    INSERT INTO product_prices(price, product_id)
    VALUES (prce, LAST_INSERT_ID());
END;

DROP PROCEDURE IF EXISTS product_update;
CREATE PROCEDURE `product_update`(
	IN detail_id INT,
	IN product_name varchar(45),
  IN i_url VARCHAR(2083),
  IN reorder INT,
  IN avail boolean,
  IN bcode VARCHAR(256),
  IN items_crate INT)
BEGIN
	UPDATE products
    SET name = product_name, image_url = i_url,
    reorder_point = reorder, available = avail, barcode = bcode, items_per_crate = items_crate
    WHERE id = detail_id;
END;

DROP PROCEDURE IF EXISTS product_update_price;
CREATE PROCEDURE `product_update_price` (IN pid INT, IN new_price DECIMAL(13,2) )
BEGIN
    INSERT INTO product_prices(price, product_id)
    VALUES (new_price, pid);
END;

DROP PROCEDURE IF EXISTS transaction_deposit;
CREATE DEFINER=`root`@`localhost` PROCEDURE `transaction_deposit`( IN user_id_in INT, IN in_amount DECIMAL(13,2) )
BEGIN
	INSERT INTO transactions(sender, recipient) 
    VALUES (1, user_id_in);
    INSERT INTO transfers(transaction_id, amount)
    VALUES (LAST_INSERT_ID(), in_amount);
END;

DROP PROCEDURE IF EXISTS transaction_order;
CREATE PROCEDURE `transaction_order` (
IN adminId INT,
IN co DECIMAL(13,2))
BEGIN
  DECLARE trans_id INT DEFAULT 0;

	INSERT INTO transactions(sender, recipient)
  VALUES (2, 1);

  SELECT LAST_INSERT_ID() INTO trans_id;

  INSERT INTO matohmat.`orders`(transaction_id, buy_cost, admin_id)
  VALUES (LAST_INSERT_ID(), co, adminId);

  SELECT trans_id;
END;

DROP PROCEDURE IF EXISTS transaction_purchase;
CREATE PROCEDURE `transaction_purchase` (IN user INT)
BEGIN
	INSERT INTO transactions(sender, recipient)
    VALUES (user, 2); 
    SELECT LAST_INSERT_ID();
END;

DROP PROCEDURE IF EXISTS transaction_transfer;
CREATE DEFINER=`root`@`localhost` PROCEDURE `transaction_transfer`( IN user_id_from INT, IN user_id_to INT, IN in_amount DECIMAL(13,2) )
BEGIN
	INSERT INTO transactions(sender, recipient) 
    VALUES (user_id_from, user_id_to);
    INSERT INTO transfers(transaction_id, amount)
    VALUES (LAST_INSERT_ID(), in_amount);
END;

DROP PROCEDURE IF EXISTS transaction_withdraw;
CREATE DEFINER=`root`@`localhost` PROCEDURE `transaction_withdraw`(IN user_id_in INT, IN amount DECIMAL(13,2) )
BEGIN
	INSERT INTO transactions(sender, recipient) 
    VALUES (user_id_in, 1);
    INSERT INTO transfers(transaction_id, amount)
    VALUES (LAST_INSERT_ID(), amount);
END;

DROP PROCEDURE IF EXISTS user_authenticate;
CREATE DEFINER=`root`@`localhost` PROCEDURE `user_authenticate`(
	IN auth_key CHAR(40), 
	OUT user_id_out INT, 
    OUT balance_out DECIMAL(13,2) )
BEGIN
    SELECT users.id, user_balance.balance 
    INTO user_id_out, balance_out 
    FROM users, user_balance 
    WHERE users.auth_hash = BINARY(UNHEX(auth_key)) AND users.id = user_balance.id;
END;

DROP PROCEDURE IF EXISTS user_hash_exists;
CREATE DEFINER=`root`@`localhost` PROCEDURE `user_hash_exists`( IN auth_key CHAR(40), OUT existing BOOLEAN )
BEGIN
	IF (  EXISTS ( SELECT * FROM users WHERE auth_hash = BINARY(UNHEX(auth_key)) ) ) THEN
		SET existing = TRUE;
	ELSE 
		SET existing = FALSE;
    END IF;
END;

DROP PROCEDURE IF EXISTS user_id_exists;
CREATE
  definer = root@localhost PROCEDURE user_id_exists(IN user_id int, OUT existing tinyint(1))
BEGIN
	IF (  EXISTS ( SELECT * FROM users WHERE id = user_id ) ) THEN
		SET existing = TRUE;
	ELSE 
		SET existing = FALSE;
    END IF;
END;

DROP PROCEDURE IF EXISTS user_update;
CREATE PROCEDURE `user_update` (uid INT, IN auth binary(20), IN uname char(45), IN av BOOLEAN)
BEGIN
	UPDATE users
	  SET auth_hash = auth, name = uname, available = av
  WHERE id = uid;
END;

DROP PROCEDURE IF EXISTS user_update_access_date;
CREATE DEFINER=`root`@`localhost` PROCEDURE `user_update_access_date`(IN user_id INT)
BEGIN
	UPDATE users SET `users`.`last_seen` = NOW() WHERE users.id = user_id;
END;







