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
import org.dungeon.commands.CommandResult;
import org.dungeon.commands.IssuedCommand;
import org.dungeon.gui.GameWindow;
import org.dungeon.io.DLogger;
import org.dungeon.io.IO;
import org.dungeon.io.Loader;
import org.dungeon.util.Messenger;

public class Game {

  private static GameWindow gameWindow;
  private static GameState gameState;
  private static CommandResult lastCommandResult;

  public static void main(String[] args) {
    GameData.loadGameData();
    gameWindow = new GameWindow();
    setGameState(loadAGameStateOrCreateANewOne());
  }

  /**
   * Loads a saved GameState or creates a new one.
   * If a new GameState is created and the saves folder is empty, the tutorial is suggested.
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
    IO.writeNewLine();
    IO.writeString("You may want to issue 'tutorial' to learn the basics.");
  }

  public static GameWindow getGameWindow() {
    return gameWindow;
  }

  public static GameState getGameState() {
    return gameState;
  }

  /**
   * Sets a new GameState to the static field.
   * This setter also invokes the {@code Hero.look()} method on the Hero of the specified GameState.
   * <p/>
   * There is no need to call Engine.refresh() after invoking this method.
   *
   * @param state the new GameState (should not be {@code null})
   */
  public static void setGameState(GameState state) {
    gameState = state;
    Engine.refresh();
    IO.writeNewLine(); // Improves readability.
    gameState.getHero().look(null);
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
    if (gameState.getHero().isDead()) {
      IO.writeString("You died.");
      setGameState(loadAGameStateOrCreateANewOne());
    } else {
      // Advance the campaign's world date.
      if (lastCommandResult != null) {
        if (lastCommandResult.getDuration() > 0) {
          gameState.getWorld().rollDate(lastCommandResult.getDuration());
        }
        // If the last turn changed the GameState, set the game as not saved.
        if (lastCommandResult.evaluateIfGameStateChanged()) {
          gameState.setSaved(false);
        }
      }
      // Refresh the campaign state.
      Engine.refresh();
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
      lastCommandResult = command.execute(issuedCommand);
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
      DLogger.info("Exited with no problems.");
    }
    System.exit(0);
  }

}
