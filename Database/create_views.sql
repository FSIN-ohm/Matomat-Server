USE matohmat;

-- CREATE PLACEHOLDERS
DROP VIEW IF EXISTS user_balance;
CREATE TABLE IF NOT EXISTS `matohmat`.`user_balance` (`id` INT, `last_seen` INT, `balance` INT, `available` INT, `name` INT);


DROP VIEW IF EXISTS admin_balance;
CREATE VIEW admin_balance AS
       SELECT a.id,
              a.username,
              a.email,
              a.password,
              a.password_salt,
              ub.balance,
              ub.last_seen,
              ub.available
       FROM admins AS a
       JOIN user_balance ub ON a.id = ub.id
  ORDER BY a.id;

DROP VIEW IF EXISTS transfer_detail;
CREATE VIEW `transfer_detail` AS
SELECT transactions.id,
		date,
        sender,
        recipient,
        amount,
       (CASE
          WHEN sender = 1 THEN 'deposit'
          WHEN recipient = 1 THEN 'withdraw'
          ELSE 'transfer'
        END) AS type
FROM matohmat.transactions
JOIN transfers ON transfers.transaction_id = transactions.id;

DROP VIEW IF EXISTS purchase_total;
CREATE VIEW `purchase_total` AS
  SELECT tr.id,
         date,
         sender,
         recipient,
         amount,
         'purchase' as type
  FROM transactions AS tr
  JOIN (SELECT t.id,
           SUM(count * price) AS amount
    FROM transactions AS t
    JOIN purchase_amount_products pap ON t.id = pap.transaction_id
    JOIN product_prices p ON pap.prices_id = p.id
    GROUP BY t.id) ta ON tr.id = ta.id;

DROP VIEW IF EXISTS order_total;
CREATE VIEW `order_total` AS
  SELECT tr.id,
         date,
         sender,
         recipient,
         buy_cost  AS amount,
         o.admin_id,
         'order' AS type
  FROM transactions AS tr
  JOIN orders o on tr.id = o.transaction_id
  JOIN ordered_products op ON tr.id = op.order_transaction_id;

DROP VIEW IF EXISTS products_stock;
CREATE VIEW products_stock AS
  SELECT products.id, IFNULL(bought.amount_bought, 0) - IFNULL(sold.products_sold, 0) AS stock
  FROM products
  LEFT JOIN
    (SELECT  product_id, SUM(count) AS amount_bought
    FROM ordered_products
    GROUP BY  product_id) AS bought ON bought. product_id = products.id
  LEFT JOIN
      (SELECT pd.id AS  product_id, SUM(count) AS products_sold FROM purchase_amount_products
    JOIN product_prices p ON purchase_amount_products.prices_id = p.id
    JOIN products pd ON p.product_id = pd.id
    GROUP BY pd.id) AS sold ON sold. product_id = products.id;

DROP VIEW IF EXISTS products_all;
CREATE VIEW `products_all` AS
  SELECT
      products.id,
      prices.price,
      products.name AS name,
      products.reorder_point AS reorder_point,
      image_url,
      available,
      items_per_crate,
      barcode,
      valp.valid_date,
      products_stock.stock
  FROM products
  JOIN
      (SELECT product_id, MAX(valid_from) AS valid_date
        FROM product_prices
        GROUP BY  product_id) AS valp ON valp.product_id = products.id
  JOIN product_prices AS prices ON prices.product_id = products.id
  JOIN products_stock on products_stock.id = products.id
  ORDER BY products.id;

DROP VIEW IF EXISTS transactions_total;
CREATE VIEW `transactions_total` AS
  SELECT
      id, date, sender, recipient, amount, type
  FROM
      transfer_detail
  UNION ALL SELECT
      id, date, sender, recipient, amount, type
    FROM
      purchase_total
  UNION ALL SELECT
      id, date, sender, recipient, amount, type
    FROM order_total
  ORDER BY id;

DROP TABLE IF EXISTS `matohmat`.`user_balance`;
CREATE OR REPLACE VIEW `user_balance` AS
  SELECT
      id, last_seen, (balance + IFNULL(in_amount, 0) - IFNULL(out_amount,0) ) AS balance, available, name
  FROM users
  LEFT JOIN
      (SELECT
          sender, SUM(amount) AS out_amount
      FROM
          transactions_total
      GROUP BY sender) as in_transactions ON sender = users.id
  LEFT JOIN
      (SELECT
          recipient, SUM(amount) AS in_amount
      FROM
          transactions_total
      GROUP BY recipient) as out_transactions ON recipient = users.id
  ORDER BY id;
