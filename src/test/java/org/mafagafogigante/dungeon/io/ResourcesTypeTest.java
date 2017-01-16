package org.mafagafogigante.dungeon.io;

import com.eclipsesource.json.JsonObject;

class ResourcesTypeTest {

  static JsonObject getJsonObjectByJsonFile(JsonFileEnum jsonFile) {
    return JsonObjectFactory.makeJsonObject(jsonFile.getJsonFileName());
  }

  enum JsonFileEnum {

    ACHIEVEMENTS("achievements.json"), LOCATIONS("locations.json"), CREATURES("cratures.json"),
    TUTORIAL("tutorial.json"), PREFACE("preface.json"), DREAMS("locations.json"), HINTS("hints.json"),
    ITEMS("items.json"), POEMS("poems.json"), WIKI("wiki.json");

    private final String jsonFileName;

    JsonFileEnum(String jsonFileName) {
      this.jsonFileName = jsonFileName;
    }

    public String getJsonFileName() {
      return jsonFileName;
    }
  }

}
