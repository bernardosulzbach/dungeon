package org.mafagafogigante.dungeon.io;

import com.eclipsesource.json.JsonObject;

class ResourcesTypeTest {

  static final String WIKI_JSON_FILE_NAME = "wiki.json";
  static final String HINTS_JSON_FILE_NAME = "hints.json";
  static final String POEMS_JSON_FILE_NAME = "poems.json";
  static final String ITEMS_JSON_FILE_NAME = "items.json";
  static final String DREAMS_JSON_FILE_NAME = "dreams.json";
  static final String PREFACE_JSON_FILE_NAME = "preface.json";
  static final String TUTORIAL_JSON_FILE_NAME = "tutorial.json";
  static final String LOCATIONS_JSON_FILE_NAME = "locations.json";
  static final String CREATURES_JSON_FILE_NAME = "creatures.json";
  static final String ACHIEVEMENTS_JSON_FILE_NAME = "achievements.json";

  static JsonObject getJsonObjectByJsonFile(String jsonFileName) {
    return JsonObjectFactory.makeJsonObject(jsonFileName);
  }

}
