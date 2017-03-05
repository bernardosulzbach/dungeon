package org.mafagafogigante.dungeon.io;

import org.mafagafogigante.dungeon.entity.TagSet;
import org.mafagafogigante.dungeon.entity.TagSet.InvalidTagException;

import com.eclipsesource.json.JsonValue;

/**
 * A parser for TagSets from JSON objects.
 */
public class TagSetParser<E extends Enum<E>> {

  private final Class<E> enumClass;
  private final JsonValue jsonValue;

  /**
   * Creates a TagSetParser for a JsonObject which must be either null or a JsonArray of uppercase strings.
   */
  public TagSetParser(Class<E> enumClass, JsonValue jsonValue) {
    this.enumClass = enumClass;
    this.jsonValue = jsonValue;
  }

  /**
   * Effectively parses the JsonValue and produces a TagSet.
   */
  public TagSet<E> parse() {
    TagSet<E> tagSet = TagSet.makeEmptyTagSet(enumClass);
    if (jsonValue != null) {
      if (jsonValue.isArray()) {
        for (JsonValue value : jsonValue.asArray()) {
          if (value.isString()) {
            String tag = value.asString();
            try {
              tagSet.addTag(Enum.valueOf(enumClass, tag));
            } catch (IllegalArgumentException fatal) {
              // Guarantee that bugged resource files are not going to make it to a release.
              String message = "invalid tag '" + tag + "' found.";
              throw new InvalidTagException(message, fatal);
            }
          } else {
            throw new InvalidTagException("tag value is not a string.");
          }
        }
      } else {
        throw new InvalidTagException("JsonObject is neither null or an array");
      }
    }
    return tagSet;
  }

}
