USE matohmat;

INSERT INTO Users
    (ID, auth_hash, start_balance, LastSeen, available)
VALUES  (1, 'dagobert', 34.56, '2010-12-18 13:17:17', 1),
        (2, 'asdf', 34.56, '2011-12-18 13:17:17', 1),
        (3, 'himler', 34.56, '2011-12-18 13:17:17', 1),
        (4, 'peter', 34.56, '2011-12-18 13:17:17', 0),
        (5, 'fdsa', 60.20, '2016-02-16 14:00:42', 1),
        (6, 'Mr. Fresh', 0.00, '2016-02-16 14:00:42', 1);

INSERT INTO Admins
    (ID, username, password, email, password_salt, User_ID)
VALUES
       (1, 'himler', 'christopf', 'himler@finanzen.urg', 'aasdfasfd', 3);

INSERT INTO Products
    (ID, price)
VALUES
       (1, '0.70'),
       (2, '0.20'),
       (3, '0.20'),
       (4, '0.50');

INSERT INTO Product_infos
    (product_ID, name, image_url, reorder_point, product_hash, available)
VALUES
       (1, 'Mate', 'https://matebild.com/bild.jpg', 50, 'aksdjfkj', 1),
       (2, 'Snickers', 'https://snickersbild.com/bild.jpg', 20, 'laksdjfje', 1),
       (3, 'Mars', 'https://marsbild.com/bild.jpg', 50, 'fi3jjf', 0),
       (4, 'Wasser', 'https://lolbild.com/bild.jpg', 70, 'eieiiej', 1);

INSERT INTO Transactions
    (ID, Date, sender, recipient)
VALUES
       (1, '2013-12-18 05:17:17', 1, 3),
       (2, '2014-12-18 05:17:17', 2, 1),
       (3, '2014-12-18 14:17:17', 4, 1),
       (4, '2014-12-18 15:16:17', 4, 1),
       (5, '2014-12-18 15:17:17', 1, 4),
       (6, '2014-12-18 18:17:17', 3, 1),
       (7, '2014-12-18 19:17:17', 5, 4),
       (8, '2014-12-19 19:17:17', 5, 4);

INSERT INTO Transfers
    (transaction_ID, charged_amount)
VALUES
       (5, 10.00),
       (6, 15.00),
       (7, 12.00),
       (8, 4.00);

INSERT INTO Purchases
    (Transaction_ID, Product_ID, count)
VALUES
       (1, 1, 50),
       (1, 2, 60),
       (1, 3, 100),
       (1, 4, 20),
       (2, 1, 2),
       (2, 2, 1),
       (3, 3, 1),
       (3, 4, 4),
       (4, 2, 5);