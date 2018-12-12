# Generate test data while testing procedures

CALL `matohmat`.`user_create`(sha1('testuser1'));
CALL `matohmat`.`admin_create`('testadmin1', 'testadmin@example.com', sha1('passwort123456'), sha1('schlechtersalt') );
CALL `matohmat`.`product_add`('Bier', 0.89, 'localhost/img/bier', 20);
CALL `matohmat`.`product_update_price`(1, 1.10);
CALL `matohmat`.`transaction_deposit`(3, 10.0);
CALL `matohmat`.`transaction_withdraw`(3, 2.0);
CALL `matohmat`.`transaction_purchase`(3, 1, 2);

select * from products_current;
select * from purchase_detail;
select * from purchase_total;
SELECT * from transactions_total;
SELECT * from transfer_detail;
select * from user_balance;