package game;

/**
 * A draft of what the achievement class will look like.
 *
 * Hero will have an Achievement list (or something similar).
 *
 * @author Bernardo Sulzbach
 */
public class Achievement {

    private final String name;
    private final String info;
    private boolean unlocked;

    public Achievement(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

}
