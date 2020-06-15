# Dungeon

[![Travis CI](https://travis-ci.org/bernardosulzbach/dungeon.svg?branch=master)](https://travis-ci.org/bernardosulzbach/dungeon/)
[![AppVeyor](https://ci.appveyor.com/api/projects/status/ef8ijtoeov8dy5ba/branch/master?svg=true)](https://ci.appveyor.com/project/bernardosulzbach/dungeon/branch/master)
[![Coverity Scan Build Status](https://scan.coverity.com/projects/6794/badge.svg)](https://scan.coverity.com/projects/bernardosulzbach-dungeon)
[![codecov.io](http://codecov.io/github/bernardosulzbach/dungeon/coverage.svg?branch=master)](http://codecov.io/github/mafagafogigante/dungeon?branch=master)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/bernardosulzbach/dungeon)

Dungeon is a text-based open-world role-playing-game.
You are in control of the life of a character that wakes up with a headache and can't remember exactly what happened to him.
You need to do your best to equip yourself to explore Darrowmere scavenging for powerful and valuable treasure.

You are completely free to explore Darrowmere however you want.
Be it slaying the most terrible creatures, drinking milk directly from cows, or reading books found in the world to learn about its history and the lives of some of the Commons (the human-like race of Darrowmere) that lived there.
Some books may even teach your character magical spells that give him some advantage when dealing with the aberrations roaming the land.

The world is randomly generated, making each and every experience unique.
There is also an in-game wiki with a lot of information about the different facets of the game. You can also [browse the wiki online](https://github.com/bernardosulzbach/dungeon/wiki).

## Screenshots

[See the gallery](https://www.bernardosulzbach.com/dungeon/screenshots).

## Running the game

[Download a release](https://github.com/bernardosulzbach/dungeon/releases).

You only need an updated version of Java to play this game.

## Awards

In 2017 this project won Yegor Bugayenko's Software Quality Award.

[![Software Quality Award](images/software-quality-award.png)](https://www.yegor256.com/2016/10/23/award-2017.html)

## Tools

### YourKit

![YourKit](https://www.yourkit.com/images/yklogo.png)

YourKit supports open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/) and [YourKit .NET Profiler](https://www.yourkit.com/.net/profiler/), innovative and intelligent tools for profiling Java and .NET applications.

## Building the game

If you want to build the game locally, issue

```bash
$ git clone https://github.com/bernardosulzbach/dungeon.git
$ cd dungeon
$ mvn package
$ java -jar target/dungeon-[version].jar
```

## Reporting bugs

Bug reports, questions, and suggestions are welcome on our [issue tracker](https://github.com/bernardosulzbach/dungeon/issues).

## Contributing

If you know Java, Python, or even if you just know English well, you can contribute.

Check out the [issue tracker](https://github.com/bernardosulzbach/dungeon/issues) to see what could be done or figure out something you would like to do and start working on it.

Discussing your ideas on the tracker before coding is a good way to increase the chance of your changes being accepted.

Do not forget to also read [Dungeon's contributing guide](https://github.com/bernardosulzbach/dungeon/blob/master/CONTRIBUTING.md).

## Versioning scheme

Dungeon uses [semantic versioning](http://semver.org/spec/v2.0.0.html).

- A release that is **incompatible with old saved files** increases the
  **MAJOR** version.
- A release that adds **content** in a backwards-compatible manner increases
  the **MINOR** version.
- A release that makes backwards-compatible **bug fixes** increases the
  **PATCH** version.

## License

The game is licensed under the [BSD 3-Clause](https://github.com/bernardosulzbach/dungeon/blob/master/LICENSE.txt) license.
