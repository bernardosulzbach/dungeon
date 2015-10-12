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

package org.dungeon.entity;

import org.dungeon.game.Id;
import org.dungeon.game.Name;
import org.dungeon.util.Selectable;

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
