package org.mafagafogigante.dungeon.achievements;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.util.CounterMap;

import java.util.ArrayList;
import java.util.Collection;

class AchievementBuilder {

  private final Collection<BattleStatisticsRequirement> requirements = new ArrayList<>();
  private String id;
  private String name;
  private String info;
  private String text;
  private CounterMap<Id> killsByLocationId;
  private CounterMap<Id> visitedLocations;
  private CounterMap<Id> maximumNumberOfVisits;

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void addBattleStatisticsRequirement(BattleStatisticsRequirement requirement) {
    requirements.add(requirement);
  }

  public void setKillsByLocationId(CounterMap<Id> killsByLocationId) {
    if (killsByLocationId.isNotEmpty()) {
      this.killsByLocationId = killsByLocationId;
    }
  }

  public void setVisitedLocations(CounterMap<Id> visitedLocations) {
    if (visitedLocations.isNotEmpty()) {
      this.visitedLocations = visitedLocations;
    }
  }

  public void setMaximumNumberOfVisits(CounterMap<Id> maximumNumberOfVisits) {
    if (maximumNumberOfVisits.isNotEmpty()) {
      this.maximumNumberOfVisits = maximumNumberOfVisits;
    }
  }

  public Achievement createAchievement() {
    return new Achievement(id, name, info, text, requirements, killsByLocationId, visitedLocations,
        maximumNumberOfVisits);
  }

}
