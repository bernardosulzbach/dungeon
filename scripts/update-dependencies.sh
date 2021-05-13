#!/usr/bin/env bash

DIRECTORY="${BASH_SOURCE%/*}"
if [[ ! -d "$DIRECTORY" ]]; then DIRECTORY="$PWD"; fi
. "$DIRECTORY/include.sh"

mvn versions:use-latest-releases
