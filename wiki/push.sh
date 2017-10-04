#!/usr/bin/env bash

# Commit changes and update remote
cd dungeon.wiki
git add *.md
git commit -am "Regenerated the Wiki"
git push origin

# Self-destruct
cd ..
rm -rf dungeon.wiki
