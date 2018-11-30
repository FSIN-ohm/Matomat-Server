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
  SELECT U.ID,
         SUM(IFNULL(Tr.charged_amount, 0) + IFNULL(cfp.charged_amount, 0)) AS expense
    FROM Users U
      LEFT JOIN Transactions T on U.ID = T.sender
      LEFT JOIN Transfers Tr ON T.ID = Tr.transaction_ID
      LEFT JOIN charges_for_purchases cfp on T.ID = cfp.ID
        GROUP BY U.ID;

DROP VIEW IF EXISTS users_income;
CREATE VIEW users_income AS
  SELECT U.ID,
         SUM(IFNULL(Tr.charged_amount, 0) + IFNULL(cfp.charged_amount, 0)) AS income
      FROM Users U
        LEFT JOIN Transactions T on U.ID = T.recipient
        LEFT JOIN Transfers Tr ON T.ID = Tr.transaction_ID
        LEFT JOIN charges_for_purchases cfp on T.ID = cfp.ID
          GROUP BY U.ID;

DROP VIEW IF EXISTS users_balance;
CREATE VIEW users_balance AS
  SELECT iou.ID,
         iou.income - eou.expense AS balance
    FROM income_of_users iou
      JOIN expenses_of_users eou ON iou.ID = eou.ID;