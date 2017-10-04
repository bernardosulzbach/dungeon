package org.mafagafogigante.dungeon.game;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DungeonDistributorTest {

  private static final Point origin = new Point(0, 0, 0);
  private static final MinimumBoundingRectangle boundingRectangle = new MinimumBoundingRectangle(1, 1);
  private static final List<Point> list = DungeonDistributor.makeNoEntrancesZonePointList(origin, boundingRectangle);

  @Test
  public void makeNoEntrancesZonePointListShouldNotIncludeTheStartingPoint() throws Exception {
    List<Point> list = DungeonDistributor.makeNoEntrancesZonePointList(origin, new MinimumBoundingRectangle(1, 1));
    for (Point point : list) {
      Assert.assertThat(point, CoreMatchers.not(origin));
    }
  }

  @Test
  public void makeNoEntrancesZonePointListShouldIncludeBorders() throws Exception {
    List<Point> expectedList = new ArrayList<>();
    expectedList.add(new Point(-1, 1, 0));
    expectedList.add(new Point(0, 1, 0));
    expectedList.add(new Point(1, 1, 0));
    expectedList.add(new Point(-1, 0, 0));
    expectedList.add(new Point(1, 0, 0));
    expectedList.add(new Point(-1, -1, 0));
    expectedList.add(new Point(0, -1, 0));
    expectedList.add(new Point(1, -1, 0));
    Assert.assertTrue(expectedList.containsAll(list));
    Assert.assertTrue(list.containsAll(expectedList));
  }

  @Test
  public void makeNoEntrancesZonePointListShouldWorkWithBiggerDungeons() throws Exception {
    final int width = 3;
    final int height = 1;
    final MinimumBoundingRectangle threeByOneBoundingRectangle = new MinimumBoundingRectangle(width, height);
    List<Point> returnedList = DungeonDistributor.makeNoEntrancesZonePointList(origin, threeByOneBoundingRectangle);
    List<Point> expectedList = new ArrayList<>();
    for (int i = -width; i <= width; i++) {
      for (int j = -height; j <= height; j++) {
        if (i != origin.getX() || j != origin.getY()) {
          expectedList.add(new Point(i, j, 0));
        }
      }
    }
    Assert.assertTrue(expectedList.containsAll(returnedList));
    Assert.assertTrue(returnedList.containsAll(expectedList));
  }

  @Test
  public void dungeonDistributorShouldThrowExceptionIfTheSameEntranceIsRegisteredTwice() throws Exception {
    DungeonDistributor first = new DungeonDistributor();
    first.registerDungeonEntrance(new Point(0, 0, 0));
    try {
      first.registerDungeonEntrance(new Point(0, 0, 0));
      Assert.fail("Expected an IllegalStateException from the DungeonDistributor.");
    } catch (IllegalStateException expected) {
      // Expected.
    }
  }

  @Test
  public void dungeonDistributorsEntranceRegistriesShouldBeIndependent() throws Exception {
    DungeonDistributor first = new DungeonDistributor();
    first.registerDungeonEntrance(new Point(0, 0, 0));
    DungeonDistributor second = new DungeonDistributor();
    second.registerDungeonEntrance(new Point(0, 0, 0));
  }

}
