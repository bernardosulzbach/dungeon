Contributing
============

This file contains some basic directives for developers interested in this project.


Writing code
------------
Our code style is a slightly modified version of Google Java Style.

The fork is called [Dungeon Java Style]
(http://htmlpreview.github.io/?https://github.com/mafagafogigante/dungeon/blob/master/misc/style/dungeon_java_style.html).

There is a Code Style exported from IDEA 14.0.2 in the ``misc`` directory.
Download it and import it into the IDE to get proper Code Reformatting.

Writing GUI code
----------------
Do not use a generator, write the code yourself.

Readability, performance and simplicity (of code and of interface) are our main goals.


Writing resource files
----------------------
```
All entries have the following syntax:
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
integers:  n | Integer.MIN_VALUE <= n <= Integer.MAX_VALUE
doubles:   n | Double.MIN_VALUE  <= n <= Double.MAX_VALUE
strings:   Plain text.
```

* IDs and KEYs are always UPPERCASE_WITH_UNDERSCORES.
* Do not surround strings with quotation marks.
* Do not use TRUE or FALSE for booleans.
* Multiple line strings are not yet supported. Feel free to implement it.


Commits and pull requests
-------------------------
* Do not change code unless there is a reason to do it. Refactorings are a valid reason.
* If you are not sure about how to implement something, discuss it on the issue tracker on GitHub.
* Do not revert changes before discussing it with the other developers.
* Do not use any license other than GNU GPLv3.
