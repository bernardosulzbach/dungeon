package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.Name;
import org.mafagafogigante.dungeon.util.Selectable;

import java.io.Serializable;

/**
 * Entity abstract class that is a common type for everything that can be placed into a Location and interacted with.
 * Namely, items and creatures.
 */
public abstract class Entity implements Selectable, Serializable {

  private final Id id;
  private final String type;
  private final Name name;
  private final Weight weight;
  private final Visibility visibility;

  protected Entity(Preset preset) {
    this.id = preset.getId();
    this.type = preset.getType();
    this.name = preset.getName();
    this.weight = preset.getWeight();
    this.visibility = preset.getVisibility();
  }

  public Id getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  @Override
  public Name getName() {
    return name;
  }

  protected Weight getWeight() {
    return weight;
  }

  public Visibility getVisibility() {
    return visibility;
  }

  /**
   * Returns the total luminosity of this entity. This accounts for its innate luminosity plus the luminosity from any
   * items it may be holding.
   */
  public abstract Luminosity getLuminosity();

}
