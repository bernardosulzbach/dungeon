package org.mafagafogigante.dungeon.schema;

public class JsonValidationResult {

  private final String elementValue;
  private final TypeOfJsonValidationResult typeOfJsonValidationResult;

  public JsonValidationResult(String elementValue, TypeOfJsonValidationResult typeOfJsonValidationResult) {
    this.elementValue = elementValue;
    this.typeOfJsonValidationResult = typeOfJsonValidationResult;
  }

  public String getElementValue() {
    return elementValue;
  }

  public TypeOfJsonValidationResult getTypeOfJsonValidationResult() {
    return typeOfJsonValidationResult;
  }

}
