package org.mafagafogigante.dungeon.world;

import org.mafagafogigante.dungeon.game.World;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

class SimpleAstronomicalBody implements AstronomicalBody, Serializable {

  private final World world;
  private final String description;
  private final Collection<VisibilityRule> visibilityRules;

  SimpleAstronomicalBody(World world, String description, VisibilityRule... rules) {
    this.world = world;
    this.description = description;
    this.visibilityRules = new ArrayList<>(Arrays.asList(rules));
  }

  @Override
  public boolean isVisible() {
    for (VisibilityRule visibilityRule : visibilityRules) {
      if (!visibilityRule.isRespected(world)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String describeYourself() {
    return description;
  }

  @Override
  public String toString() {
    return description;
  }

}
