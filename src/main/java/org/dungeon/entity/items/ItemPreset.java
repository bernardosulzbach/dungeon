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

package org.dungeon.entity.items;

import org.dungeon.entity.Integrity;
import org.dungeon.entity.Luminosity;
import org.dungeon.entity.Preset;
import org.dungeon.entity.TagSet;
import org.dungeon.entity.Visibility;
import org.dungeon.entity.Weight;
import org.dungeon.game.Id;
import org.dungeon.game.Name;
import org.dungeon.util.Percentage;

/**
 * Stores the information about an item that the factory may need to create it.
 */
public final class ItemPreset implements Preset {

  private final TagSet<Item.Tag> tagSet = TagSet.makeEmptyTagSet(Item.Tag.class);
  private Id id;
  private String type;
  private Name name;
  private Integrity integrity;
  private int damage;
  private Percentage hitRate;
  private int integrityDecrementOnHit;
  private int nutrition;
  private int integrityDecrementOnEat;
  private Weight weight;
  private Visibility visibility;
  private Luminosity luminosity = Luminosity.ZERO;
  private Id skill;
  private String text;
  private long putrefactionPeriod;

  public TagSet<Item.Tag> getTagSet() {
    return tagSet;
  }

  public boolean hasTag(Item.Tag tag) {
    return getTagSet().hasTag(tag);
  }

  public void addTag(Item.Tag tag) {
    getTagSet().addTag(tag);
  }

  public Id getId() {
    return id;
  }

  public void setId(Id id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Name getName() {
    return name;
  }

  public void setName(Name name) {
    this.name = name;
  }

  public Integrity getIntegrity() {
    return integrity;
  }

  public void setIntegrity(Integrity integrity) {
    this.integrity = integrity;
  }

  public int getDamage() {
    return damage;
  }

  public void setDamage(int damage) {
    this.damage = damage;
  }

  public Percentage getHitRate() {
    return hitRate;
  }

  public void setHitRate(Percentage hitRate) {
    this.hitRate = hitRate;
  }

  public int getIntegrityDecrementOnHit() {
    return integrityDecrementOnHit;
  }

  public void setIntegrityDecrementOnHit(int integrityDecrementOnHit) {
    this.integrityDecrementOnHit = integrityDecrementOnHit;
  }

  public int getNutrition() {
    return nutrition;
  }

  public void setNutrition(int nutrition) {
    this.nutrition = nutrition;
  }

  public int getIntegrityDecrementOnEat() {
    return integrityDecrementOnEat;
  }

  public void setIntegrityDecrementOnEat(int integrityDecrementOnEat) {
    this.integrityDecrementOnEat = integrityDecrementOnEat;
  }

  @Override
  public Weight getWeight() {
    return weight;
  }

  public void setWeight(Weight weight) {
    this.weight = weight;
  }

  @Override
  public Visibility getVisibility() {
    return visibility;
  }

  public void setVisibility(Visibility visibility) {
    this.visibility = visibility;
  }

  public Luminosity getLuminosity() {
    return luminosity;
  }

  public void setLuminosity(Luminosity luminosity) {
    this.luminosity = luminosity;
  }

  public Id getSkill() {
    return skill;
  }

  public void setSkill(String skill) {
    this.skill = new Id(skill);
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public long getPutrefactionPeriod() {
    return putrefactionPeriod;
  }

  public void setPutrefactionPeriod(long putrefactionPeriod) {
    this.putrefactionPeriod = putrefactionPeriod;
  }

  @Override
  public String toString() {
    return "ItemPreset{" +
        "id=" + id +
        ", type='" + type + '\'' +
        ", name=" + name +
        ", putrefactionPeriod=" + putrefactionPeriod +
        ", tagSet=" + tagSet +
        ", integrity=" + integrity +
        ", damage=" + damage +
        ", hitRate=" + hitRate +
        ", integrityDecrementOnHit=" + integrityDecrementOnHit +
        ", nutrition=" + nutrition +
        ", integrityDecrementOnEat=" + integrityDecrementOnEat +
        ", text='" + text + '\'' +
        ", weight=" + weight +
        ", visibility=" + visibility +
        ", skill=" + skill +
        ", luminosity=" + luminosity +
        '}';
  }

}
