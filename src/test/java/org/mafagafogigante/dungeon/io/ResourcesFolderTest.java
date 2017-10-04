package org.mafagafogigante.dungeon.io;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResourcesFolderTest {

  private static final int FONT_FILES_NUMBER = 1;
  private static final int JSON_FILES_NUMBER = 11;
  private static final String RESOURCES_TARGET_PATH = "../classes/";
  private static final String JSON_FILE_EXTENSION = ".json";
  private static final String FONT_FILE_EXTENSION = ".ttf";
  private static final String SPLASH_SCREEN_FILE_NAME = "splash.png";

  private File resourcesDir;

  /**
   * Try to initialize the resources directory.
   */
  @Before
  public void initialize() {
    URL resourcesUrl = this.getClass().getClassLoader().getResource(RESOURCES_TARGET_PATH);
    if (resourcesUrl != null) {
      resourcesDir = new File(resourcesUrl.getFile());
      if (!resourcesDir.isDirectory()) {
        Assert.fail("Resources file should be a directory");
      }
    } else {
      Assert.fail("Unable to find resources folder");
    }
  }

  @Test
  public void testResourcesFolderContainsTheCorrectNumberOfJsonFiles() {
    List<File> jsonFiles = findFilesByFileFilter(getEndWithFileFilter(JSON_FILE_EXTENSION));
    Assert.assertEquals(JSON_FILES_NUMBER, jsonFiles.size());
  }

  @Test
  public void testResourcesFolderContainsTheCorrectNumberOfFontFiles() {
    List<File> fontFiles = findFilesByFileFilter(getEndWithFileFilter(FONT_FILE_EXTENSION));
    Assert.assertEquals(FONT_FILES_NUMBER, fontFiles.size());
  }

  @Test
  public void testResourcesFolderContainsTheSplashScreenFile() {
    List<File> splashScreenFile = findFilesByFileFilter(getEndWithFileFilter(SPLASH_SCREEN_FILE_NAME));
    Assert.assertEquals(1, splashScreenFile.size());
  }

  private FileFilter getEndWithFileFilter(final String endWithExpression) {
    return new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname.getName().endsWith(endWithExpression);
      }
    };
  }

  private List<File> findFilesByFileFilter(FileFilter fileFilter) {
    final File[] files = resourcesDir.listFiles(fileFilter);
    if (files == null) {
      return Collections.emptyList();
    } else {
      return Arrays.asList(files);
    }
  }

}
