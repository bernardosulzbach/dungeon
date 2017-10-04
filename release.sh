#!/usr/bin/env bash
if [ $(whoami) != "mg" ]
    then
        echo "This should only be ran by Bernardo Sulzbach."
        echo "Even if you skip this check, it will not work."
        exit 1
fi
if [ $# -ne 2 ]
    then
        echo "Pass two arguments: the new tag and a short release message."
    else
        old_tag=$(git describe --abbrev=0)
        sed -i "s/$old_tag/$1/g" pom.xml
        git commit -a -m "$(printf "Release $1\n\n$2")"
        git tag -a $1 -m "$2"
    # git push origin --set-upstream HEAD
    #   A handy way to push the current branch to the same name on the
    #   remote.
    # --follow-tags
    #   Also push annotated tags in refs/tags that are missing from the
    #   remote but are pointing at commit-ish that are reachable from the
    #   refs being pushed
    echo "Pushing the new tag..."
    git push origin HEAD --follow-tags
    echo "Updating the GitHub wiki..."
    bash wiki/wiki.sh
fi
