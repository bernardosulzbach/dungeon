package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.game.Name;
import org.mafagafogigante.dungeon.game.NameFactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchesTest {

  @Test
  public void fromCollectionShouldPreserveAllElements() throws Exception {
    List<WrappedString> strings = Arrays.asList(new WrappedString("A"), new WrappedString("B"));
    Matches<WrappedString> matches = Matches.fromCollection(strings);
    Assertions.assertEquals(strings.size(), matches.size());
    for (WrappedString wrappedString : strings) {
      Assertions.assertTrue(matches.hasMatchWithName(wrappedString.getName()));
    }
  }

  @Test
  public void toListShouldOutputAllElements() throws Exception {
    List<WrappedString> strings = Arrays.asList(new WrappedString("A"), new WrappedString("B"));
    Matches<WrappedString> matches = Matches.fromCollection(strings);
    for (WrappedString wrappedString : matches.toList()) {
      Assertions.assertTrue(strings.contains(wrappedString));
    }
  }

  @Test
  public void sizeShouldBeTheSameAsTheCollectionSize() throws Exception {
    List<WrappedString> strings = new ArrayList<>();
    Assertions.assertEquals(strings.size(), Matches.fromCollection(strings).size());
    strings.add(new WrappedString("A"));
    Assertions.assertEquals(strings.size(), Matches.fromCollection(strings).size());
    strings.add(new WrappedString("A"));
    Assertions.assertEquals(strings.size(), Matches.fromCollection(strings).size());
  }

  @Test
  public void getDifferentNamesShouldReturnTheRightCount() throws Exception {
    List<WrappedString> strings = new ArrayList<>();
    Assertions.assertEquals(0, Matches.fromCollection(strings).getDifferentNames());
    strings.add(new WrappedString("A"));
    Assertions.assertEquals(1, Matches.fromCollection(strings).getDifferentNames());
    strings.add(new WrappedString("A"));
    Assertions.assertEquals(1, Matches.fromCollection(strings).getDifferentNames());
    strings.add(new WrappedString("B"));
    Assertions.assertEquals(2, Matches.fromCollection(strings).getDifferentNames());
  }

  class WrappedString implements Selectable {
    private final Name name;

    WrappedString(String name) {
      this.name = NameFactory.newInstance(name);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      WrappedString that = (WrappedString) o;

      return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
      return name != null ? name.hashCode() : 0;
    }

    @Override
    public Name getName() {
      return name;
    }
  }

}
