package game;

public class Campaign {

    private final World campaignWorld;
    private final Hero campaignHero;

    public Campaign() {
        campaignHero = new Hero("Seth");
        campaignHero.setWeapon(new Weapon("Stick", 6, 20));
        campaignWorld = createDemoWorld();
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

    public World getWorld() {
        return campaignWorld;
    }

    public Hero getHero() {
        return campaignHero;
    }

}
