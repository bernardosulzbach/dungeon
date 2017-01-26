package org.mafagafogigante.dungeon.schema.rules;

import org.mafagafogigante.dungeon.schema.JsonRule;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import org.junit.Test;

public class PeriodJsonRuleTest {

  private static final JsonRule periodJsonRule = new PeriodJsonRule();

  @Test(expected = IllegalArgumentException.class)
  public void periodJsonRuleShouldFailOnInvalidPeriodFormat() {
    JsonValue jsonValue = Json.value("1 monthss");
    periodJsonRule.validate(jsonValue);
  }

  @Test
  public void percentageJsonRuleShouldPassOnValidDaysPeriod() {
    JsonValue oneDay = Json.value("1 day");
    JsonValue twoDays = Json.value("2 days");
    periodJsonRule.validate(oneDay);
    periodJsonRule.validate(twoDays);
  }

  @Test
  public void percentageJsonRuleShouldPassOnValidMonthPeriod() {
    JsonValue oneMonth = Json.value("1 month");
    JsonValue twoMonths = Json.value("2 months");
    periodJsonRule.validate(oneMonth);
    periodJsonRule.validate(twoMonths);
  }

  @Test
  public void percentageJsonRuleShouldPassOnValidYearPeriod() {
    JsonValue oneYear = Json.value("1 year");
    JsonValue twoYears = Json.value("2 years");
    periodJsonRule.validate(oneYear);
    periodJsonRule.validate(twoYears);
  }

}
