#!/bin/sh

mvn clean
sh build-snapshot.sh
mvn package
cp org.eclipse.kura.kiri.feature.armv7a/target/*.dp .
cp org.eclipse.kura.kiri.feature.jni/target/*.dp .
