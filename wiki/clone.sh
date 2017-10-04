#!/usr/bin/env bash

# Delete the wiki clone if it is there (shouldn't be)
rm -rf dungeon.wiki

# Clone the wiki
git clone git@github.com:mafagafogigante/dungeon.wiki.git
