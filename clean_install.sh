#!/bin/bash

#install
mvn install
#generate bundles dependencies
mvn -Pcreate-osgi-bundles-from-dependencies bundle:wrap

#copy bundles - put felix framwork in your home
find . -iname *.jar -exec cp {} ~/Documents/workspace_smartcampus/Felix/bundle/ \;

