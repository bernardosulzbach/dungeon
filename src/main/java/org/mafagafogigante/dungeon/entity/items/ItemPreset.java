package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.entity.Integrity;
import org.mafagafogigante.dungeon.entity.Luminosity;
import org.mafagafogigante.dungeon.entity.Preset;
import org.mafagafogigante.dungeon.entity.TagSet;
import org.mafagafogigante.dungeon.entity.Weight;
import org.mafagafogigante.dungeon.entity.items.Item.Tag;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.Name;
import org.mafagafogigante.dungeon.util.Percentage;

import java.io.Serializable;

/**
 * Stores the information about an item that the factory may need to create it.
 */
public final class ItemPreset implements Preset, Serializable {

  private final TagSet<Tag> tagSet = TagSet.makeEmptyTagSet(Item.Tag.class);
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
  private Percentage visibility;
  private Luminosity luminosity = Luminosity.ZERO;
  private Id spellId;
  private String text;
  private long putrefactionPeriod;
  private boolean unique;

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
  public Percentage getVisibility() {
    return visibility;
  }

  public void setVisibility(Percentage visibility) {
    this.visibility = visibility;
  }

  public Luminosity getLuminosity() {
    return luminosity;
  }

  public void setLuminosity(Luminosity luminosity) {
    this.luminosity = luminosity;
  }

  public Id getSpellId() {
    return spellId;
  }

  public void setSpellId(String spellIdString) {
    this.spellId = new Id(spellIdString);
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

  public boolean isUnique() {
    return unique;
  }

  public void setUnique(boolean unique) {
    this.unique = unique;
  }

}
