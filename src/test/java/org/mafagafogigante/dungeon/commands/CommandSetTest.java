package org.mafagafogigante.dungeon.commands;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandSetTest {

  @Test
  public void shouldBeAbleToRetrieveAnAddedCommand() throws Exception {
    CommandSet commandSet = CommandSet.emptyCommandSet();
    Command command = new Command("go", "Go to the specified location.") {
      @Override
      public void execute(@NotNull String[] arguments) {
      }
    };
    commandSet.addCommand(command);
    Assertions.assertEquals(command, commandSet.getCommand("go"));
  }

  @Test
  public void shouldGetNullWhenRetrievingANonexistentCommand() throws Exception {
    CommandSet commandSet = CommandSet.emptyCommandSet();
    Assertions.assertNull(commandSet.getCommand("go"));
  }

  @Test
  public void shouldNotGetNullWhenRetrievingTheCommandsCommand() throws Exception {
    CommandSet commandSet = CommandSet.emptyCommandSet();
    Assertions.assertNotNull(commandSet.getCommand("commands"));
  }

}
