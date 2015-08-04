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

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class Game {

  private static GameWindow gameWindow;
  private static GameState gameState;

  public static void main(String[] args) {
    GameData.loadGameData();
    try {
      SwingUtilities.invokeAndWait(new Runnable() {
        @Override
        public void run() {
          gameWindow = new GameWindow();
        }
      });
    } catch (InterruptedException fatal) {
      DungeonLogger.severe(fatal.getMessage());
      System.exit(1);
    } catch (InvocationTargetException fatal) {
      DungeonLogger.severe(fatal.getMessage());
      System.exit(1);
    }
    setGameState(loadAGameStateOrCreateANewOne());
  }

  /**
   * Loads a saved GameState or creates a new one. If a new GameState is created and the saves folder is empty, the
   * tutorial is suggested.
   */
  private static GameState loadAGameStateOrCreateANewOne() {
    GameState gameState = Loader.loadGame();
    if (gameState == null) {
      gameState = Loader.newGame();
      // Note that loadedGameState may be null even if a save exists (if the player declined to load it).
      // So check for any save in the folder.
      if (!Loader.checkForAnySave()) { // Suggest the tutorial only if no saved game exists.
        suggestTutorial();
      }
    }
    return gameState;
  }

  private static void suggestTutorial() {
    Writer.writeNewLine();
    Writer.writeString("You may want to issue 'tutorial' to learn the basics.");
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
    gameState = state;
    if (state == null) {
      DungeonLogger.info("Set the GameState field in Game to null.");
    } else {
      DungeonLogger.info("Set the GameState field in Game to a GameState.");
      // This is a new GameState that must be refreshed in order to have spawned creatures at the beginning.
      Engine.refresh();
      Writer.writeNewLine(); // Improves readability.
      gameState.getHero().look(null);
    }
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
    if (gameState.getHero().getHealth().isDead()) {
      Writer.writeString("You died.");
      setGameState(loadAGameStateOrCreateANewOne());
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
    gameState.getCommandHistory().addCommand(issuedCommand);
    gameState.getStatistics().addCommand(issuedCommand);
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
    if (gameState != null) {
      if (!gameState.isSaved()) {
        Loader.saveGame(gameState);
      }
      DungeonLogger.info("Exited with no problems.");
    }
    System.exit(0);
  }

}
