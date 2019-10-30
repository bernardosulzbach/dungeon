package org.mafagafogigante.dungeon.util;

import org.mafagafogigante.dungeon.game.RichString;
import org.mafagafogigante.dungeon.game.RichStringSequence;
import org.mafagafogigante.dungeon.game.Writable;
import org.mafagafogigante.dungeon.io.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * SystemInformation class used to retrieve human readable information about the system on which the application is
 * running.
 *
 * <p>The information is generated at construction time, therefore the caller should not cache objects of this class.
 */
public final class SystemInformation extends Writable {

  private final RichStringSequence information = compileInformation();

  private static String getUserString() {
    return System.getProperty("user.name");
  }

  private static String getMemoryInformation() {
    Runtime runtime = Runtime.getRuntime();
    String used = Converter.bytesToHuman(runtime.totalMemory() - runtime.freeMemory());
    String total = Converter.bytesToHuman(runtime.totalMemory());
    return "Using " + used + " out of the allocated " + total;
  }

  private static String getJavaVersionString() {
    return "Java version " + System.getProperty("java.version") + " by " + System.getProperty("java.vendor");
  }

  private static String getOsVersionString() {
    String name = System.getProperty("os.name");
    String arch = System.getProperty("os.arch");
    String version = System.getProperty("os.version");
    return String.format("%s (%s) %s", name, arch, version);
  }

  private RichStringSequence compileInformation() {
    Date currentDate = new Date();
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String time = timeFormat.format(currentDate);
    String date = dateFormat.format(currentDate);
    RichStringSequence richStringSequence = new RichStringSequence();
    richStringSequence.append("User: ", getUserString(), "\n");
    richStringSequence.append("Time: ", time, "\n");
    richStringSequence.append("Date: ", date, "\n");
    richStringSequence.append("Java: ", getJavaVersionString(), "\n");
    richStringSequence.append("Heap: ", getMemoryInformation(), "\n");
    richStringSequence.append("OS: ", getOsVersionString());
    return richStringSequence;
  }

  @Override
  public List<RichString> toRichStrings() {
    return information.toRichStrings();
  }

  @Override
  public String toString() {
    return toJavaString();
  }

}
