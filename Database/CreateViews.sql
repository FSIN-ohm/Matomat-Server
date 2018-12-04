USE matohmat;

DROP VIEW IF EXISTS charges_for_purchases;
CREATE VIEW charges_for_purchases AS
  SELECT P.Transaction_ID as ID,
         SUM(Pr.price * P.count) AS charged_amount
    FROM Purchases AS P
    JOIN Products Pr on P.Product_ID = Pr.ID
      GROUP BY P.Transaction_ID;

DROP VIEW IF EXISTS products_per_transaction;
CREATE VIEW products_per_transaction AS
  SELECT *, Pr.price * P.count AS charged from Purchases AS P
    JOIN Products Pr on P.Product_ID = Pr.ID;

DROP VIEW IF EXISTS purchase_transactions;
CREATE VIEW purchase_transactions AS
  SELECT DISTINCT T.ID,
                  T.recipient,
                  T.sender,
                  T.Date
    FROM Transactions AS T
    JOIN Purchases P on T.ID = P.Transaction_ID;

DROP VIEW IF EXISTS transfer_transactions;
CREATE VIEW transfer_transactions AS
  SELECT ID,
         Date,
         sender,
         recipient,
         charged_amount
  FROM Transactions AS T
    JOIN Transfers Tr on T.ID = Tr.transaction_ID;

DROP VIEW IF EXISTS users_expense;
CREATE VIEW users_expense AS
  SELECT T.sender AS user_id,
         SUM(IFNULL(Tr.charged_amount, 0) + IFNULL(cfp.charged_amount, 0)) AS expense
    FROM Transactions T
      LEFT JOIN Transfers Tr ON T.ID = Tr.transaction_ID
      LEFT JOIN charges_for_purchases cfp on T.ID = cfp.ID
        GROUP BY T.sender;

DROP VIEW IF EXISTS users_income;
CREATE VIEW users_income AS
  SELECT T.recipient AS user_id,
         SUM(IFNULL(Tr.charged_amount, 0) + IFNULL(cfp.charged_amount, 0)) AS income
      FROM Transactions T
        LEFT JOIN Transfers Tr ON T.ID = Tr.transaction_ID
        LEFT JOIN charges_for_purchases cfp on T.ID = cfp.ID
          GROUP BY T.recipient;

DROP VIEW IF EXISTS users_balance;
CREATE VIEW users_balance AS
  SELECT U.ID,
         IFNULL(ui.income, 0) - IFNULL(ue.expense, 0) AS balance
    FROM Users U
        LEFT JOIN users_income ui ON U.ID = ui.user_id
        LEFT JOIN users_expense ue ON U.ID = ue.user_id;


DROP VIEW if exists bought_products;
CREATE VIEW bought_products AS
  SELECT T.sender as user_id,
         P.Product_ID as product_id,
         SUM(P.count) as bought_products
  From Transactions T
  JOIN Purchases P on T.ID = P.Transaction_ID
  GROUP BY P.Product_ID, T.sender;

DROP VIEW if exists sold_products;
CREATE VIEW sold_products AS
  SELECT T.recipient as user_id,
         P.Product_ID as product_id,
         SUM(P.count) as sold_products
  From Transactions T
  JOIN Purchases P on T.ID = P.Transaction_ID
  GROUP BY P.Product_ID, T.recipient;

DROP VIEW IF EXISTS product_stocks;
CREATE VIEW product_stocks AS
  SELECT U.ID as user_id,
         IFNULL(bp.product_id, sp.product_id) as product_id,
         IFNULL(bp.bought_products, 0) - IFNULL(sp.sold_products, 0) as stock
    FROM Users U
    LEFT JOIN bought_products bp ON bp.user_id = U.ID
    LEFT JOIN sold_products sp ON bp.user_id = sp.user_id AND sp.product_id = bp.product_id
      WHERE NOT ISNULL(IFNULL(bp.product_id, sp.product_id))
        AND IFNULL(bp.bought_products, 0) - IFNULL(sp.sold_products, 0) > 0;

DROP VIEW IF EXISTS virtual_product;
CREATE VIEW virtual_product AS
  SELECT P.ID,
         price,
         name,
         image_url,
         reorder_point,
         product_hash
  FROM Products P
  JOIN Product_infos info on P.Product_info_ID = info.ID
  WHERE info.available = 1;