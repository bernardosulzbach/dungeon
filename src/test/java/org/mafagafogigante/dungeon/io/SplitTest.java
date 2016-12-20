package org.mafagafogigante.dungeon.io;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SplitTest {

  @Test
  public void splitOnOnWithEmptyListShouldProduceTwoEmptyLists() throws Exception {
    Assert.assertEquals(Collections.<String>emptyList(), Split.splitOnOn(Collections.<String>emptyList()).getBefore());
    Assert.assertEquals(Collections.<String>emptyList(), Split.splitOnOn(Collections.<String>emptyList()).getAfter());
  }

  @Test
  public void splitOnOnWithOnListShouldProduceTwoEmptyLists() throws Exception {
    List<String> emptyList = Collections.emptyList();
    List<String> onList = Collections.singletonList("on");
    Assert.assertEquals(emptyList, Split.splitOnOn(onList).getBefore());
    Assert.assertEquals(emptyList, Split.splitOnOn(onList).getAfter());
  }

  @Test
  public void splitOnOnWithOnOnListShouldProduceEmptyBeforeAndOnOnAfter() throws Exception {
    List<String> emptyList = Collections.emptyList();
    List<String> onList = Collections.singletonList("on");
    List<String> onOnList = Arrays.asList("on", "on");
    Assert.assertEquals(emptyList, Split.splitOnOn(onOnList).getBefore());
    Assert.assertEquals(onList, Split.splitOnOn(onOnList).getAfter());
  }

  @Test
  public void splitOnOnWithThreeWordsSeparatedByOnShouldSplitOnOn() throws Exception {
    List<String> fooList = Collections.singletonList("foo");
    List<String> barList = Collections.singletonList("bar");
    List<String> sourceList = Arrays.asList("foo", "on", "bar");
    Assert.assertEquals(fooList, Split.splitOnOn(sourceList).getBefore());
    Assert.assertEquals(barList, Split.splitOnOn(sourceList).getAfter());
  }

}
