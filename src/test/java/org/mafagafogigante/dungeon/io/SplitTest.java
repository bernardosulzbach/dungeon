package org.mafagafogigante.dungeon.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SplitTest {

  @Test
  public void splitOnOnWithEmptyListShouldProduceTwoEmptyLists() throws Exception {
    Assertions.assertEquals(Collections.<String>emptyList(), Split.splitOnOn(Collections.emptyList()).getBefore());
    Assertions.assertEquals(Collections.<String>emptyList(), Split.splitOnOn(Collections.emptyList()).getAfter());
  }

  @Test
  public void splitOnOnWithOnListShouldProduceTwoEmptyLists() throws Exception {
    List<String> emptyList = Collections.emptyList();
    List<String> onList = Collections.singletonList("on");
    Assertions.assertEquals(emptyList, Split.splitOnOn(onList).getBefore());
    Assertions.assertEquals(emptyList, Split.splitOnOn(onList).getAfter());
  }

  @Test
  public void splitOnOnWithOnOnListShouldProduceEmptyBeforeAndOnOnAfter() throws Exception {
    List<String> emptyList = Collections.emptyList();
    List<String> onList = Collections.singletonList("on");
    List<String> onOnList = Arrays.asList("on", "on");
    Assertions.assertEquals(emptyList, Split.splitOnOn(onOnList).getBefore());
    Assertions.assertEquals(onList, Split.splitOnOn(onOnList).getAfter());
  }

  @Test
  public void splitOnOnWithThreeWordsSeparatedByOnShouldSplitOnOn() throws Exception {
    List<String> fooList = Collections.singletonList("foo");
    List<String> barList = Collections.singletonList("bar");
    List<String> sourceList = Arrays.asList("foo", "on", "bar");
    Assertions.assertEquals(fooList, Split.splitOnOn(sourceList).getBefore());
    Assertions.assertEquals(barList, Split.splitOnOn(sourceList).getAfter());
  }

}
