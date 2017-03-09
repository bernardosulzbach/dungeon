package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.date.DungeonTimeParser;
import org.mafagafogigante.dungeon.date.DungeonTimeUnit;
import org.mafagafogigante.dungeon.date.Duration;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.Percentage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EffectFactory implements Serializable {

  private static final long serialVersionUID = Version.MAJOR;
  private static final EffectFactory defaultFactory = new EffectFactory();
  private final Map<Id, EffectTemplate> templates = new HashMap<>();

  private EffectFactory() {
    templates.put(new Id("HEALING"), new HealingEffectTemplate());
    templates.put(new Id("EXTRA_ATTACK"), new AttackEffectTemplate());
    templates.put(new Id("FISHING_PROFICIENCY"), new FishingProficiencyTemplate());
    templates.put(new Id("WELL_FED"), new WellFedEffectTemplate());
  }

  public static EffectFactory getDefaultFactory() {
    return defaultFactory;
  }

  private static void assertParameterCount(List<String> parameters, int count) {
    int actual = parameters.size();
    if (actual != count) {
      throw new IllegalArgumentException(String.format("Wrong number of arguments: %d instead of %d", actual, count));
    }
  }

  /**
   * Returns an Effect for the specified Id with the provided parameterization.
   */
  public Effect getEffect(Id id, List<String> parameters) {
    EffectTemplate template = templates.get(id);
    if (template == null) {
      throw new IllegalArgumentException(id + " did not match any effect template");
    }
    return template.instantiate(parameters);
  }

  private static class HealingEffectTemplate extends EffectTemplate {
    private static final long serialVersionUID = Version.MAJOR;

    @Override
    public Effect instantiate(List<String> parameters) {
      assertParameterCount(parameters, 1);
      final int healing = Integer.parseInt(parameters.get(0));
      return new HealingEffect(healing);
    }
  }

  private static class AttackEffectTemplate extends EffectTemplate {
    private static final long serialVersionUID = Version.MAJOR;

    @Override
    public Effect instantiate(List<String> parameters) {
      assertParameterCount(parameters, 2);
      final int extraDamage = Integer.parseInt(parameters.get(0));
      // Period parsing already throws only IllegalArgumentException and derived exceptions.
      final Duration duration = DungeonTimeParser.parseDuration(parameters.get(1));
      return new AttackEffect(duration, extraDamage);
    }
  }

  private static class WellFedEffectTemplate extends EffectTemplate {
    private static final long serialVersionUID = Version.MAJOR;
    private static final Duration SIX_HOURS = DungeonTimeParser.parseDuration("6 hours");

    @Override
    public Effect instantiate(List<String> parameters) {
      assertParameterCount(parameters, 0);
      return new HitRateEffect(SIX_HOURS, 1.05, 1);
    }
  }

  private static class FishingProficiencyTemplate extends EffectTemplate {
    private static final long serialVersionUID = Version.MAJOR;

    @Override
    public Effect instantiate(List<String> parameters) {
      assertParameterCount(parameters, 2);
      final Percentage extraProficiency = Percentage.fromString(parameters.get(0));
      // Period parsing already throws only IllegalArgumentException and derived exceptions.
      final Duration duration = DungeonTimeParser.parseDuration(parameters.get(1));
      return new FishingProficiencyEffect(duration, extraProficiency, 1);
    }
  }

  private static class HealingEffect extends Effect {
    private static final long serialVersionUID = Version.MAJOR;
    private final int healing;

    HealingEffect(int healing) {
      this.healing = healing;
    }

    @Override
    public void affect(Creature creature) {
      creature.getHealth().incrementBy(healing);
    }
  }

  private static class AttackEffect extends Effect {
    private static final long serialVersionUID = Version.MAJOR;
    private final Duration duration;
    private final int extraDamage;

    AttackEffect(Duration duration, int extraDamage) {
      this.duration = duration;
      this.extraDamage = extraDamage;
    }

    @Override
    public void affect(final Creature creature) {
      Date start = creature.getLocation().getWorld().getWorldDate();
      final Date end = start.plus(duration.getSeconds(), DungeonTimeUnit.SECOND);
      creature.addCondition(new AttackCondition(this, end, extraDamage));
    }

    private static class AttackCondition extends Condition {
      private static final long serialVersionUID = Version.MAJOR;
      private final Date end;
      private final Effect effect;
      private final int attackModifier;

      AttackCondition(Effect effect, Date end, int attackModifier) {
        this.effect = effect;
        this.end = end;
        this.attackModifier = attackModifier;
      }

      @Override
      Date getExpirationDate() {
        return end;
      }

      public Effect getEffect() {
        return effect;
      }

      @Override
      String getDescription() {
        return "+" + attackModifier + " to attack";
      }

      @Override
      int modifyAttack(int currentAttack) {
        return currentAttack + attackModifier;
      }
    }
  }

  private static class HitRateEffect extends Effect {
    private static final long serialVersionUID = Version.MAJOR;
    private final Duration duration;
    private final double hitRateMultiplier;
    private final int maximumStack;

    HitRateEffect(Duration duration, double hitRateMultiplier, int maximumStack) {
      this.duration = duration;
      this.hitRateMultiplier = hitRateMultiplier;
      this.maximumStack = maximumStack;
    }

    @Override
    public int getMaximumStack() {
      return maximumStack;
    }

    @Override
    public void affect(final Creature creature) {
      Date start = creature.getLocation().getWorld().getWorldDate();
      final Date end = start.plus(duration.getSeconds(), DungeonTimeUnit.SECOND);
      creature.addCondition(new HitRateCondition(this, end, hitRateMultiplier));
    }

    private static class HitRateCondition extends Condition {
      private static final long serialVersionUID = Version.MAJOR;
      private final Date end;
      private final Effect effect;
      private final double hitRateMultiplier;

      HitRateCondition(Effect effect, Date end, double hitRateMultiplier) {
        this.end = end;
        this.effect = effect;
        this.hitRateMultiplier = hitRateMultiplier;
      }

      @Override
      Date getExpirationDate() {
        return end;
      }

      public Effect getEffect() {
        return effect;
      }

      @Override
      String getDescription() {
        return "+" + (int) ((hitRateMultiplier - 1.0) * 100) + "% hit rate";
      }

      @Override
      Percentage modifyHitRate(Percentage hitRate) {
        double asDouble = hitRate.toDouble() * hitRateMultiplier;
        if (Double.compare(asDouble, 1.0) > 0) {
          asDouble = 1.0;
        }
        return new Percentage(asDouble);
      }
    }
  }

  private static class FishingProficiencyEffect extends Effect {
    private static final long serialVersionUID = Version.MAJOR;
    private final Duration duration;
    private final Percentage extraProficiency;
    private final int maximumStack;

    FishingProficiencyEffect(Duration duration, Percentage extraProficiency, int maximumStack) {
      this.duration = duration;
      this.extraProficiency = extraProficiency;
      this.maximumStack = maximumStack;
    }

    @Override
    public int getMaximumStack() {
      return maximumStack;
    }

    @Override
    public void affect(final Creature creature) {
      Date start = creature.getLocation().getWorld().getWorldDate();
      final Date end = start.plus(duration.getSeconds(), DungeonTimeUnit.SECOND);
      creature.addCondition(new ImprovedFishingCondition(this, end, extraProficiency));
    }

    private static class ImprovedFishingCondition extends Condition {
      private static final long serialVersionUID = Version.MAJOR;
      private final Date end;
      private final Effect effect;
      private final Percentage extraProficiency;

      ImprovedFishingCondition(Effect effect, Date end, Percentage extraProficiency) {
        this.effect = effect;
        this.end = end;
        this.extraProficiency = extraProficiency;
      }

      @Override
      Date getExpirationDate() {
        return end;
      }

      public Effect getEffect() {
        return effect;
      }

      @Override
      String getDescription() {
        return "+" + extraProficiency + " to fishing proficiency";
      }

      @Override
      Percentage modifyFishingProficiency(Percentage proficiency) {
        return proficiency.add(extraProficiency);
      }
    }
  }

}
