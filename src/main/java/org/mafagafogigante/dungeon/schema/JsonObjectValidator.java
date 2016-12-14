package org.mafagafogigante.dungeon.schema;

import org.mafagafogigante.dungeon.util.JsonElementSearchUtil;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.util.ArrayList;
import java.util.List;

public final class JsonObjectValidator {

  private final JsonRulesFactory jsonRulesFactory;

  public JsonObjectValidator(JsonRulesFactory jsonRulesFactory) {
    this.jsonRulesFactory = jsonRulesFactory;
  }

  /**
   * Applies JSON rule pairs to json object and returns validation result list.
   */
  public List<JsonValidationResult> applyTo(JsonObject sourceJsonObject) {
    final List<JsonRulePair> jsonRulePairs = jsonRulesFactory.getJsonRulePairs();
    List<JsonValidationResult> jsonValidationResults = new ArrayList<>();
    for (JsonRulePair jsonRulePair : jsonRulePairs) {
      final String targetElementName = jsonRulePair.getTargetElementName();
      List<JsonValue> sourceJsonObjectValues =
          JsonElementSearchUtil.searchJsonValueByAttributeName(sourceJsonObject, targetElementName);
      for (JsonValue sourceJsonValue : sourceJsonObjectValues) {
        JsonValidationResult jsonValidationResult = jsonRulePair.getJsonRule().validate(sourceJsonValue);
        jsonValidationResults.add(jsonValidationResult);
      }
    }
    return jsonValidationResults;
  }

}
