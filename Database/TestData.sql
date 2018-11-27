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
    (ID, name, price, stock, image_url, reorder_point, available)
VALUES
       (1, 'Mate', '0.70', 200, 'https://matebild.com/bild.jpg', 50, 1),
       (2, 'Snickers', '0.20', 300, 'https://snickersbild.com/bild.jpg', 20, 1),
       (3, 'Mars', '0.20', 0, 'https://marsbild.com/bild.jpg', 50, 0),
       (4, 'Wasser', '0.50', 130, 'https://lolbild.com/bild.jpg', 70, 1);

