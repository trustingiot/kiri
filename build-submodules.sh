#!/bin/sh

if [ ! -e tools/.gitignore ]; then
  git submodule init
  git submodule update
fi

