package org.mafagafogigante.dungeon.util;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class JsonElementSearchUtilTest {

  private static final String DEFAULT_ELEMENT_NAME_PREFIX = "element_";
  private static final String DEFAULT_CHILD_OBJECT_NAME_PREFIX = "child_";
  private static final String DEFAULT_CHILD_OF_CHILD_OBJECT_NAME_PREFIX = "child_child_";
  private static final int DEFAULT_NUMBER_OF_ELEMENTS = 5;

  private JsonObject parentJsonObject;

  /**
   * Initialize test JSON object.
   */
  @Before
  public void setUp() {
    parentJsonObject = buildJsonObjectWithNElements(DEFAULT_NUMBER_OF_ELEMENTS);
    JsonObject childObject = buildJsonObjectWithNElements(DEFAULT_NUMBER_OF_ELEMENTS);
    parentJsonObject.add(DEFAULT_CHILD_OBJECT_NAME_PREFIX + 1, childObject);
    parentJsonObject.add(DEFAULT_CHILD_OBJECT_NAME_PREFIX + 2, childObject);

    JsonObject parentOfChildObject = buildJsonObjectWithNElements(DEFAULT_NUMBER_OF_ELEMENTS);
    JsonObject childOfParentChildObject = buildJsonObjectWithNElements(DEFAULT_NUMBER_OF_ELEMENTS);
    parentOfChildObject.add(DEFAULT_CHILD_OF_CHILD_OBJECT_NAME_PREFIX + 1, childOfParentChildObject);
    parentOfChildObject.add(DEFAULT_CHILD_OF_CHILD_OBJECT_NAME_PREFIX + 2, childOfParentChildObject);
    parentJsonObject.add(DEFAULT_CHILD_OBJECT_NAME_PREFIX + 3, parentOfChildObject);

    JsonArray childArray = new JsonArray();
    childArray.add(buildJsonObjectWithNElements(DEFAULT_NUMBER_OF_ELEMENTS));
    parentJsonObject.add(DEFAULT_CHILD_OBJECT_NAME_PREFIX + 4, childArray);
  }

  @Test
  public void testJsonElementUtilFindsAllElementsInJsonObjectByAttributeName() {
    final int suffixValue = 2;
    List<JsonValue> jsonValueSearchResult = JsonElementSearchUtil
        .searchJsonValueByAttributeName(parentJsonObject, DEFAULT_ELEMENT_NAME_PREFIX + suffixValue);
    for (JsonValue jsonValue : jsonValueSearchResult) {
      Assert.assertEquals(suffixValue, jsonValue.asInt());
    }
    Assert.assertEquals(7, jsonValueSearchResult.size());
  }

  @Test
  public void testJsonElementUtilFindsChildElementInJsonObjectByAttributeName() {
    List<JsonValue> jsonValueSearchResult =
        JsonElementSearchUtil.searchJsonValueByAttributeName(parentJsonObject, DEFAULT_CHILD_OBJECT_NAME_PREFIX + 1);
    Assert.assertEquals(1, jsonValueSearchResult.size());
  }

  @Test
  public void testJsonElementUtilReturnsEmptyListIfAttributeNameDoesntExist() {
    List<JsonValue> jsonValueSearchResult =
        JsonElementSearchUtil.searchJsonValueByAttributeName(parentJsonObject, DEFAULT_CHILD_OBJECT_NAME_PREFIX);
    Assert.assertEquals(0, jsonValueSearchResult.size());
  }

  private JsonObject buildJsonObjectWithNElements(int nElements) {
    final JsonObject emptyJsonObject = new JsonObject();
    for (int i = 0; i <= nElements; i++) {
      emptyJsonObject.add(DEFAULT_ELEMENT_NAME_PREFIX + i, i);
    }
    return emptyJsonObject;
  }

}
