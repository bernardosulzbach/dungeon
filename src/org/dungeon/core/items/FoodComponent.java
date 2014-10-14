package org.dungeon.core.items;

import java.io.Serializable;

/**
 * The food part of some items.
 * <p/>
 * Created by Bernardo on 14/10/2014.
 */
public class FoodComponent implements Serializable {

    private int nutrition;
    private int experienceOnEat;
    private int integrityDecrementOnEat;

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
