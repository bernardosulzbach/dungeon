Contributing
============
This file contains some basic directives for developers interested in contributing to this project.

Forking and submitting changes
------------------------------
**Note: read "Commits and PRs" below before trying anything**
* Fork the project on GitHub to your own account
* Clone **your fork** locally by using `git clone`
* Branch it off by using `git branch [descriptive-branch-name]`
* Run `mvn test` to run all tests locally
  * Fix all issues you find
* Commit and push your changes to GitHub and open a pull request

Setting up your environment
---------------------------
**Note: this is a suggested setup, you may do things differently**
* Install Git, Maven 3, Java JDK 7 (or later), and IntelliJ IDEA
* Clone your forked repository locally
* Open IntelliJ Idea
  * Press "Create New Project"
  * Select "Java"
  * Press "Next"
  * Press "Next"
  * Change the location to the clone directory
  * Press "Finish"

Commits and PRs
---------------
Please discuss design and implementation details on the issue tracker.

Writing code
------------
Strive for

1. Readability
2. Simplicity
3. Performance

*In this order.*

**"Strive to write a good program rather than a fast one."** (adapted from Effective Java 2nd Edition).


Code Style
----------
Our code style is a slightly modified version of Google Java Style called
[Dungeon Java Style](https://github.com/mafagafogigante/dungeon/blob/master/STYLE.md).

My main settings in IDEA are in the `idea` directory.

Import the JAR into the IDE to get proper code reformatting and a project-specific inspection profile.

**Note that this is not 100% style compliant, you will still need to fix some things yourself, like Javadoc.**
