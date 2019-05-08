#!/bin/bash

CURDIR=$PWD
rm -fr /tmp/matohmat_db
mkdir /tmp/matohmat_db
cp Database/*.sql /tmp/matohmat_db
cd /tmp
tar -czvf matohmat-db.tar.gz matohmat_db
rm -fr /tmp/maatohmat_db
cp matohmat-db.tar.gz $CURDIR
