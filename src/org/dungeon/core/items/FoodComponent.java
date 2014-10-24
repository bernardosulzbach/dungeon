/* 
 * Copyright (C) 2014 Bernardo Sulzbach
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
package org.dungeon.core.items;

import java.io.Serializable;

/**
 * The food part of some items.
 * <p/>
 * Created by Bernardo on 14/10/2014.
 */
public class FoodComponent implements Serializable {

    private final int nutrition;
    private final int experienceOnEat;
    private final int integrityDecrementOnEat;

    // mafagafogigante: FoodComponent only has a copy constructor because it may, in the future, hold mutable data.
    // I think that some specific fruits may be more nutritious than others and that food integrity may eventually
    // become independent of the item's integrity.
    /**
     * The copy constructor.
     */
    public FoodComponent(FoodComponent model) {
        this(model.getNutrition(), model.getExperienceOnEat(), model.getIntegrityDecrementOnEat());
    }

    public FoodComponent(int nutrition, int experienceOnEat, int integrityDecrementOnEat) {
        this.nutrition = nutrition;
        this.experienceOnEat = experienceOnEat;
        this.integrityDecrementOnEat = integrityDecrementOnEat;
    }

    public int getNutrition() {
        return nutrition;
    }

    public int getExperienceOnEat() {
        return experienceOnEat;
    }

    public int getIntegrityDecrementOnEat() {
        return integrityDecrementOnEat;
    }

}
