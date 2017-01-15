package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.commands.IssuedCommand;
import org.mafagafogigante.dungeon.commands.IssuedCommandEvaluation;
import org.mafagafogigante.dungeon.commands.IssuedCommandProcessor;
import org.mafagafogigante.dungeon.gui.GameWindow;
import org.mafagafogigante.dungeon.io.Loader;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.logging.DungeonLogger;
import org.mafagafogigante.dungeon.util.StopWatch;
import org.mafagafogigante.dungeon.util.Utils;

import org.apache.commons.lang3.StringUtils;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Game {

  private static final InstanceInformation instanceInformation = new InstanceInformation();

  private static GameWindow gameWindow;
  private static GameState gameState;

  /**
   * The main method.
   */
  public static void main(String[] args) {
    final StopWatch stopWatch = new StopWatch();
    DungeonLogger.info("Started initializing Dungeon " + Version.getCurrentVersion() + ".");
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
    } catch (InterruptedException | InvocationTargetException fatal) {
      DungeonLogger.logSevere(fatal);
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
    Writer.write(new DungeonString("\nYou may want to issue 'tutorial' to learn the basics.\n"));
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
    Writer.write(new DungeonString("\n")); // Improves readability.
    gameState.getHero().look();
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
  public static void renderTurn(IssuedCommand issuedCommand, StopWatch stopWatch) {
    DungeonLogger.logCommandRenderingReport(issuedCommand.toString(), "started renderTurn", stopWatch);
    // Clears the text pane.
    getGameWindow().clearTextPane();
    DungeonLogger.logCommandRenderingReport(issuedCommand.toString(), "started processInput", stopWatch);
    boolean wasSuccessful = processInput(issuedCommand);
    DungeonLogger.logCommandRenderingReport(issuedCommand.toString(), "finished processInput", stopWatch);
    if (wasSuccessful) {
      if (getGameState().getHero().getHealth().isDead()) {
        getGameWindow().clearTextPane();
        Writer.write("You died.");
        unsetGameState();
        setGameState(getAfterDeathGameState());
      } else {
        Engine.endTurn();
      }
    }
    DungeonLogger.logCommandRenderingReport(issuedCommand.toString(), "finished renderTurn", stopWatch);
  }

  /**
   * Processes the player's input. Adds the IssuedCommand to the CommandHistory and to the CommandStatistics. Finally,
   * this method finds and executes the corresponding Command object or prints a message if there is not such Command.
   *
   * @param issuedCommand the last IssuedCommand.
   * @return a boolean indicating whether or not the command executed successfully
   */
  private static boolean processInput(IssuedCommand issuedCommand) {
    IssuedCommandEvaluation evaluation = IssuedCommandProcessor.evaluateIssuedCommand(issuedCommand);
    if (evaluation.isValid()) {
      instanceInformation.incrementAcceptedCommandCount();
      getGameState().getCommandHistory().addCommand(issuedCommand);
      getGameState().getStatistics().addCommand(issuedCommand);
      IssuedCommandProcessor.prepareIssuedCommand(issuedCommand).execute();
      return true;
    } else {
      DungeonString string = new DungeonString();
      string.setColor(Color.RED);
      string.append("That is not a valid command.\n");
      string.append("But it is similar to ");
      List<String> suggestionsBetweenCommas = new ArrayList<>();
      for (String suggestion : evaluation.getSuggestions()) {
        suggestionsBetweenCommas.add(StringUtils.wrap(suggestion, '"'));
      }
      string.append(Utils.enumerate(suggestionsBetweenCommas));
      string.append(".\n");
      string.setColor(Color.ORANGE);
      string.append("See 'commands' for a complete list of commands.");
      Writer.write(string);
      return false;
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
    StringBuilder builder = new StringBuilder();
    builder.append("Closing instance. Ran for ");
    builder.append(instanceInformation.getDurationString());
    builder.append(". ");
    if (instanceInformation.getAcceptedCommandCount() == 0) {
      builder.append("Parsed no commands.");
    } else if (instanceInformation.getAcceptedCommandCount() == 1) {
      builder.append("Parsed one command.");
    } else {
      builder.append("Parsed ");
      builder.append(instanceInformation.getAcceptedCommandCount());
      builder.append(" commands.");
    }
    DungeonLogger.info(builder.toString());
  }

}
