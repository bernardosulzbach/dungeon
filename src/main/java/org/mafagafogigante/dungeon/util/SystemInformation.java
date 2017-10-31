package org.mafagafogigante.dungeon.util;

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
public final class SystemInformation implements Writable {

  private final RichText information = compileInformation();

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

  private RichText compileInformation() {
    Date currentDate = new Date();
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String time = timeFormat.format(currentDate);
    String date = dateFormat.format(currentDate);
    StandardRichTextBuilder builder = new StandardRichTextBuilder();
    builder.append("User: ").append(getUserString()).append("\n");
    builder.append("Time: ").append(time).append("\n");
    builder.append("Date: ").append(date).append("\n");
    builder.append("Java: ").append(getJavaVersionString()).append("\n");
    builder.append("Heap: ").append(getMemoryInformation()).append("\n");
    builder.append("OS: ").append(getOsVersionString());
    return builder.toRichText();
  }

  @Override
  public List<RichString> toRichStrings() {
    return information.toRichStrings();
  }

  @Override
  public String toString() {
    return compileInformation().toString();
  }

}
