#!/bin/bash

rm -r smartcampusapi

#download source code
git clone https://github.com/it4energy/smartcampusapi.git
cd smartcampusapi

git checkout origin/master

#download apache felix
wget https://dl.dropboxusercontent.com/u/14813041/felix/felix-framework-4.4.0.zip
unzip felix-framework-4.4.0.zip


mvn clean
#install
mvn install
#generate bundles dependencies
mvn -Pcreate-osgi-bundles-from-dependencies bundle:wrap

#copy bundles to felix framework bundle folder
find . -iname *.jar -exec cp {} felix-framework-4.4.0/bundle/ \;

cd felix-framework-4.4.0

#run apache felix
java -jar bin/felix.jar 



