package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.Random;
import org.mafagafogigante.dungeon.io.Version;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class EnchantmentRules implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;

  private final Map<Id, Double> rules = new HashMap<>();

  public void add(Id id, double probability) {
    rules.put(id, probability);
  }

  List<Id> randomRoll() {
    List<Id> chosenRules = new ArrayList<>();
    for (Entry<Id, Double> rule : rules.entrySet()) {
      if (Random.roll(rule.getValue())) {
        chosenRules.add(rule.getKey());
      }
    }
    return chosenRules;
  }

}
