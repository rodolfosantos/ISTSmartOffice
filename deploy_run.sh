#!/bin/bash
git checkout origin/master

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
