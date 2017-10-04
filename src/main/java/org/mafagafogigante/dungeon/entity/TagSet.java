package org.mafagafogigante.dungeon.entity;

import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.logging.DungeonLogger;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;

/**
 * A set of Tags.
 */
public class TagSet<E extends Enum<E>> implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private final Set<E> set;

  private TagSet(Set<E> set) {
    this.set = set;
  }

  /**
   * Returns an empty TagSet.
   *
   * @param enumClass the Class of the Enum type
   * @param <E> an Enum type
   * @return a new TagSet
   */
  public static <E extends Enum<E>> TagSet<E> makeEmptyTagSet(Class<E> enumClass) {
    return new TagSet<>(EnumSet.noneOf(enumClass));
  }

  /**
   * Returns a copy of the specified TagSet such that Tags can be added without affecting the original TagSet.
   *
   * @param tagSet the original TagSet
   * @param <E> an Enum type
   * @return a new TagSet
   */
  public static <E extends Enum<E>> TagSet<E> copyTagSet(TagSet<E> tagSet) {
    return new TagSet<>(EnumSet.copyOf(tagSet.set));
  }

  /**
   * Creates a TagSet from a JSON array.
   *
   * @param enumClass the Class of the enum, not null
   * @param array a JsonArray object, not null
   * @param <E> an Enum type
   * @return a TagSet
   */
  public static <E extends Enum<E>> TagSet<E> fromJsonArray(@NotNull JsonArray array, @NotNull Class<E> enumClass) {
    TagSet<E> tagSet = makeEmptyTagSet(enumClass);
    for (JsonValue value : array) {
      try {
        tagSet.addTag(Enum.valueOf(enumClass, value.asString()));
      } catch (IllegalArgumentException fatal) {
        throw new InvalidTagException("invalid tag '" + value.asString() + "' found.", fatal);
      }
    }
    return tagSet;
  }

  /**
   * Checks if this TagSet has a specified Tag.
   *
   * @param tag the Tag object
   * @return true if this set contains the specified Tag
   */
  public boolean hasTag(E tag) {
    return set.contains(tag);
  }

  /**
   * Adds a Tag to this TagSet.
   */
  public void addTag(E tag) {
    if (!set.add(tag)) {
      DungeonLogger.warning("Tried to add a Tag that was already in the TagSet.");
    }
  }

  @Override
  public String toString() {
    return "TagSet{" + "set=" + set + '}';
  }

  public static class InvalidTagException extends IllegalArgumentException {
    public InvalidTagException(String message) {
      super(message);
    }

    public InvalidTagException(String message, Throwable cause) {
      super(message, cause);
    }
  }

}
