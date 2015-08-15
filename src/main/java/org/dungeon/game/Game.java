/*
 * Copyright (C) 2014 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.dungeon.game;

import org.dungeon.commands.Command;
import org.dungeon.commands.CommandCollection;
import org.dungeon.commands.IssuedCommand;
import org.dungeon.gui.GameWindow;
import org.dungeon.io.DungeonLogger;
import org.dungeon.io.Loader;
import org.dungeon.io.Writer;
import org.dungeon.util.Messenger;
import org.dungeon.util.StopWatch;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Game {

  private static final InstanceInformation instanceInformation = new InstanceInformation();

  private static GameWindow gameWindow;
  private static GameState gameState;

  public static void main(String[] args) {
    final StopWatch stopWatch = new StopWatch();
    GameData.loadGameData();
    DungeonLogger.info("Finished loading game data. Took " + stopWatch.toString() + ".");
    invokeOnEventDispatchThreadAndWait(new Runnable() {
      @Override
      public void run() {
        gameWindow = new GameWindow();
      }
    });
    DungeonLogger.info("Finished making the window. Took " + stopWatch.toString() + ".");
    setGameState(getInitialGameState());
    invokeOnEventDispatchThreadAndWait(new Runnable() {
      @Override
      public void run() {
        getGameWindow().startAcceptingCommands();
        DungeonLogger.info("Signaled the window to start accepting commands.");
      }
    });
  }

  /**
   * Invokes a runnable on the EDT and waits for it to finish. If an exception is thrown, this method logs it and
   * finishes the application.
   */
  private static void invokeOnEventDispatchThreadAndWait(Runnable runnable) {
    try {
      SwingUtilities.invokeAndWait(runnable);
    } catch (InterruptedException fatal) {
      DungeonLogger.severe(fatal.toString());
      System.exit(1);
    } catch (InvocationTargetException fatal) {
      DungeonLogger.severe(fatal.toString());
      System.exit(1);
    }
  }

  /**
   * Loads a saved GameState or creates a new one. Should be invoked to get the first GameState of the instance.
   *
   * <p>If a new GameState is created and the saves folder is empty, the tutorial is suggested.
   */
  private static GameState getInitialGameState() {
    GameState gameState = Loader.loadGame(true);
    if (gameState == null) {
      gameState = Loader.newGame();
      // Note that loadedGameState may be null even if a save exists (if the player declined to load it).
      // So check for any save in the folder.
      if (!Loader.checkForSave()) { // Suggest the tutorial only if no saved game exists.
        suggestTutorial();
      }
    }
    return gameState;
  }

  private static void suggestTutorial() {
    Writer.writeNewLine();
    Writer.writeString("You may want to issue 'tutorial' to learn the basics.");
  }

  /**
   * Gets a GameState object. Should be invoked to get a GameState after the Hero dies.
   */
  private static GameState getAfterDeathGameState() {
    GameState gameState = Loader.loadGame(false);
    if (gameState != null) {
      JOptionPane.showMessageDialog(getGameWindow(), "Loaded the most recent saved game.");
    } else {
      gameState = Loader.newGame();
      JOptionPane.showMessageDialog(getGameWindow(), "Could not load a saved game. Created a new game.");
    }
    return gameState;
  }

  public static GameWindow getGameWindow() {
    return gameWindow;
  }

  public static GameState getGameState() {
    return gameState;
  }

  /**
   * Sets a new GameState to the static field. Can be used to nullify the GameState, something that should be done while
   * another GameState is being created. If the provided GameState is not null, this setter also invokes Hero.look().
   *
   * @param state another GameState object, or null
   */
  public static void setGameState(GameState state) {
    if (getGameState() != null) {
      DungeonLogger.warning("Called setGameState without unsetting the old game state.");
    }
    if (state == null) {
      throw new IllegalArgumentException("passed null to setGameState.");
    }
    gameState = state;
    DungeonLogger.info("Set the GameState field in Game to a GameState.");
    // This is a new GameState that must be refreshed in order to have spawned creatures at the beginning.
    Engine.refresh();
    Writer.writeNewLine(); // Improves readability.
    gameState.getHero().look(null);

  }

  public static void unsetGameState() {
    DungeonLogger.info("Set the GameState field in Game to null.");
    gameState = null;
  }

  /**
   * Renders a turn based on the last IssuedCommand.
   *
   * @param issuedCommand the last IssuedCommand.
   */
  public static void renderTurn(IssuedCommand issuedCommand) {
    // Clears the text pane.
    getGameWindow().clearTextPane();
    processInput(issuedCommand);
    if (getGameState().getHero().getHealth().isDead()) {
      getGameWindow().clearTextPane();
      Writer.writeString("You died.");
      unsetGameState();
      setGameState(getAfterDeathGameState());
    } else {
      Engine.endTurn();
    }
  }

  /**
   * Processes the player's input. Adds the IssuedCommand to the CommandHistory and to the CommandStatistics. Finally,
   * this method finds and executes the corresponding Command object or prints a message if there is not such Command.
   *
   * @param issuedCommand the last IssuedCommand.
   */
  private static void processInput(IssuedCommand issuedCommand) {
    instanceInformation.incrementAcceptedCommandCount();
    getGameState().getCommandHistory().addCommand(issuedCommand);
    getGameState().getStatistics().addCommand(issuedCommand);
    Command command = CommandCollection.getDefaultCommandCollection().getCommand(issuedCommand);
    if (command != null) {
      command.execute(issuedCommand);
    } else {
      Messenger.printInvalidCommandMessage(issuedCommand.getFirstToken());
    }
  }

  /**
   * Exits the game, prompting the user if the current state should be saved if it is not already saved.
   */
  public static void exit() {
    if (getGameState() != null && !getGameState().isSaved()) {
      Loader.saveGame(getGameState());
    }
    logInstanceClosing();
    System.exit(0);
  }

  private static void logInstanceClosing() {
    String durationString = instanceInformation.getDurationString();
    int commandCount = instanceInformation.getAcceptedCommandCount();
    DungeonLogger.info("Closing instance. Ran for " + durationString + ". Commands parsed: " + commandCount + ".");
  }

}
