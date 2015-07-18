# Dungeon Java Style

This is the complete definition of Dungeon's coding standards.
These standards have been highly influenced by Google Java Style, but are more specific to this project.

## Terminology

The term class is used inclusively to mean an "ordinary" class, enum class, interface or annotation.

The term comment always refers to implementation comments. "Documentation comments" are called "Javadoc."

# Source file basics

## File name 

The source file name consists of the case-sensitive name of the top-level class it contains, plus the `.java` extension.

## File encoding

Source files are encoded in **UTF-8**.

## Special characters

### Whitespace characters

Aside from the line terminator sequence, the ASCII horizontal space character (`0x20`) is the only whitespace character
that appears anywhere in a source file. This implies that all other whitespace characters in string and character
literals are escaped.

### Special escape sequences

For any character that has a special escape sequence (`\b`, `\t`, `\n`, `\f`, `\r`, `\"`, `\'` and `\\`), that sequence
is used rather than the corresponding octal (e.g. `\012`) or Unicode (e.g. `\u000a`) escape.

### Non-ASCII characters

For the remaining non-ASCII characters, either use the actual Unicode character or the equivalent Unicode escape,
depending on which makes the code easier to read and understand.

# Source file structure

A source file consists of, in order:
+ License information.
+ Package statement.
+ Import statements.
+ **Exactly one** top-level class.

Exactly one blank line separates each section that is present.

## The package statement

The package statement is never line-wrapped.

## The import statements

The import statements are never line-wrapped.

Separated in blocks in this order:
+ All static imports
+ `org.dungeon` imports
+ Third-party imports
+ `java` imports
+ `javax` imports

Within a group there are no blank lines, and the imported names appear in ASCII sort order.

## Class declaration

### Class member ordering 

The ordering of the members of a class can have a great effect on learnability, therefore one should strive for a
logical ordering of the class members.

When a class has multiple constructors, or multiple methods with the same name, these appear sequentially, with no
intervening members.

# Formatting

## Braces

Braces are used where optional (`if`, `else`, `do`, `for`, `while`, ...).

Braces follow the Kernighan and Ritchie style ("Egyptian brackets") for nonempty blocks and block-like constructs:

+ No line break before the opening brace.
+ Line break after the opening brace.
+ Line break before the closing brace.
+ Line break after the closing brace if that brace terminates a statement or the body of a method, constructor or named
  class.
For example, there is no line break after the brace if it is followed by else or a comma.

Empty blocks should **not** be concise.

    void doNothing() {}; // Wrong!
    
## Block indentation: +2 spaces 

Each time a new block or block-like construct is opened, the indent increases by two spaces.

## One statement per line 

Each statement is followed by a line-break.

    String name = "John"; int age = 42; // Wrong!

## Column limit: 120 characters

Any line that needs to exceed this limit must be line-wrapped. Exceptions:

+ Lines where obeying the column limit is not possible (for example, a long URL in Javadoc).
+ `package` and `import` statements.

## Line-wrapping: +4 spaces

Extract methods or local variables to **avoid** line-wrapping whenever possible.

*Line-wrapping is a last resort*.

Each line after the first is indented four spaces from the original line.

    problemFactory.createNewProblem("math", // Suppose we have to line-wrap here.
        4, 6); // 4 spaces as abovementioned.

Dungeon has **no other rules** on how to line-wrap. Aim for readability and you should do fine.

## Whitespace

### Vertical Whitespace 

A single blank line appears:

+ Between consecutive members (or initializers) of a class: fields, constructors, methods, nested classes, static
  initializers, instance initializers. *Exception: A blank line between two consecutive fields (having no other code
  between them) is optional. Such blank lines are used as needed to create logical groupings of fields*.
+ Within method bodies, as needed to create logical groupings of statements.
+ Optionally before the first member or after the last member of the class.
+ As required by other sections of this document (such as `import` statements).

Multiple consecutive blank lines are **not** permitted.

### Horizontal whitespace 

Beyond where required by the language or other style rules, and apart from literals, comments and Javadoc, a single
ASCII space also appears in the following places only.

+ Separating any reserved word, such as if, for or catch, from an open parenthesis (`(`) that follows it on that line
+ Separating any reserved word, such as `else` or `catch`, from a closing curly brace (`}`) that precedes it on that
  line
+ Before any open curly brace (`{`), with two exceptions:
  + `@SomeAnnotation({a, b})`
  + `String[][] x = {{"foo"}};`
+ On both sides of any binary or ternary operator. This also applies to the following "operator-like" symbols:
  + the ampersand in a conjunctive type bound: `<T extends Foo & Bar>`
  + the pipe for a catch block that handles multiple exceptions: `catch (FooException | BarException e)`
  + the colon (`:`) in an enhanced for ("foreach") statement
+ After `,:;` or the closing parenthesis (`)`) of a cast.
+ On both sides of the double slash (`//`) that begins an end-of-line comment.
+ Between the type and variable of a declaration: `List<String> list`.

### Horizontal alignment

Horizontal alignment is the practice of adding a variable number of additional spaces in your code with the goal of
making certain tokens appear directly below certain other tokens on previous lines. **Do not waste time doing this**.

## Enum classes

After each comma that follows an enum constant, a line-break is optional.

An enum class with no methods and no documentation on its constants may optionally be formatted as if it were an array
initializer.

    private enum Suit { CLUBS, HEARTS, SPADES, DIAMONDS }

Since enum classes are classes, all other rules for formatting classes apply.

## Variable declarations

+ One variable per declaration.
+ Declared when needed, initialized as soon as possible.

## Arrays

### No C-style array declaration

The square brackets form a part of the type, not the variable:
`String[] args`, not `String args[]`.

## Switch statements: forbidden

They are unnecessary, more likely to cause subtle bugs than to provide an elegant alternative to `if-else` blocks, that
never get too big as we have proper code quality standards and common sense.

If an `if-else` block is too big, rewriting it in an object-oriented manner should eliminate it completely.

## Annotations

Annotations applying to a class, method or constructor appear immediately after the documentation block, and each
annotation is listed on a line of its own (that is, one annotation per line).

## Block comment style 

Block comments are indented at the same level as the surrounding code. For multi-line `/* ... */` comments, subsequent
lines must start with `*` aligned with the `*` on the previous line.

## Modifiers 

Class and member modifiers, when present, appear in the order recommended by the Java Language Specification:

`public protected private abstract static final transient volatile synchronized native strictfp`

## Numeric Literals 

`long` integer literals use an **uppercase** `L` suffix.
For example, `3000000000L` rather than `3000000000l`.

# Naming

## Rules common to all identifiers 

Identifiers use only ASCII letters and digits, and in two cases noted below, underscores.

## Package names 

Package names are all lowercase, with consecutive words simply concatenated together (no underscores). For example,
`com.example.deepspace`, not `com.example.deepSpace` or `com.example.deep_space`.

## Class names 

Class names are written in UpperCamelCase.

Class names are typically nouns or noun phrases. For example, `Character` or `ImmutableList`. Interface names may also
be nouns or noun phrases (for example, `List`), but may sometimes be adjectives or adjective phrases instead (for
example, `Readable`).

Test classes are named starting with the name of the class they are testing, and ending with Test. For example,
`HashTest` or `HashIntegrationTest`.

## Method names

Method names are written in lowerCamelCase.

Method names are typically verbs or verb phrases. For example, `sendMessage` or `stop`.

## Constant names

Constant names use CONSTANT_CASE: all uppercase letters, with words separated by underscores.

But what is a constant, exactly?

Every constant is a **static final** field, *but not all static final fields are constants*.
Before choosing constant case, consider whether the field really feels like a constant.
For example, if any of that instance's observable state can change, it is almost certainly not a constant.
Merely intending to never mutate the object is generally not enough. Examples:

    // Constants
    static final int NUMBER = 5;
    static final ImmutableList<String> NAMES = ImmutableList.of("Ed", "Ann");
    static final Joiner COMMA_JOINER = Joiner.on(','); // because Joiner is immutable
    static final SomeMutableType[] EMPTY_ARRAY = {};
    enum SomeEnum { ENUM_CONSTANT }

    // Not constants
    static String nonFinal = "non-final";
    final String nonStatic = "non-static";
    static final Set<String> mutableCollection = new HashSet<String>();
    static final ImmutableSet<SomeMutableType> mutableElements = ImmutableSet.of(mutable);
    static final Logger logger = Logger.getLogger(MyClass.getName());
    static final String[] nonEmptyArray = {"these", "can", "change"};

## Non-constant field names

Non-constant field names (static or otherwise) are written in lowerCamelCase.

## Parameter names 

Parameter names are written in lowerCamelCase.

## Local variable names

Local variable names are written in lowerCamelCase.

Only looping variables should use one-character names.

Even when final and immutable, local variables are not considered to be constants,
and should not be styled as constants.

## Type variable names

Each type variable is named in one of two styles:

+ A single capital letter, optionally followed by a single numeral (such as `E`, `T`, `X`, `T2`).
+ A name in the form used for classes, followed by the capital letter `T` (examples: `RequestT`, `FooBarT`).

## Camel case: defined

Sometimes there is more than one reasonable way to convert an English phrase into camel case,
such as when acronyms or unusual constructs like "IPv6" or "iOS" are present.
To improve predictability, Dungeon Style specifies the following (nearly) deterministic scheme.

Beginning with the prose form of the name:

+ Convert the phrase to plain ASCII and remove any apostrophes. For example, "Müller's algorithm" might become "Muellers
  algorithm".
+ Divide this result into words, splitting on spaces and any remaining punctuation (typically hyphens).
+ Recommended: if any word already has a conventional camel-case appearance in common usage, split this into its
  constituent parts (e.g., "AdWords" becomes "ad words"). Note that a word such as "iOS" is not really in camel case per
  se; it defies any convention, so this recommendation does not apply.
+ Now lowercase everything (including acronyms), then uppercase only the first character of:
  + each word, to yield upper camel case, or
  + each word except the first, to yield lower camel case
+ Finally, join all the words into a single identifier.

Note that the casing of the original words is almost entirely disregarded. Examples

| Prose form             | Correct         | Incorrect       |
|------------------------|-----------------|-----------------|
| "XML HTTP request"     |XmlHttpRequest   |XMLHTTPRequest   |
| "new customer ID"      |newCustomerId    |newCustomerID    |
| "inner stopwatch"      |innerStopwatch   |innerStopWatch   |
| "supports IPv6 on iOS?"|supportsIpv6OnIos|supportsIPv6OnIOS|

# Programming practices

## @Override: always used

A method is marked with the `@Override` annotation whenever it is legal.

## @Deprecated: never used

If something is no longer used, remove it instead of annotating it.

## Raw types: unwanted

Use parameterized types instead.

    List<Entity> list = makeList(); // good
    List list = makeList(); // wrong

## Caught exceptions: not ignored 

Except as noted below, it is very rarely correct to do nothing in response to a caught exception.
(Typical responses are to log it, or if it is considered "impossible", rethrow it as an `AssertionError`.)

When it truly is appropriate to take no action whatsoever in a catch block, the reason this is justified is explained in
a comment.

    try {
      int i = Integer.parseInt(response);
      return handleNumericResponse(i);
    } catch (NumberFormatException ok) {
      // it's not numeric; that's fine, just continue
    }
    return handleTextResponse(response);

Exception: In tests, a caught exception may be ignored without comment if it is named expected. The following is a very
common idiom for ensuring that the method under test does throw an exception of the expected type, so a comment is
unnecessary here.

    try {
      emptyStack.pop();
      fail();
    } catch (NoSuchElementException expected) {
    }

## Static members: qualified using class 

When a reference to a static class member must be qualified, it is qualified with that class's name, not with a
reference or expression of that class's type.

    Foo aFoo = ...;
    Foo.aStaticMethod(); // good
    aFoo.aStaticMethod(); // bad
    somethingThatYieldsAFoo().aStaticMethod(); // very bad

## Overriding finalizers: forbidden

Do not override `Object.finalize`.

## Cloning: forbidden

Do not implement `Cloneable` or use `clone`.

# Javadoc 

## Formatting

The basic formatting of Javadoc blocks is as seen in this example:

    /**
     * Multiple lines of Javadoc text are written here,
     * wrapped normally...
     */
    public int method(String name) { ... }

One blank line - that is, a line containing only the aligned leading asterisk (`*`) - appears between paragraphs,
and before the group of "at-clauses" if present.
Each paragraph but the first has `<p>` immediately before the first word, with no space after.

## At-clauses

Any of the standard "at-clauses" that are used appear in the order `@param`, `@return`, `@throws`, and these types never
appear with an empty description.

## The summary fragment

The Javadoc for each class and member begins with a brief summary fragment. This fragment is very important and should
be as descriptive as possible.

This is a fragment - a noun phrase or verb phrase, not a complete sentence.
It does not begin with `A {@code Foo} is a...`, or `This method returns...`,
nor does it form a complete imperative sentence like `Save the record.`.
However, the fragment is capitalized and punctuated as if it were a complete sentence.

A common mistake is to write simple Javadoc in the form `/** @return the customer ID */`.
This is incorrect, and should be changed to

    /**
     * Returns the customer ID. 
     */

## Where Javadoc is used

Anywhere it is needed.
If what a method does is not obvious, it should be documented.
Javadoc is not always present on a method that overrides a supertype method.
