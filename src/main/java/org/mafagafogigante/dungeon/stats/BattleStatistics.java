package org.mafagafogigante.dungeon.stats;

import org.mafagafogigante.dungeon.achievements.BattleStatisticsRequirement;
import org.mafagafogigante.dungeon.entity.creatures.Creature;
import org.mafagafogigante.dungeon.game.PartOfDay;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.CounterMap;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * BattleStatistics class that stores battle statistics to enable achievements.
 */
public class BattleStatistics implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final CounterMap<BattleRecord> records = new CounterMap<>();

  /**
   * Adds the outcome of a battle to the statistics.
   *
   * @param foe the defeated Creature, not null
   * @param causeOfDeath the CauseOfDeath, not null
   * @param partOfDay the PartOfDay in which the last hit took place, not null
   */
  public void addBattle(@NotNull Creature foe, @NotNull CauseOfDeath causeOfDeath, @NotNull PartOfDay partOfDay) {
    BattleRecord record = new BattleRecord(foe.getId(), foe.getType(), causeOfDeath, partOfDay);
    records.incrementCounter(record);
  }

  /**
   * Returns a CounterMap of CauseOfDeath representing how many times each CauseOfDeath already registered occurred.
   */
  public CounterMap<CauseOfDeath> getKillsByCauseOfDeath() {
    CounterMap<CauseOfDeath> causeOfDeathCounterMap = new CounterMap<>();
    for (BattleRecord record : records.keySet()) {
      causeOfDeathCounterMap.incrementCounter(record.getCauseOfDeath(), records.getCounter(record));
    }
    return causeOfDeathCounterMap;
  }

  /**
   * Evaluates if this BattleStatistics satisfies a BattleStatisticsRequirement.
   */
  public boolean satisfies(BattleStatisticsRequirement requirement) {
    int count = 0;
    for (BattleRecord record : records.keySet()) {
      if (requirement.getQuery().matches(record)) {
        count += records.getCounter(record);
        if (count >= requirement.getCount()) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("BattleStatistics{records=%s}", records);
  }

}
