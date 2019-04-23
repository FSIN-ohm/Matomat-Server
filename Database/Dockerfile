FROM mysql

COPY CreateDatabase.sql /docker-entrypoint-initdb.d/init_01.sql
COPY create_views.sql /docker-entrypoint-initdb.d/init_02.sql 
COPY create_routines.sql /docker-entrypoint-initdb.d/init_03.sql 
