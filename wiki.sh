#!/usr/bin/env bash

python3 wiki/wiki.py
cd dungeon.wiki
git commit -am "Regenerated the Wiki"
git push origin
