if [ $# -ne 2 ]
    then
        echo "Pass two arguments: the new tag and a short release message."
    else
        old_tag=$(git describe --abbrev=0)
        sed -i "s/$old_tag/$1/g" README.md pom.xml
        git commit -a -m "Release $1."
        git tag -a $1 -m "$2"
        echo "Check if everything is OK and if it is push to the remote."
fi
