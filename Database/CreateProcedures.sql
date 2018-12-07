USE matohmat;

DROP PROCEDURE IF EXISTS ADD_ADMIN;
CREATE PROCEDURE ADD_ADMIN
  (uname VARCHAR(45),
  passwd BINARY(64),
  mail VARCHAR(45))
  BEGIN
    INSERT INTO Users
           (auth_hash, start_balance, LastSeen, available)
    VALUES
           ('', 0.00, CURDATE(), 1);
    INSERT INTO Admins
        (username, password, email, password_salt, User_ID)
    VALUES
           (uname, passwd, mail, RAND(), LAST_INSERT_ID());
  end;

DROP PROCEDURE IF EXISTS SET_ADMIN;
CREATE PROCEDURE SET_ADMIN
  (admin_id INTEGER,
  uname VARCHAR(45),
  passwd BINARY(64),
  mail VARCHAR(45))
  BEGIN
    UPDATE Admins A
    SET
      A.username = uname,
      A.password = passwd,
      A.email = mail
    WHERE A.ID = admin_id;
  end;

DROP PROCEDURE IF EXISTS ADD_USER;
CREATE PROCEDURE ADD_USER
  (hash BINARY(64))
  BEGIN
    INSERT INTO Users
        (auth_hash, start_balance, LastSeen, available)
    VALUES
         (hash, 0.00, CURDATE(), 1);
  end;

DROP PROCEDURE IF EXISTS SET_USER;
CREATE PROCEDURE SET_USER
  (user_id INTEGER, hash BINARY(64), isAvailable TINYINT(1))
  BEGIN
    UPDATE Users
        SET ID = user_id, auth_hash = hash, available = isAvailable;
  end;

DROP PROCEDURE IF EXISTS ADD_PRODUCT;
CREATE PROCEDURE ADD_PRODUCT
  (product_price DECIMAL(13,2),
  product_name VARCHAR(45),
  i_url VARCHAR(128),
  reorder_p INT(11),
  hash binary(64))
  BEGIN
    INSERT INTO Product_infos
        (name, image_url, reorder_point, product_hash, available)
    VALUES
           (product_name, i_url, reorder_p, hash, 1);
    INSERT INTO Products
        (price, Product_info_ID)
    VALUES
           (product_price, LAST_INSERT_ID());
  end;

DROP PROCEDURE IF EXISTS SET_PRODUCT;
CREATE PROCEDURE SET_PRODUCT
  (product_id INTEGER,
    product_name VARCHAR(45),
  i_url VARCHAR(128),
  reorder_p INT(11),
  hash binary(64))
  BEGIN
    UPDATE Product_infos
        SET name = product_name,
            image_url = i_url,
            reorder_point = reorder_p,
            product_hash = hash
    WHERE ID = product_id;
  end;

DROP PROCEDURE IF EXISTS ADD_TRANSFER;
CREATE PROCEDURE ADD_TRANSFER
  (sender_user INT(11),
  recipient_user INT(11),
  charge DECIMAL(13,2))
  BEGIN
    INSERT INTO Transactions
        (Date, sender, recipient)
    VALUES
           (CURDATE(), sender_user, recipient_user);
    INSERT INTO Transfers
        (transaction_ID, charged_amount)
    VALUES
           (LAST_INSERT_ID(), charge);
  end;

CREATE PROCEDURE ADD_PURCHASE_TO_EXISTING_TRANSACTION
  (t_id INT(11),
  p_id INT(11),
  p_count INT(11))
  BEGIN
    INSERT INTO Purchases
        (Transaction_ID, Product_ID, count)
    VALUES
           (t_id, p_id, p_count);
  end;

CREATE FUNCTION ADD_PURCHASE
  (sender_user INT(11),
  recipient_user INT(11),
  p_id INT(11),
  p_count INT(11))
  RETURNS INT(11)
  BEGIN
    DECLARE trans_id INT(11);
    INSERT INTO Transactions
        (Date, sender, recipient)
    VALUES
           (CURDATE(), sender_user, recipient_user);
    SET trans_id = LAST_INSERT_ID();
    INSERT INTO Purchases
        (Transaction_ID, Product_ID, count)
    VALUES
           (trans_id, p_id, p_count);
    RETURN trans_id;
  end;