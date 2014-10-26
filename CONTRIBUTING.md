Contributing
============

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
component: COMPONENT_ID
booleans:  0 or 1
integers:  n | Integer.MIN_VALUE <= n <= Integer.MAX_VALUE
doubles:   n | Double.MIN_VALUE  <= n <= Double.MAX_VALUE
strings:   Plain text.
null:      (nothing)
```

IDs and KEYs are **ALWAYS** UPPERCASE_WITH_UNDERSCORES.
Do not surround strings with quotation marks.
Do not use TRUE or FALSE for booleans.
Multiple line strings are NOT YET supported. Feel free to implement it.


Code Style
----------
* Four spaces instead of tabs.
* 120 characters per line.
* Final variables and enumeration constants should be UPPERCASE_WITH_UNDERSCORES.