package org.mafagafogigante.dungeon.commands;

import org.mafagafogigante.dungeon.achievements.Achievement;
import org.mafagafogigante.dungeon.achievements.AchievementStoreFactory;
import org.mafagafogigante.dungeon.achievements.AchievementTracker;
import org.mafagafogigante.dungeon.achievements.AchievementTrackerWriter;
import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.entity.creatures.Creature;
import org.mafagafogigante.dungeon.entity.creatures.Hero;
import org.mafagafogigante.dungeon.entity.items.CreatureInventory.SimulationResult;
import org.mafagafogigante.dungeon.entity.items.Item;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Engine;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.GameState;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.Location;
import org.mafagafogigante.dungeon.game.LocationPreset;
import org.mafagafogigante.dungeon.game.LocationPresetStore;
import org.mafagafogigante.dungeon.game.Point;
import org.mafagafogigante.dungeon.game.Random;
import org.mafagafogigante.dungeon.game.World;
import org.mafagafogigante.dungeon.gui.WritingSpecifications;
import org.mafagafogigante.dungeon.io.Loader;
import org.mafagafogigante.dungeon.io.PoemWriter;
import org.mafagafogigante.dungeon.io.SavesTableWriter;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.map.WorldMapWriter;
import org.mafagafogigante.dungeon.stats.CauseOfDeath;
import org.mafagafogigante.dungeon.stats.ExplorationStatistics;
import org.mafagafogigante.dungeon.util.ColumnAlignment;
import org.mafagafogigante.dungeon.util.CounterMap;
import org.mafagafogigante.dungeon.util.Messenger;
import org.mafagafogigante.dungeon.util.SystemInformation;
import org.mafagafogigante.dungeon.util.Table;
import org.mafagafogigante.dungeon.util.Tutorial;
import org.mafagafogigante.dungeon.util.library.Libraries;
import org.mafagafogigante.dungeon.wiki.WikiSearcher;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class CommandSets {

  private static final Map<String, CommandSet> commandSetMap = initializeCommandSetMap();

  private CommandSets() {
    throw new AssertionError();
  }

  @NotNull
  private static Map<String, CommandSet> initializeCommandSetMap() {
    Map<String, CommandSet> map = new HashMap<>();
    map.put("default", initializeDefaultCommandSet());
    map.put("extra", initializeExtraCommandSet());
    map.put("debug", initializeDebugCommandSet());
    return map;
  }

  private static CommandSet initializeDefaultCommandSet() {
    CommandSet commandSet = CommandSet.emptyCommandSet();
    // Respect the alphabetical ordering of the Command names.
    commandSet.addCommand(new Command("achievements", "Displays the already unlocked achievements.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        AchievementTrackerWriter.parseCommand(arguments);
      }
    });
    commandSet.addCommand(new Command("age", "Displays the character's age.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().printAge();
      }
    });
    commandSet.addCommand(new Command("cast", "Casts a spell.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().getSpellcaster().parseCast(arguments);
      }
    });
    commandSet.addCommand(new Command("destroy", "Destroys items on the ground.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().destroyItems(arguments);
      }
    });
    commandSet.addCommand(new Command("drop", "Drops the specified items.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().dropItems(arguments);
      }
    });
    commandSet.addCommand(new Command("drink", "Drinks an item.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().drinkItem(arguments);
      }
    });
    commandSet.addCommand(new Command("eat", "Eats an item.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().eatItem(arguments);
      }
    });
    commandSet.addCommand(new Command("conditions", "Lists the currently active conditions.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().writeConditions();
      }
    });
    commandSet.addCommand(new Command("equip", "Equips the specified item.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().parseEquip(arguments);
      }
    });
    commandSet.addCommand(new Command("examine", "Examines an item.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().examineItem(arguments);
      }
    });
    commandSet.addCommand(new Command("exit", "Exits the game.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.exit();
      }
    });
    commandSet.addCommand(new Command("fish", "Attempts to fish.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().fish();
      }
    });
    commandSet.addCommand(new Command("go", "Makes the character move in the specified direction.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().walk(arguments);
      }
    });
    commandSet.addCommand(new Command("items", "Lists the items in the character's inventory.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().writeInventory();
      }
    });
    commandSet.addCommand(new Command("kill", "Attacks the target chosen by the player.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().attackTarget(arguments);
      }
    });
    commandSet.addCommand(new Command("load", "Loads a saved game.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        GameState loadedGameState = Loader.parseLoadCommand(arguments);
        if (loadedGameState != null) {
          Game.unsetGameState();
          Game.setGameState(loadedGameState);
        }
      }
    });
    commandSet.addCommand(new Command("look", "Describes what the character can see.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().look();
      }
    });
    commandSet.addCommand(new Command("map", "Shows a map of your surroundings.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        WorldMapWriter.writeMap();
      }
    });
    commandSet.addCommand(new Command("milk", "Attempts to milk a creature.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().parseMilk(arguments);
      }
    });
    commandSet.addCommand(new Command("new", "Starts a new game.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.unsetGameState();
        Game.setGameState(Loader.newGame());
      }
    });
    commandSet.addCommand(new Command("pick", "Attempts to pick up items from the current location.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().pickItems(arguments);
      }
    });
    commandSet.addCommand(new Command("read", "Reads the specified item.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().readItem(arguments);
      }
    });
    commandSet.addCommand(new Command("rest", "Rests until healing about three fifths of the character's health.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().rest();
      }
    });
    commandSet.addCommand(new Command("save", "Saves the game.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Loader.saveGame(Game.getGameState(), arguments);
      }
    });
    commandSet.addCommand(new Command("saves", "Displays a table with all the save files.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        SavesTableWriter.writeSavesFolderTable();
      }
    });
    commandSet.addCommand(new Command("sleep", "Sleeps until the sun rises.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().sleep();
      }
    });
    commandSet.addCommand(new Command("spells", "Lists all the spells known by the character.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().writeSpellList();
      }
    });
    commandSet.addCommand(new Command("status", "Displays the character's status.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().printAllStatus();
      }
    });
    commandSet.addCommand(new Command("time", "Displays what the character knows about the current time.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().readTime();
      }
    });
    commandSet.addCommand(new Command("tutorial", "Displays the tutorial.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Writer.write(new Tutorial(), new WritingSpecifications(false, 0));
      }
    });
    commandSet.addCommand(new Command("unequip", "Unequips the currently equipped item.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getHero().unequipWeapon();
      }
    });
    commandSet.addCommand(new Command("wiki", "Searches the wiki for an article.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        WikiSearcher.search(arguments);
      }
    });
    return commandSet;
  }

  private static CommandSet initializeExtraCommandSet() {
    CommandSet commandSet = CommandSet.emptyCommandSet();
    commandSet.addCommand(new Command("text", "Throws an enormous amount of colored text on the screen.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        List<String> alphabet = Arrays.asList("abcdefghijklmnopqrstuvwxyz".split(""));
        DungeonString dungeonString = new DungeonString();
        for (int i = 0; i < 10000; i++) {
          dungeonString.setColor(new Color(Random.nextInteger(256), Random.nextInteger(256), Random.nextInteger(256)));
          dungeonString.append(Random.select(alphabet));
        }
        Writer.write(dungeonString);
      }
    });
    commandSet.addCommand(new Command("hint", "Displays a random hint of the game.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Writer.write(Libraries.getHintLibrary().next());
      }
    });
    commandSet.addCommand(new Command("poem", "Prints a poem from the poem library.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        PoemWriter.parsePoemCommand(arguments);
      }
    });
    commandSet.addCommand(new Command("statistics", "Displays all available game statistics.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Game.getGameState().getStatistics().writeStatistics();
      }
    });
    commandSet.addCommand(new Command("system", "Displays information about the underlying system.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Writer.write(new SystemInformation());
      }
    });
    commandSet.addCommand(new Command("version", "Displays the game version.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Writer.write("Dungeon version " + Version.getCurrentVersion() + ".");
      }
    });
    return commandSet;
  }

  private static CommandSet initializeDebugCommandSet() {
    CommandSet commandSet = CommandSet.emptyCommandSet();
    commandSet.addCommand(new Command("achievements", "Writes the achievements you have not yet unlocked.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        AchievementTracker tracker = Game.getGameState().getHero().getAchievementTracker();
        List<Achievement> notYetUnlockedAchievementList = new ArrayList<>();
        for (Achievement achievement : AchievementStoreFactory.getDefaultStore().getAchievements()) {
          if (tracker.hasNotBeenUnlocked(achievement)) {
            notYetUnlockedAchievementList.add(achievement);
          }
        }
        if (notYetUnlockedAchievementList.isEmpty()) {
          Writer.write("All achievements have been unlocked.");
        } else {
          Collections.sort(notYetUnlockedAchievementList, new Comparator<Achievement>() {
            @Override
            public int compare(Achievement o1, Achievement o2) {
              return o1.getName().compareTo(o2.getName());
            }
          });
          for (Achievement achievement : notYetUnlockedAchievementList) {
            Writer.write(String.format("%s : %s", achievement.getName(), achievement.getInfo()));
          }
        }
      }
    });
    commandSet.addCommand(new Command("exploration", "Writes statistics about your exploration.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
        List<ColumnAlignment> columnAlignments = new ArrayList<>();
        columnAlignments.add(ColumnAlignment.LEFT);
        columnAlignments.add(ColumnAlignment.RIGHT);
        columnAlignments.add(ColumnAlignment.RIGHT);
        columnAlignments.add(ColumnAlignment.RIGHT);
        Table table = new Table("Name", "Kills", "Visited so far", "Maximum number of visits");
        table.setColumnAlignments(columnAlignments);
        for (LocationPreset preset : LocationPresetStore.getDefaultLocationPresetStore().getAllPresets()) {
          String name = preset.getName().getSingular();
          String kills = String.valueOf(explorationStatistics.getKillCount(preset.getId()));
          String visitedSoFar = String.valueOf(explorationStatistics.getVisitedLocations(preset.getId()));
          String maximumNumberOfVisits = String.valueOf(explorationStatistics.getMaximumNumberOfVisits(preset.getId()));
          table.insertRow(name, kills, visitedSoFar, maximumNumberOfVisits);
        }
        Writer.write(table);
      }
    });
    commandSet.addCommand(new Command("kills", "Writes statistics about your killings.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        CounterMap<CauseOfDeath> map =
            Game.getGameState().getStatistics().getBattleStatistics().getKillsByCauseOfDeath();
        if (map.isNotEmpty()) {
          Table table = new Table("Type", "Count");
          table.setColumnAlignments(Arrays.asList(ColumnAlignment.LEFT, ColumnAlignment.RIGHT));
          for (CauseOfDeath causeOfDeath : map.keySet()) {
            table.insertRow(causeOfDeath.toString(), String.valueOf(map.getCounter(causeOfDeath)));
          }
          Writer.write(table);
        } else {
          Writer.write("You haven't killed anything yet. Go kill something!");
        }
      }
    });
    commandSet.addCommand(new Command("location", "Writes information about the current location.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        final int width = 40;  // The width of the row's "tag".
        Location heroLocation = Game.getGameState().getHero().getLocation();
        Point heroPosition = heroLocation.getPoint();
        DungeonString dungeonString = new DungeonString();
        dungeonString.append(StringUtils.rightPad("Point:", width));
        dungeonString.append(heroPosition.toString());
        dungeonString.append("\n");
        dungeonString.append(StringUtils.rightPad("Creatures (" + heroLocation.getCreatureCount() + "):", width));
        dungeonString.append("\n");
        for (Creature creature : heroLocation.getCreatures()) {
          dungeonString.append("  " + creature.getName());
          dungeonString.append("\n");
        }
        if (!heroLocation.getItemList().isEmpty()) {
          dungeonString.append(StringUtils.rightPad("Items (" + heroLocation.getItemList().size() + "):", width));
          dungeonString.append("\n");
          for (Item item : heroLocation.getItemList()) {
            dungeonString.append("  " + item.getQualifiedName());
            dungeonString.append("\n");
          }
        } else {
          dungeonString.append("No items.\n");
        }
        dungeonString.append(StringUtils.rightPad("Luminosity:", width));
        dungeonString.append(heroLocation.getLuminosity().toPercentage().toString());
        dungeonString.append("\n");
        dungeonString.append(StringUtils.rightPad("Permittivity:", width));
        dungeonString.append(heroLocation.getLightPermittivity().toString());
        dungeonString.append("\n");
        dungeonString.append(StringUtils.rightPad("Blocked Entrances:", width));
        dungeonString.append(heroLocation.getBlockedEntrances().toString());
        dungeonString.append("\n");
        Writer.write(dungeonString);
      }
    });
    commandSet.addCommand(new Command("map", "Produces a map as complete as possible.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        WorldMapWriter.writeDebugMap();
      }
    });
    commandSet.addCommand(new Command("give", "Gives items to the character.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        if (arguments.length != 0) {
          World world = Game.getGameState().getWorld();
          Date date = world.getWorldDate();
          try {
            Id id = new Id(arguments[0].toUpperCase(Locale.ENGLISH));
            if (world.getItemFactory().canMakeItem(id)) {
              Item item = world.getItemFactory().makeItem(id, date);
              Writer.write("Item successfully created.");
              Hero hero = Game.getGameState().getHero();
              if (hero.getInventory().simulateItemAddition(item) == SimulationResult.SUCCESSFUL) {
                hero.addItem(item);
              } else {
                hero.getLocation().addItem(item);
                Writer.write("Item could not be added to your inventory. Thus, it was added to the current location.");
              }
              Engine.refresh(); // Set the game state to unsaved after adding an item to the world.
            } else {
              Writer.write("Item could not be created due to a restriction.");
            }
          } catch (IllegalArgumentException invalidPreset) {
            Writer.write("Item could not be created.");
          }
        } else {
          Messenger.printMissingArgumentsMessage();
        }
      }
    });
    commandSet.addCommand(new Command("saved", "Tests if the game is saved or not.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        if (Game.getGameState().isSaved()) {
          Writer.write("The game is saved.");
        } else {
          Writer.write("This game state is not saved.");
        }
      }
    });
    commandSet.addCommand(new Command("spawn", "Spawns a creature.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        if (arguments.length != 0) {
          for (String argument : arguments) {
            Id givenId = new Id(argument.toUpperCase(Locale.ENGLISH));
            World world = Game.getGameState().getWorld();
            Creature creature = world.getCreatureFactory().makeCreature(givenId, world);
            if (creature != null) {
              Game.getGameState().getHero().getLocation().addCreature(creature);
              Writer.write("Spawned a " + creature.getName() + ".");
              Engine.refresh(); // Set the game state to unsaved after adding a creature to the world.
            } else {
              Writer.write(givenId + " does not match any known creature.");
            }
          }
        } else {
          Messenger.printMissingArgumentsMessage();
        }
      }
    });
    commandSet.addCommand(new Command("time", "Writes information about the current time.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        Writer.write(Game.getGameState().getWorld().getWorldDate().toString());
      }
    });
    commandSet.addCommand(new Command("wait", "Makes time pass.") {
      @Override
      public void execute(@NotNull String[] arguments) {
        DebugWaitParser.parseDebugWait(arguments);
      }
    });
    return commandSet;
  }

  static boolean hasCommandSet(String identifier) {
    return commandSetMap.containsKey(identifier);
  }

  static CommandSet getCommandSet(String identifier) {
    return commandSetMap.get(identifier);
  }

}
