package org.mafagafogigante.dungeon.entity.creatures;

import org.mafagafogigante.dungeon.date.Date;
import org.mafagafogigante.dungeon.date.DungeonTimeParser;
import org.mafagafogigante.dungeon.date.DungeonTimeUnit;
import org.mafagafogigante.dungeon.date.Duration;
import org.mafagafogigante.dungeon.game.Id;
import org.mafagafogigante.dungeon.io.Version;
import org.mafagafogigante.dungeon.util.DungeonMath;

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
    templates.put(new Id("EXTRA_ATTACK"), new ExtraAttackEffectTemplate());
  }

  public static EffectFactory getDefaultFactory() {
    return defaultFactory;
  }

  /**
   * Returns an Effect for the specified Id with the provided parameterization.
   */
  public Effect getEffect(Id id, List<String> parameters) {
    EffectTemplate template = templates.get(id);
    if (template == null) {
      throw new IllegalArgumentException(id + " did not match any effect template!");
    }
    return template.instantiate(parameters);
  }

  private static class HealingEffectTemplate extends EffectTemplate {
    private static final long serialVersionUID = Version.MAJOR;

    @Override
    public Effect instantiate(List<String> parameters) {
      if (parameters.size() != 1) {
        throw new IllegalArgumentException("expected one parameter, got " + parameters.size());
      }
      final int healing = Integer.parseInt(parameters.get(0));
      return new HealingEffect(healing);
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
  }

  private static class ExtraAttackEffectTemplate extends EffectTemplate {
    private static final long serialVersionUID = Version.MAJOR;

    @Override
    public Effect instantiate(List<String> parameters) {
      if (parameters.size() != 2) {
        throw new IllegalArgumentException("expected two parameters");
      }
      final int extraDamage = Integer.parseInt(parameters.get(0));
      // Period parsing already throws only IllegalArgumentException and derived exceptions.
      final Duration duration = DungeonTimeParser.parsePeriod(parameters.get(1));
      return new ExtraAttackEffect(duration, extraDamage);
    }

    private static class ExtraAttackEffect extends Effect {
      private static final long serialVersionUID = Version.MAJOR;
      private final Duration duration;
      private final int extraDamage;

      ExtraAttackEffect(Duration duration, int extraDamage) {
        this.duration = duration;
        this.extraDamage = extraDamage;
      }

      @Override
      public void affect(final Creature creature) {
        Date start = creature.getLocation().getWorld().getWorldDate();
        int durationSeconds = DungeonMath.safeCastLongToInteger(duration.getSeconds());
        final Date end = start.plus(durationSeconds, DungeonTimeUnit.SECOND);
        creature.addCondition(new ExtraAttackCondition(end, extraDamage));
      }

      private static class ExtraAttackCondition extends Condition {
        private static final long serialVersionUID = Version.MAJOR;
        private final Date end;
        private final int extraDamage;

        ExtraAttackCondition(Date end, int extraDamage) {
          this.end = end;
          this.extraDamage = extraDamage;
        }

        @Override
        Date getExpirationDate() {
          return end;
        }

        @Override
        String getDescription() {
          return "+" + extraDamage + " to attack";
        }

        @Override
        int modifyAttack(int currentAttack) {
          return currentAttack + extraDamage;
        }
      }
    }
  }

}
