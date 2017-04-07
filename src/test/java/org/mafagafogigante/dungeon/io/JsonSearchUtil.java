package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.game.Id;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

final class JsonSearchUtil {

  static Set<JsonValue> searchJsonValuesByPath(String path, JsonObject object) {
    Set<JsonValue> accResult = new HashSet<>();
    Queue<String> paths = new LinkedList<>(Arrays.asList(path.split("\\.")));
    return recursionSearch(paths, object, accResult);
  }

  static Set<Id> convertJsonValuesToDungeonIds(Set<JsonValue> idJsonValues) {
    Set<Id> ids = new HashSet<>();
    for (JsonValue idJsonValue : idJsonValues) {
      ids.add(new Id(idJsonValue.asString()));
    }
    return ids;
  }

  private static Set<JsonValue> recursionSearch(Queue<String> paths, JsonObject object, Set<JsonValue> accResult) {
    String fieldName = paths.poll();
    JsonValue currentValue = object.get(fieldName);
    if (currentValue != null) {
      if (paths.isEmpty()) {
        accResult.add(currentValue);
      } else {
        if (currentValue.isArray()) {
          JsonArray jsonArray = currentValue.asArray();
          for (JsonValue jsonValue : jsonArray) {
            recursionSearch(new LinkedList<>(paths), jsonValue.asObject(), accResult);
          }
        } else {
          recursionSearch(new LinkedList<>(paths), currentValue.asObject(), accResult);
        }
      }
    }
    return accResult;
  }

}
