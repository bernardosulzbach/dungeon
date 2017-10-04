package org.mafagafogigante.dungeon.io;

import com.eclipsesource.json.JsonObject;

class ResourcesTypeTest {

  static JsonObject getJsonObjectByJsonFilename(String filename) {
    return JsonObjectFactory.makeJsonObject(filename);
  }

}
