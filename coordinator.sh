#!/bin/sh

if [ $# -ne 2 ]; then
  echo "Call: $0 <host> <port>"
  exit 0
fi

if [ ! -e tools/.gitignore ]; then
  git submodule init
  git submodule update
fi

cd tools
if [ ! -e target/*-jar-with-dependencies.jar ]; then
  mvn package
fi

java -jar target/*-jar-with-dependencies.jar Coordinator $1 $2
