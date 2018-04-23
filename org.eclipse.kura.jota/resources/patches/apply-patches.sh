#!/bin/sh

if [ "$#" -ne 1 ] || ! [ -d "$1" ]; then
	echo "Usage: $0 <path> " >&2
	exit 1
fi

if [ -d src ]; then
	rm -rf src
fi

mkdir src
cp -R $1/jota src/jota
cp -R $1/cfb src/cfb

for f in *.diff; do patch -p0 < $f; done
