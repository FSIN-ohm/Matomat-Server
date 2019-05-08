#!/bin/bash

./gradlew assemble
cd build
rm -fr zip
mkdir zip
cp libs/* zip
cp -r ../Database zip
rm zip/Database/*.mwb
rm zip/Database/*.bak
tar -cvzf matohmat-server.tar.gz zip
