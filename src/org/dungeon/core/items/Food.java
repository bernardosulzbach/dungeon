package org.dungeon.core.items;


/**
 * Food class.
 * <p/>
 * Change log
 * Created by Bernardo on 18/09/2014.
 */
public class Food extends Item {

    private static final String TYPE = "Food";

    private final int nutrition;

    protected static Food createFood(FoodPreset preset) {
        return new Food(preset.name, preset.nutrition);
    }

    private Food(String name, int nutrition) {
        super(name, TYPE);
        this.nutrition = nutrition;
    }

    public int getNutrition() {
        return nutrition;
    }

    @Override
    public String toSelectionEntry() {
        return super.toSelectionEntry() + "Nutrition: " + getNutrition();
    }
}