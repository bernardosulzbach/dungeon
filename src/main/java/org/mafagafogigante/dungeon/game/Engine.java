package org.mafagafogigante.dungeon.game;

import org.mafagafogigante.dungeon.achievements.AchievementStoreFactory;
import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.entity.creatures.Creature;
import org.mafagafogigante.dungeon.entity.creatures.Hero;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.io.Writer;
import org.mafagafogigante.dungeon.util.StandardRichTextBuilder;
import org.mafagafogigante.dungeon.util.Utils;

import java.awt.Color;
import java.io.Serializable;

public class Engine implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private static final int BATTLE_TURN_DURATION = 30;
  private final GameState gameState;

  Engine(GameState gameState) {
    this.gameState = gameState;
  }

  /**
   * Refreshes the game.
   *
   * <p>This method should be called whenever the state of the game is changed and the engine should be updated.
   *
   * <p>If time passed, use {@link Engine#rollDateAndRefresh(long)}.
   */
  public void refresh() {
    effectivelyUpdate(0);
  }

  /**
   * Rolls the world date forward and refreshes the game.
   *
   * <p>This method should be called whenever the state of the game is changed and the engine should be updated.
   *
   * <p>If no time passed, use {@link Engine#refresh()}.
   *
   * @param seconds how many seconds to roll the date forward, a positive integer
   */
  public void rollDateAndRefresh(long seconds) {
    if (seconds <= 0) {
      throw new IllegalArgumentException("seconds should be positive.");
    }
    effectivelyUpdate(seconds);
  }

  /**
   * Effectively updates the game. Rolls time forward before silently refreshing the game.
   *
   * @param seconds how many seconds to roll the date forward, nonnegative
   */
  private void effectivelyUpdate(long seconds) {
    if (seconds < 0) {
      throw new IllegalArgumentException("seconds should be nonnegative.");
    }
    if (seconds > 0) {
      gameState.getWorld().rollDate(seconds);
    }
    silentRefresh();
    notifyGameStateModification();
  }

  /**
   * Sets the status of the GameState to unsaved.
   */
  private void notifyGameStateModification() {
    gameState.setSaved(false);
  }

  /**
   * Silently refreshes the game. Does not produce any visible textual output.
   */
  private void silentRefresh() {
    refreshSpawners();
    refreshItems();
  }

  /**
   * Ends the turn, refreshing the game state and checking if any achievements were unlocked.
   */
  void endTurn() {
    silentRefresh();
    refreshAchievements();
  }

  private void refreshSpawners() {
    gameState.getHero().getLocation().refreshSpawners();
  }

  private void refreshItems() {
    gameState.getHero().getLocation().refreshItems();
  }

  private void refreshAchievements() {
    Date worldDate = gameState.getWorld().getWorldDate();
    gameState.getHero().getAchievementTracker().update(AchievementStoreFactory.getDefaultStore(), worldDate);
  }

  /**
   * Simulates a battle between the hero and a creature.
   *
   * @param hero the attacker
   * @param foe the defender
   */
  public void battle(Hero hero, Creature foe) {
    if (hero == foe) {
      Writer.getDefaultWriter().write(new StandardRichTextBuilder().append("You cannot attempt suicide.").toRichText());
      return;
    }
    while (hero.getHealth().isAlive() && foe.getHealth().isAlive()) {
      hero.hit(foe);
      rollDateAndRefresh(BATTLE_TURN_DURATION);
      // No contract specifies that calling hit on the Hero will not kill it, so check both creatures again.
      // Additionally, rolling the date forward may kill the hero in the future.
      if (hero.getHealth().isAlive() && foe.getHealth().isAlive()) {
        foe.hit(hero);
        rollDateAndRefresh(BATTLE_TURN_DURATION);
      }
    }
    Creature survivor = hero.getHealth().isAlive() ? hero : foe;
    Creature defeated = (survivor == hero) ? foe : hero;
    // Imagine if a third factor (such as hunger) could kill one of the creatures.
    // I think it still makes sense to say that the survivor managed to kill the defeated, but that's just me.
    StandardRichTextBuilder builder = new StandardRichTextBuilder();
    builder.setColor(Color.CYAN);
    builder.append(survivor.getName().getSingular());
    builder.append(" managed to kill ");
    builder.append(defeated.getName().getSingular());
    builder.append(".\n");
    Writer.getDefaultWriter().write(builder.toRichText());
    writeDrops(defeated);
    if (hero == survivor) {
      PartOfDay partOfDay = PartOfDay.getCorrespondingConstant(gameState.getWorld().getWorldDate());
      gameState.getStatistics().getBattleStatistics().addBattle(foe, defeated.getCauseOfDeath(), partOfDay);
      gameState.getStatistics().getExplorationStatistics().addKill(hero.getLocation().getPoint());
      long heroTakenDamage = hero.getBattleLog().getAndResetTaken();
      long heroInflictedDamage = hero.getBattleLog().getAndResetInflicted();
      gameState.getStatistics().getHeroStatistics().incrementDamageTaken(heroTakenDamage);
      gameState.getStatistics().getHeroStatistics().incrementDamageInflicted(heroInflictedDamage);
    }
  }

  /**
   * Writes a message with what the creature dropped if it dropped something.
   *
   * @param source a dead Creature
   */
  private void writeDrops(Creature source) {
    if (!source.getDroppedItemsList().isEmpty()) {
      StandardRichTextBuilder builder = new StandardRichTextBuilder();
      builder.append(source.getName().getSingular() + " dropped ");
      builder.append(Utils.enumerateEntities(source.getDroppedItemsList()));
      builder.append(".\n");
      Writer.getDefaultWriter().write(builder.toRichText());
    }
  }

}
