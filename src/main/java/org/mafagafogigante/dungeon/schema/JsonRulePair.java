package org.mafagafogigante.dungeon.schema;

public class JsonRulePair {

  private final String targetElementName;
  private final JsonRule jsonRule;

  public JsonRulePair(String targetElementName, JsonRule jsonRule) {
    this.targetElementName = targetElementName;
    this.jsonRule = jsonRule;
  }

  public String getTargetElementName() {
    return targetElementName;
  }

  public JsonRule getJsonRule() {
    return jsonRule;
  }

}
