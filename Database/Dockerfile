FROM mariadb

COPY CreateDatabase.sql /docker-entrypoint-initdb.d/init_01.sql
COPY CreateViews.sql /docker-entrypoint-initdb.d/init_02.sql
COPY TestData.sql /docker-entrypoint-initdb.d/init_03.sql
