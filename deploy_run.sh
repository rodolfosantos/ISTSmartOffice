#!/bin/bash

#remove previews folder
sudo rm -r smartcampusapi

#download source code
git clone https://github.com/it4energy/smartcampusapi.git
cd smartcampusapi

git checkout origin/temporary

#download apache felix
wget http://mirrors.fe.up.pt/pub/apache//felix/org.apache.felix.main.distribution-4.4.0.zip
unzip org.apache.felix.main.distribution-4.4.0.zip

#clean
mvn clean
#install
mvn install
#generate bundles dependencies
mvn -Pcreate-osgi-bundles-from-dependencies bundle:wrap

#copy bundles to felix framework bundle folder
find . -iname *.jar -exec cp {} felix-framework-4.4.0/bundle/ \;

cd felix-framework-4.4.0

#run apache felix
java -Dgosh.args=--nointeractive -jar bin/felix.jar
