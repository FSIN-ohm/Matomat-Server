CALL user_create(SHA1('world'));
CALL user_create(SHA1('dagobert'));
CALL admin_create("Admin", "fachsachft-in@th-nuernberg.de", sha1( concat("keinpasswort", sha1("keinsalt") ), sha1("keinsalt") ) );