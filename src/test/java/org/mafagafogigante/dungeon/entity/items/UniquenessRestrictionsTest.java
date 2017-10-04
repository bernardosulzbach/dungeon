package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.game.Id;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class UniquenessRestrictionsTest {

  private static final Id UNIQUE_ID = new Id("BANANA");
  private static final Id NON_UNIQUE_ID = new Id("CARAMEL");
  private static final List<Id> uniqueIds = Arrays.asList(new Id("APPLE"), UNIQUE_ID);

  @Test
  public void testUniquenessRestrictionsShouldNeverBanNonUniqueItems() throws Exception {
    ItemFactoryRestrictions restrictions = new UniquenessRestrictions(uniqueIds);
    Assert.assertTrue(restrictions.canMakeItem(NON_UNIQUE_ID));
    restrictions.registerItem(NON_UNIQUE_ID);
    Assert.assertTrue(restrictions.canMakeItem(NON_UNIQUE_ID));
  }

  @Test
  public void testUniquenessRestrictionsShouldNotBanUniqueItemsBeforeTheyHaveBeenCreated() throws Exception {
    ItemFactoryRestrictions restrictions = new UniquenessRestrictions(uniqueIds);
    Assert.assertTrue(restrictions.canMakeItem(UNIQUE_ID));
  }

  @Test
  public void testUniquenessRestrictionsShouldBanUniqueItemsAfterTheyHaveBeenCreated() throws Exception {
    ItemFactoryRestrictions restrictions = new UniquenessRestrictions(uniqueIds);
    restrictions.registerItem(UNIQUE_ID);
    Assert.assertFalse(restrictions.canMakeItem(UNIQUE_ID));
  }

  @Test(expected = IllegalStateException.class)
  public void testUniquenessRestrictionsShouldThrowExceptionWhenRegisteringBannedItems() throws Exception {
    ItemFactoryRestrictions restrictions = new UniquenessRestrictions(uniqueIds);
    restrictions.registerItem(UNIQUE_ID);
    restrictions.registerItem(UNIQUE_ID);
  }

}
