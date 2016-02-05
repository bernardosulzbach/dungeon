#!/usr/bin/env bash

# Delete the wiki clone if it is there (shouldn't be)
rm -rf dungeon.wiki

# Clone the wiki
git clone git@github.com:mafagafogigante/dungeon.wiki.git

# Run the converter
python3 wiki/wiki.py

# Commit changes and update remote
cd dungeon.wiki
git add *.md
git commit -am "Regenerated the Wiki"
git push origin

# Self-destruct
cd ..
rm -rf dungeon.wiki
