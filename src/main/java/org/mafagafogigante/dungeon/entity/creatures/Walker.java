package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.game.Direction;
import org.mafagafogigante.dungeon.game.DungeonString;
import org.mafagafogigante.dungeon.game.Engine;
import org.mafagafogigante.dungeon.game.Game;
import org.mafagafogigante.dungeon.game.GameState;
import org.mafagafogigante.dungeon.game.Point;
import org.mafagafogigante.dungeon.game.World;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.stats.ExplorationStatistics;

import java.awt.Color;
import java.io.Serializable;

/**
 * A Walker that is capable of moving between Locations. The Hero needs a Walker component in order to move around.
 */
class Walker implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private static final int WALK_BLOCKED = 2;
  private static final int WALK_SUCCESS = 200;

  /**
   * Parses an issued command to move the player.
   */
  public void parseHeroWalk(String[] arguments) {
    if (arguments.length != 0) {
      for (Direction dir : Direction.values()) {
        if (dir.equalsIgnoreCase(arguments[0])) {
          heroWalk(dir);
          return;
        }
      }
      Writer.write("Invalid input.");
    } else {
      Writer.write(new DungeonString("To where?", Color.ORANGE));
    }
  }

  /**
   * Attempts to move the hero in a given direction.
   *
   * <p>If the hero moves, this method refreshes both locations at the latest date. If the hero does not move, this
   * method refreshes the location where the hero is.
   */
  private void heroWalk(Direction dir) {
    GameState gameState = Game.getGameState();
    World world = gameState.getWorld();
    Point point = gameState.getHero().getLocation().getPoint();
    Point destinationPoint = new Point(point, dir);
    // This order is important. Calling .getLocation may trigger location creation, so avoid creating a location that we
    // don't need if we can't get there.
    if (world.getLocation(point).isBlocked(dir) || world.getLocation(destinationPoint).isBlocked(dir.invert())) {
      Engine.rollDateAndRefresh(WALK_BLOCKED); // The hero tries to go somewhere.
      Writer.write("You cannot go " + dir + ".");
    } else {
      Hero hero = gameState.getHero();
      Engine.rollDateAndRefresh(WALK_SUCCESS); // Time spent walking.
      hero.getLocation().removeCreature(hero);
      world.getLocation(destinationPoint).addCreature(hero);
      gameState.setHeroPosition(destinationPoint);
      Engine.refresh(); // Hero arrived in a new location, refresh the game.
      hero.look();
      updateExplorationStatistics(destinationPoint);
    }
  }

  private void updateExplorationStatistics(Point destination) {
    ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
    explorationStatistics.addVisit(destination, Game.getGameState().getWorld().getLocation(destination).getId());
  }

}
