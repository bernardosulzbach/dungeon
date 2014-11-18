package org.dungeon.core.game;

import org.dungeon.io.IO;
import org.dungeon.utils.Utils;

import java.awt.*;

/**
 * Some in-game configuration methods.
 * <p/>
 * Created by Bernardo Sulzbach on 04/11/14.
 */
class ConfigTools {

    private static final String[] args = {"bars", "bold", "generator", "list", "rows"};

    private static final int MIN_ROWS = 10;
    private static final int MAX_ROWS = 50;

    private static void listAllArguments() {
        IO.writeString("Arguments are: ");
        for (String arg : args) {
            IO.writeString("\n  " + arg);
        }
    }

    private static void toggleBold() {
        boolean newBoldValue = !Game.getGameState().isBold();
        Game.getGameState().setBold(newBoldValue);
        IO.writeString("Bold set to " + newBoldValue + ".");
    }

    private static void toggleBars() {
        boolean newUsingBarsValue = !Game.getGameState().isUsingBars();
        Game.getGameState().setUsingBars(newUsingBarsValue);
        if (newUsingBarsValue) {
            IO.writeString("Bars enabled.");
        } else {
            IO.writeString("Bars disabled.");
        }
    }

    private static boolean changeRowCount(String argument) {
        try {
            int rows = Integer.parseInt(argument);
            if (rows < MIN_ROWS || rows > MAX_ROWS) {
                IO.writeString("Row count should be in the range [" + MIN_ROWS + ", " + MAX_ROWS + "].");
            } else {
                // setRows returns true if the number of rows changed.
                if (Game.getGameWindow().setRows(rows)) {
                    IO.writeString("Rows set to " + rows + ".");
                    return true;
                } else {
                    IO.writeString("Row count unchanged.");
                }
            }
        } catch (NumberFormatException exception) {
            IO.writeString("Provide a valid number of rows.", Color.RED);
        }
        return false;
    }

    // Returns true if the chunk side changed.
    private static boolean changeChunkSide(String argument) {
        try {
            int givenSide = Integer.parseInt(argument);
            int oldChunkSide = Game.getGameState().getWorld().getGenerator().getChunkSide();
            // The setter returns the new chunk side.
            int newChunkSide = Game.getGameState().getWorld().getGenerator().setChunkSide(givenSide);
            if (oldChunkSide == newChunkSide) {
                IO.writeString("Chunk side unchanged.");
            } else {
                IO.writeString("Chunk side set to " + newChunkSide + ".");
                return true;
            }
        } catch (NumberFormatException exception) {
            IO.writeString("Provide a valid number.", Color.RED);
        }
        return false;
    }

    // Returns true if a configuration was changed.
    static boolean parseConfigCommand(String[] inputWords) {
        if (inputWords.length == 1) {
            Utils.printMissingArgumentsMessage();
        } else {
            if (inputWords[1].equals(args[0])) {
                toggleBars();
                return true;
            } else if (inputWords[1].equals(args[1])) {
                toggleBold();
                return true;
            } else if (inputWords[1].equals(args[2])) {
                if (inputWords.length > 2) {
                    return changeChunkSide(inputWords[2]);
                } else {
                    IO.writeString("Provide a numerical argument.");
                }
            } else if (inputWords[1].equals(args[3])) {
                listAllArguments();
            } else if (inputWords[1].equals(args[4])) {
                if (inputWords.length > 2) {
                    return changeRowCount(inputWords[2]);
                } else {
                    IO.writeString("Provide a number of rows.");
                }
            } else {
                IO.writeString("Invalid command. Use 'config list' to see all available configurations.", Color.RED);
            }
        }
        return false;
    }

}
