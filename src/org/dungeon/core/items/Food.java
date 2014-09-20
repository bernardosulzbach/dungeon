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


import org.dungeon.core.game.Game;
import org.dungeon.io.IO;

/**
 * Food class.
 * <p/>
 * Change log
 * Created by Bernardo on 18/09/2014.
 */
public class Food extends Item implements IWeapon, Breakable {

    private static final String TYPE = "Food";

    private int damage;
    private int missRate;
    private int maxIntegrity;
    private int curIntegrity;
    private int hitDecrement;
    private int nutrition;

    protected static Food createFood(FoodPreset preset) {
        return new Food(preset.name, preset.damage, preset.missRate, preset.integrity, preset.decrement, preset.nutrition);
    }

    private Food(String name, int damage, int missRate, int integrity, int decrement, int nutrition) {
        super(name, TYPE);
        this.damage = damage;
        this.missRate = missRate;
        this.maxIntegrity = integrity;
        this.curIntegrity = integrity;
        this.hitDecrement = decrement;
        this.nutrition = nutrition;
    }

    public int getNutrition() {
        return nutrition;
    }

    public int getCurIntegrity() {
        return curIntegrity;
    }

    /**
     * Sets the current integrity to a given value, validating it to avoid negative values.
     */
    public void setCurIntegrity(int curIntegrity) {
        if (curIntegrity < 0) {
            this.curIntegrity = 0;
        } else {
            this.curIntegrity = curIntegrity;
        }
    }

    public int getHitDecrement() {
        return hitDecrement;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public boolean isMiss() {
        return missRate > Game.RANDOM.nextInt(100);
    }

    @Override
    public boolean isBroken() {
        return getCurIntegrity() == 0;
    }

    @Override
    public void decrementIntegrity() {
        setCurIntegrity(getCurIntegrity() - getHitDecrement());
    }

    @Override
    public void repair() {
        IO.writeString("You cannot repair this item.");
    }

    @Override
    public String toSelectionEntry() {
        return super.toSelectionEntry() + "Nutrition: " + getNutrition();
    }
}