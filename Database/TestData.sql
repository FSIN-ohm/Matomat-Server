USE matohmat;

INSERT INTO Users
    (ID, auth_hash, start_balance, LastSeen, available)
VALUES  (1, 'dagobert', 34.56, '2010-12-18 13:17:17', 1),
        (2, 'asdf', 34.56, '2011-12-18 13:17:17', 1),
        (3, 'himler', 34.56, '2011-12-18 13:17:17', 1),
        (4, 'peter', 34.56, '2011-12-18 13:17:17', 0),
        (5, 'fdsa', 60.20, '2016-02-16 14:00:42', 1);

INSERT INTO Admins
    (ID, username, password, email, password_salt, User_ID)
VALUES
       (1, 'himler', 'christopf', 'himler@finanzen.urg', 'aasdfasfd', 3);

INSERT INTO Products
    (ID, name, price, image_url, reorder_point, available, product_hash)
VALUES
       (1, 'Mate', '0.70', 'https://matebild.com/bild.jpg', 50, 1, 'aksdjfkj'),
       (2, 'Snickers', '0.20', 'https://snickersbild.com/bild.jpg', 20, 1, 'laksdjfje'),
       (3, 'Mars', '0.20', 'https://marsbild.com/bild.jpg', 50, 0, 'fi3jjf'),
       (4, 'Wasser', '0.50', 'https://lolbild.com/bild.jpg', 70, 1, 'eieiiej');


INSERT INTO Transactions
    (ID, Date, sender, recipient)
VALUES
       (1, '2013-12-18 05:17:17', 1, 3),
       (2, '2014-12-18 05:17:17', 2, 3),
       (3, '2014-12-18 14:17:17', 4, 4),
       (4, '2014-12-18 15:17:17', 1, 4),
       (5, '2014-12-18 18:17:17', 4, 1),
       (6, '2014-12-18 19:17:17', 2, 3);

INSERT INTO Transfers
    (transaction_ID, charged_amount)
VALUES
       (4, 10.00),
       (5, 15.00),
       (6, 12.00);

INSERT INTO transaction_amount_products
    (Transaction_ID, Product_ID, count)
VALUES
       (1, 1, 50),
       (1, 2, 60),
       (1, 3, 100),
       (1, 4, 20),
       (2, 1, 2),
       (2, 2, 1),
       (3, 3, 1),
       (3, 4, 4);