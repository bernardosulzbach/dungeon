package org.mafagafogigante.dungeon.util;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.List;

public final class JsonElementSearchUtil {

  private JsonElementSearchUtil() {
    throw new AssertionError();
  }

  /**
   * Searches JSON values by attribute name in recursion.
   */
  public static List<JsonValue> searchJsonValueByAttributeName(JsonObject jsonObject, String elementNameForSearch) {
    List<JsonValue> resultJsonValues = new ArrayList<>();
    recursionJsonValueSearchByAttributeName(resultJsonValues, jsonObject, elementNameForSearch);
    return resultJsonValues;
  }

  private static List<JsonValue> recursionJsonValueSearchByAttributeName(List<JsonValue> accumulationJsonValueList,
      JsonObject jsonObject, String jsonElementName) {
    for (String name : jsonObject.names()) {
      JsonValue jsonValue = jsonObject.get(name);
      if (name.equals(jsonElementName)) {
        accumulationJsonValueList.add(jsonValue);
      } else if (jsonValue.isObject()) {
        recursionJsonValueSearchByAttributeName(accumulationJsonValueList, jsonValue.asObject(), jsonElementName);
      } else if (jsonValue.isArray()) {
        List<JsonValue> arrayValues = jsonValue.asArray().values();
        for (JsonValue arrayValue : arrayValues) {
          recursionJsonValueSearchByAttributeName(accumulationJsonValueList, arrayValue.asObject(), jsonElementName);
        }
      }
    }
    return accumulationJsonValueList;
  }

}
