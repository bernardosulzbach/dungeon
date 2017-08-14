package org.mafagafogigante.dungeon.io;

public enum DungeonResource {

  ACHIEVEMENTS("achievements.json"), CREATURES("creatures.json"), DREAMS("dreams.json"),
  ENCHANTMENTS("enchantments.json"), HINTS("hints.json"), ITEMS("items.json"), LOCATIONS("locations.json"),
  POEMS("poems.json"), PREFACE("preface.json"), TUTORIAL("tutorial.json"), WIKI("wiki.json");

  private static final long serialVersionUID = Version.MAJOR;

  private final String filename;

  DungeonResource(String filename) {
    this.filename = filename;
  }

  String getFilename() {
    return filename;
  }

}
