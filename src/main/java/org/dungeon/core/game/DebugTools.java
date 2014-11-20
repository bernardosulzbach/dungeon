package org.dungeon.core.game;

import org.dungeon.core.creatures.Creature;
import org.dungeon.core.items.Item;
import org.dungeon.io.IO;
import org.dungeon.io.Loader;
import org.dungeon.utils.Constants;
import org.dungeon.utils.Utils;
import org.joda.time.DateTime;

import java.awt.*;

/**
 * A set of debugging tools.
 * <p/>
 * Created by Bernardo Sulzbach on 03/11/14.
 */
class DebugTools {

    private static final String[] args = {"exploration", "tomorrow", "holidays", "saves", "location", "generator",
            "saved", "list"};

    private static void listAllArguments() {
        IO.writeString("Arguments are: ");
        for (String arg : args) {
            IO.writeString("\n  " + arg);
        }
    }

    private static void printIsSaved() {
        if (Game.getGameState().isSaved()) {
            IO.writeString("The game is saved.");
        } else {
            IO.writeString("This game state is not saved.");
        }
    }

    static void parseDebugCommand(String[] words) {
        if (words.length == 1) {
            Utils.printMissingArgumentsMessage();
        } else {
            final String firstWord = words[1];
            if (firstWord.equals(args[0])) {
                IO.writeString(Game.getGameState().getHero().getExplorationLog().toString());
            } else if (firstWord.equals(args[1])) {
                Game.getGameState().getWorld().rollDate(24 * 60 * 60);
                if (Game.RANDOM.nextBoolean()) {
                    IO.writeString("A day has passed.", Color.ORANGE);
                } else {
                    IO.writeString("You are one day closer to your ending.", Color.ORANGE);
                }
            } else if (firstWord.equals(args[2])) {
                String holiday;
                for (DateTime date = new DateTime(1970, 1, 1, 0, 0); date.getYear() != 1971; date = date.plusDays(1)) {
                    holiday = org.dungeon.utils.Holiday.getHoliday(date);
                    if (holiday != null) {
                        IO.writeKeyValueString(Constants.DATE_FORMAT.print(date), holiday);
                    }
                }
            } else if (firstWord.equals(args[3])) {
                Loader.printFilesInSavesFolder();
            } else if (firstWord.equals("lines")) {
                for (int i = 1; i <= Constants.ROWS; i++) {
                    IO.writeString(Integer.toString(i));
                }
            } else if (firstWord.equals(args[4])) {
                final int WIDTH = 20;  // The width of the row's "tag".
                GameState gameState = Game.getGameState();
                Point heroPosition = gameState.getHeroPosition();
                Location location = gameState.getWorld().getLocation(heroPosition);
                StringBuilder sb = new StringBuilder();
                sb.append(Utils.padString("Point:", WIDTH)).append(heroPosition.toString()).append('\n');
                sb.append(Utils.padString("Creatures (" + location.getCreatureCount() + "):", WIDTH)).append('\n');
                for (Creature creature : location.getCreatures()) {
                    sb.append("  ").append(creature.getName()).append('\n');
                }
                if (location.getItemCount() != 0) {
                    sb.append(Utils.padString("Items (" + location.getItemCount() + "):", WIDTH)).append('\n');
                    for (Item item : location.getItemList()) {
                        sb.append("  ").append(item.getQualifiedName()).append('\n');
                    }
                } else {
                    sb.append("No items.\n");
                }
                sb.append(Utils.padString("Luminosity:", WIDTH)).append(location.getLuminosity()).append('\n');
                sb.append(Utils.padString("Permittivity:", WIDTH)).append(location.getLightPermittivity()).append('\n');
                IO.writeString(sb.toString());
            } else if (firstWord.equals(args[5])) {
                Game.getGameState().getWorld().getGenerator().printStatistics();
            } else if (firstWord.equals(args[6])) {
                printIsSaved();
            } else if (firstWord.equals(args[7])) {
                listAllArguments();
            } else {
                switch (Game.RANDOM.nextInt(4)) {
                    case 0:
                        IO.writeString("FOOL! You cannot understand the power of 'debug'!", Color.RED);
                        break;
                    case 1:
                        IO.writeString("If you do not know how to use the debug command, do not use it.", Color.RED);
                        break;
                    case 2:
                        IO.writeString("Wrong are you.", Color.RED);
                        break;
                    case 3:
                        IO.writeString("Stop trying to cheat, will you?", Color.RED);
                        break;
                }
            }
        }
    }

}
