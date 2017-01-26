package org.mafagafogigante.dungeon.io;

import com.eclipsesource.json.JsonObject;

class ResourcesTypeTest {

  static JsonObject getJsonObjectByJsonFile(String jsonFileName) {
    return JsonObjectFactory.makeJsonObject(jsonFileName);
  }

}
