Contributing
============

This file contains some basic directives for developers interested in this project.


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


Code style
----------
* Four spaces instead of tabs.
* 120 characters per line. Feel free to stick to 80 or 100 or whatever you like, as long as it is not bigger than 120.
* Final variables and enumeration constants should be UPPERCASE_WITH_UNDERSCORES.


Commits and pull requests
-------------------------
* Do not change code unless there is a reason to do it. Refactorings are a valid reason.
* If you are not sure about how to implement something, discuss it on the issue tracker on GitHub.
* Do not revert changes before discussing it with the other developers.
* Do not use any license other than GNU GPLv3.
