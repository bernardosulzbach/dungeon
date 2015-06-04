Contributing
============

This file contains some basic directives for developers interested in this project.


Commits and PRs
---------------
* Do not change code unless there is a reason to do it. Refactorings are a valid reason.
* If you are not sure about how to implement something, discuss it on the issue tracker on GitHub.
* Do not revert changes before discussing it with the other developers.
* Do not use any license other than GNU GPLv3.


Writing code
------------
Readability, simplicity and performance (*in this order*) are our main goals.

**"Strive to write a good program rather than a fast one."** (adapted from Effective Java 2nd Edition).


Code Style
----------
Our code style is a slightly modified version of Google Java Style called [Dungeon Java Style]
(http://htmlpreview.github.io/?https://github.com/mafagafogigante/dungeon/blob/master/misc/style/dungeon_java_style.html).

There is a Code Style exported from IDEA 14.0.2 in the ``misc`` directory. IDEA 14.1.2 works fine with it.
Download it and import it into the IDE to get proper Code Reformatting.
*Note that this is not guaranteed to be 100% style-compliant, you may need to fix some things yourself.*


**Code comments**

You may find several classes, interfaces and methods with complete Javadoc.
This is no longer required. Clear and concise implementation comments are enough.
Note that these comments may follow the Javadoc rules, but don't need to.

Do not write jokes on source code files.

Do not pollute comments with unnecessary data.

Writing GUI code
----------------
Do not use a generator, write the code yourself.


Writing resource files
----------------------
The entry format is
```
KEY: VALUE
```

The key naming convention is

```
UPPERCASE_WITH_UNDERSCORES: value
```

Values should take the following forms:

```
IDs:       ID_OF_OBJECT
booleans:  0 or 1
integers:  n : Integer.MIN_VALUE <= n <= Integer.MAX_VALUE
doubles:   n : Double.MIN_VALUE  <= n <= Double.MAX_VALUE
strings:   Plain text.
```

* IDs and KEYs are always UPPERCASE_WITH_UNDERSCORES.
* Do not surround strings with quotation marks.
* Do not use TRUE or FALSE for booleans.

Strings of text can span multiple lines with the usage of line breaks. This is a table with all the valid line breaks.

|Line break|Replaced by|
|----------|-----------|
|``\``     |`` ``¹     |
|``\\``²   |``\n``     |

¹ - A single whitespace.  
² - Two or more backslashes.

An arbitrary amount of whitespaces may precede the line break or the first non-whitespace character of the line, the
parser ignores all of them.


**Resource comments**

Comments may be written by preceding the line with ``//``.
Note that this does not mean 'ignore until the end of the line' as it would in C, C++, or Java. Instead, it signifies
to the parser that the whole line should be ignored and it only works when the slashes are the first non-whitespace
characters of the line.


**Important**

An element (an object in a resource file) begins when the first key read from that resource file is read again.

For instance,
```
NAME: John
AGE: 23

NAME: Lucas
AGE: 22
HEIGHT: 5' 11"

NAME: Third Element
// ...
```

Generates multiple elements, delimited by the key ``NAME``.
Not all elements will have the same attributes, the only guarantee is that all elements will have a ``NAME`` field.
