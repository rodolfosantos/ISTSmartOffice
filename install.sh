#!/bin/bash

#install
mvn install

#copy bundles - put felix framwork in your home
find . -iname *.jar -exec cp {} ~/Documents/workspace_smartcampus/Felix/bundle/ \;
