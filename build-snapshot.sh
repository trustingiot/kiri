#!/bin/sh

sh build-submodules.sh

cd tools
if [ ! -e target/*-jar-with-dependencies.jar ]; then
  mvn package
fi

java -jar target/*-jar-with-dependencies.jar SnapshotBuilder

cd ..
mv tools/Snapshot* org.eclipse.kura.kiri.iri/resources/base
