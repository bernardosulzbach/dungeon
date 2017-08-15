package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.entity.Integrity;
import org.mafagafogigante.dungeon.entity.Luminosity;
import org.mafagafogigante.dungeon.entity.Preset;
import org.mafagafogigante.dungeon.entity.TagSet;
import org.mafagafogigante.dungeon.entity.Weight;
import org.mafagafogigante.dungeon.entity.creatures.Effect;
import org.mafagafogigante.dungeon.entity.creatures.EffectFactory;
import org.mafagafogigante.dungeon.entity.items.Item.Tag;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.game.Name;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.Percentage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores the information about an item that the factory may need to create it.
 */
public final class ItemPreset implements Preset, Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final List<Effect> drinkableEffects = new ArrayList<>();
  private Id id;
  private String type;
  private Name name;
  private Rarity rarity;
  private TagSet<Tag> tagSet = TagSet.makeEmptyTagSet(Item.Tag.class);
  private EnchantmentRules enchantmentRules = new EnchantmentRules();
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
  private int drinkableDoses;
  private int integrityDecrementPerDose;

  List<Effect> getDrinkableEffects() {
    return drinkableEffects;
  }

  void addDrinkableEffect(Id effectId, List<String> effectParameters) {
    drinkableEffects.add(EffectFactory.getDefaultFactory().getEffect(effectId, effectParameters));
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

  Rarity getRarity() {
    return rarity;
  }

  public void setRarity(Rarity rarity) {
    this.rarity = rarity;
  }

  public TagSet<Item.Tag> getTagSet() {
    return tagSet;
  }

  void setTagSet(TagSet<Tag> tagSet) {
    this.tagSet = tagSet;
  }

  EnchantmentRules getEnchantmentRules() {
    return enchantmentRules;
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

  Percentage getHitRate() {
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

  int getNutrition() {
    return nutrition;
  }

  void setNutrition(int nutrition) {
    this.nutrition = nutrition;
  }

  int getIntegrityDecrementOnEat() {
    return integrityDecrementOnEat;
  }

  void setIntegrityDecrementOnEat(int integrityDecrementOnEat) {
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

  Id getSpellId() {
    return spellId;
  }

  void setSpellId(String spellIdString) {
    this.spellId = new Id(spellIdString);
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  long getPutrefactionPeriod() {
    return putrefactionPeriod;
  }

  public void setPutrefactionPeriod(long putrefactionPeriod) {
    this.putrefactionPeriod = putrefactionPeriod;
  }

  boolean isUnique() {
    return unique;
  }

  void setUnique(boolean unique) {
    this.unique = unique;
  }

  int getIntegrityDecrementPerDose() {
    return integrityDecrementPerDose;
  }

  void setIntegrityDecrementPerDose(int integrityDecrementPerDose) {
    this.integrityDecrementPerDose = integrityDecrementPerDose;
  }

  int getDrinkableDoses() {
    return drinkableDoses;
  }

  void setDrinkableDoses(int doses) {
    this.drinkableDoses = doses;
  }

}
