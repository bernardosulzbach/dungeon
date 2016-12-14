package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.schema.JsonObjectValidator;
import org.mafagafogigante.dungeon.schema.JsonRulesFactory;
import org.mafagafogigante.dungeon.schema.JsonValidationResult;
import org.mafagafogigante.dungeon.schema.TypeOfJsonValidationResult;

import com.eclipsesource.json.JsonObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AchievementsJsonResourceTest {

  private static final int COUNT_ATTRIBUTE_LOWER_BOUND = 0;
  private static final int COUNT_ATTRIBUTE_UPPER_BOUND = 100;
  private static final String ID_ATTRIBUTE_NAME = "id";
  private static final String COUNT_ATTRIBUTE_NAME = "count";
  private static final String ACHIEVEMENTS_RESOURCE_FILE_NAME = "achievements.json";

  private JsonObject achievementsJsonObject;

  /**
   * Initialize achievements json file.
   */
  @Before
  public void setUp() {
    achievementsJsonObject = JsonObjectFactory.makeJsonObject(ACHIEVEMENTS_RESOURCE_FILE_NAME);
  }

  @Test
  public void testThatIdValuesInResourceFileAreInUppercase() {
    JsonRulesFactory idValidationFactory =
        JsonRulesFactory.object(JsonRulesFactory.pair(ID_ATTRIBUTE_NAME, JsonRulesFactory.stringUppercaseJsonRule()));
    JsonObjectValidator jsonObjectIdAttributeValidator = new JsonObjectValidator(idValidationFactory);
    List<JsonValidationResult> jsonValidationResults = jsonObjectIdAttributeValidator.applyTo(achievementsJsonObject);
    handleJsonValidationResults(jsonValidationResults);
  }

  @Test
  public void testThatCountValuesInResourceFileAreInBoundOf0to100() {
    JsonRulesFactory countValidationFactory = JsonRulesFactory.object(JsonRulesFactory.pair(COUNT_ATTRIBUTE_NAME,
        JsonRulesFactory.boundIntegerJsonRule(COUNT_ATTRIBUTE_LOWER_BOUND, COUNT_ATTRIBUTE_UPPER_BOUND)));
    JsonObjectValidator jsonObjectCountAttributeValidator = new JsonObjectValidator(countValidationFactory);
    List<JsonValidationResult> jsonValidationResults =
        jsonObjectCountAttributeValidator.applyTo(achievementsJsonObject);
    handleJsonValidationResults(jsonValidationResults);
  }

  private void handleJsonValidationResults(List<JsonValidationResult> jsonValidationResults) {
    for (JsonValidationResult jsonValidationResult : jsonValidationResults) {
      boolean isFailed = !jsonValidationResult.getTypeOfJsonValidationResult().equals(TypeOfJsonValidationResult.VALID);
      if (isFailed) {
        Assert.fail(getErrorMessage(jsonValidationResult));
      }
    }
  }

  private String getErrorMessage(JsonValidationResult jsonValidationResult) {
    return getClass() + " found error in resource file. Value [" + jsonValidationResult.getElementValue() +
        "] failed with next validation result type [" + jsonValidationResult.getTypeOfJsonValidationResult() + "]";
  }

}
