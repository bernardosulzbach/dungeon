# Replaces occurrences of a value by another value in README.md and pom.xml.
if [ $# -ne 2 ]
    then
        echo 'Pass two arguments: [old-text] [new-text]'
else
    sed -i 's/'$1'/'$2'/g' README.md pom.xml
fi
