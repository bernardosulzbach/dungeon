dungeon
=======
A simple text-based RPG using Java

Requirements
-------------------
Java 6 or newer.

If you have trouble running the game with a supposedly supported version of Java, tell me.

Running the game
----------------
Download a release or download the source and build it yourself.

Reporting bugs
--------------
Bug reports are welcome in our [issue tracker](https://github.com/mafagafogigante/dungeon/issues).


TODO
====

Known Bugs
----------

Planned features
----------------
- DStopWatch class for timing stuff in the game.
- Stop ignoring exceptions, log them all instead.
- Alignment system. (Killing good creatures make you evil.)
- Trees.
- Load hints from resource files.
- A Command type for the commands, create an instance for all commands and iterate through them.
- Store command help in the respective Command object.
- Store aliases independently and let the player add and remove aliases.
- Maps (a ``map`` command that prints an ASCII map of the locations the player already visited).
- Creature will have an bloodAlcoholContent double variable (% by volume).
- AttackAlgorithms will account for insobriety.
- Implement a third coordinate to Point (z).
- Implement dungeons (in z = -1).
- Implement healing during sleep.
- Harvesting.
- Farming. (Gathering seeds, planting and harvesting.)
- Factions.
- Reputations.

License
-------
[GNU GPLv3](https://github.com/mafagafogigante/dungeon/blob/master/LICENSE.txt)

