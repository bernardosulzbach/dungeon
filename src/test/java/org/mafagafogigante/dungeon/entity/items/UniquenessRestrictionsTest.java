/*
 * Copyright (C) 2016 Bernardo Sulzbach
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mafagafogigante.dungeon.entity.items;

import org.mafagafogigante.dungeon.game.Id;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class UniquenessRestrictionsTest {

  public static final Id UNIQUE_ID = new Id("BANANA");
  public static final Id NON_UNIQUE_ID = new Id("CARAMEL");
  private final static List<Id> uniqueIds = Arrays.asList(new Id("APPLE"), UNIQUE_ID);

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