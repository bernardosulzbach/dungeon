package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.game.Name;
import org.mafagafogigante.dungeon.game.NameFactory;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchesTest {

  @Test
  public void fromCollectionShouldPreserveAllElements() throws Exception {
    List<WrappedString> strings = Arrays.asList(new WrappedString("A"), new WrappedString("B"));
    Matches<WrappedString> matches = Matches.fromCollection(strings);
    Assert.assertEquals(strings.size(), matches.size());
    for (WrappedString wrappedString : strings) {
      Assert.assertTrue(matches.hasMatchWithName(wrappedString.getName()));
    }
  }

  @Test
  public void toListShouldOutputAllElements() throws Exception {
    List<WrappedString> strings = Arrays.asList(new WrappedString("A"), new WrappedString("B"));
    Matches<WrappedString> matches = Matches.fromCollection(strings);
    for (WrappedString wrappedString : matches.toList()) {
      Assert.assertTrue(strings.contains(wrappedString));
    }
  }

  @Test
  public void sizeShouldBeTheSameAsTheCollectionSize() throws Exception {
    List<WrappedString> strings = new ArrayList<>();
    Assert.assertEquals(strings.size(), Matches.fromCollection(strings).size());
    strings.add(new WrappedString("A"));
    Assert.assertEquals(strings.size(), Matches.fromCollection(strings).size());
    strings.add(new WrappedString("A"));
    Assert.assertEquals(strings.size(), Matches.fromCollection(strings).size());
  }

  @Test
  public void getDifferentNamesShouldReturnTheRightCount() throws Exception {
    List<WrappedString> strings = new ArrayList<>();
    Assert.assertEquals(0, Matches.fromCollection(strings).getDifferentNames());
    strings.add(new WrappedString("A"));
    Assert.assertEquals(1, Matches.fromCollection(strings).getDifferentNames());
    strings.add(new WrappedString("A"));
    Assert.assertEquals(1, Matches.fromCollection(strings).getDifferentNames());
    strings.add(new WrappedString("B"));
    Assert.assertEquals(2, Matches.fromCollection(strings).getDifferentNames());
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
