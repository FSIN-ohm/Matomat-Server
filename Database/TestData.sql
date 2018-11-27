USE matohmat;

INSERT INTO Users
    (ID, auth_hash, Balance, LastSeen, available)
VALUES  (1, 'asdf', 34.56, '2011-12-18 13:17:17', 1),
        (2, 'himler', 34.56, '2011-12-18 13:17:17', 1),
        (3, 'peter', 34.56, '2011-12-18 13:17:17', 0),
        (4, 'fdsa', 60.20, '2016-02-16 14:00:42', 1);

INSERT INTO Admins
    (ID, username, password, email, password_salt, User_ID)
VALUES
       (1, 'himler', 'christopf', 'himler@finanzen.urg', 'aasdfasfd', 2);

INSERT INTO Products
    (ID, name, price, stock, image_url, reorder_point, available, product_hash)
VALUES
       (1, 'Mate', '0.70', 200, 'https://matebild.com/bild.jpg', 50, 1, 'aksdjfkj'),
       (2, 'Snickers', '0.20', 300, 'https://snickersbild.com/bild.jpg', 20, 1, 'laksdjfje'),
       (3, 'Mars', '0.20', 0, 'https://marsbild.com/bild.jpg', 50, 0, 'fi3jjf'),
       (4, 'Wasser', '0.50', 130, 'https://lolbild.com/bild.jpg', 70, 1, 'eieiiej');


INSERT INTO Transactions
    (ID, Date, Type, charged_amount, sender, recipient)
VALUES
       (1, '2014-12-18 05:17:17', 'T', 0, 1, 2),
       (2, '2014-12-18 14:17:17', 'T', 0, 3, 2),
       (3, '2014-12-18 15:17:17', 'I', 10.00, 4, 2),
       (4, '2014-12-18 18:17:17', 'O', 10.00, 3, 2);

INSERT INTO transaction_amount_products
    (Transactions_ID, Products_ID, count)
VALUES
       (1, 1, 2),
       (1, 2, 1),
       (2, 3, 1),
       (2, 4, 4);