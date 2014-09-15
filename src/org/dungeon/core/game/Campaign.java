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
package org.dungeon.core.game;

import org.dungeon.core.achievement.Achievement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Campaign implements Serializable {

    private final List<Achievement> campaignAchievements;
    private final BattleCounter campaignBattleCounter;
    private final World campaignWorld;
    private final Hero campaignHero;

    private boolean saved;

    private int unlockedAchievementsCounter;

    public Campaign() {
        campaignBattleCounter = new BattleCounter();
        campaignAchievements = createDemoAchievements();
        campaignHero = new Hero("Seth");
        campaignHero.setWeapon(new Weapon("Stick", 6, 20));
        campaignWorld = createDemoWorld();
    }

    private List<Achievement> createDemoAchievements() {
        List<Achievement> achievements = new ArrayList<>();

        BattleCounter suicideSolutionRequirements = new BattleCounter();
        suicideSolutionRequirements.setCounter(CreatureID.HERO, 1);
        achievements.add(new Achievement("Suicide Solution", "Attempt to kill yourself.", suicideSolutionRequirements));

        return achievements;
    }

    private World createDemoWorld() {
        World world = new World(new Location("Training Grounds"), campaignHero);

        world.addCreature(new Creature(CreatureID.BAT, 1), 0);
        world.addCreature(new Creature(CreatureID.BEAR, 1), 0);
        world.addCreature(new Creature(CreatureID.RABBIT, 1), 0);
        world.addCreature(new Creature(CreatureID.RAT, 1), 0);
        world.addCreature(new Creature(CreatureID.SPIDER, 1), 0);
        world.addCreature(new Creature(CreatureID.WOLF, 1), 0);
        world.addCreature(new Creature(CreatureID.ZOMBIE, 1), 0);

        Weapon longSword = new Weapon("Longsword", 18, 15);
        longSword.setDestructible(true);

        world.addItem(longSword, 0);

        return world;
    }

    public BattleCounter getBattleCounter() {
        return campaignBattleCounter;
    }

    public World getWorld() {
        return campaignWorld;
    }

    public Hero getHero() {
        return campaignHero;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    private int getUnlockedAchievementsCounter() {
        return unlockedAchievementsCounter;
    }

    private void incrementUnlockedAchievementsCounter() {
        this.unlockedAchievementsCounter++;
    }

    /**
     * Prints all unlocked achievements.
     */
    public void printUnlockedAchievements() {
        StringBuilder builder = new StringBuilder();
        builder.append("Progress: ").append(getUnlockedAchievementsCounter()).append('/');
        builder.append(campaignAchievements.size());
        for (Achievement a : campaignAchievements) {
            if (a.isUnlocked()) {
                builder.append("\n").append(a.toOneLineString());
            }
        }
        IO.writeString(builder.toString());
    }

    /**
     * Refreshes the campaign. Should be called after the player plays a turn.
     */
    public void refresh() {
        refreshAchievements();
    }

    private void refreshAchievements() {
        for (Achievement a : campaignAchievements) {
            if (a.update(campaignBattleCounter)) {
                incrementUnlockedAchievementsCounter();
            }
        }
    }

}
