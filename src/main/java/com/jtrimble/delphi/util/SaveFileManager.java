package com.jtrimble.delphi.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class SaveFileManager {

  public static boolean gameDataExists() {
    File baseDir = getAppdataDirectory();

    return (new File(baseDir.getAbsolutePath()+"game_info.json")).exists();
  }

  public static JsonObject loadGameData() {
    File baseDir = getAppdataDirectory();

    File gameDataFile = new File(baseDir.getAbsolutePath()+"game_info.json");

    try {
      return new Gson().fromJson(new FileReader(gameDataFile.getAbsolutePath()), JsonObject.class);
    } catch (FileNotFoundException e) {
      throw new IllegalStateException("Game data file does not exist");
    }
  }


  private static File getAppdataDirectory() {
    String osName = System.getProperty("os.name").toUpperCase();
    String basePath;

    if (osName.contains("WIN")) {
      basePath = System.getenv("APPDATA") + "\\Delphi";
    } else if (osName.contains("MAC")) {
      basePath = System.getProperty("user.home") + "/Library/Application Support/Delphi";
    } else if (osName.contains("NUX")) {
      basePath = System.getProperty("user.dir") + ".Delphi";
    } else {
      throw new IllegalStateException("Unknown OS: "+osName);
    }

    File dir = new File(basePath);

    if (dir.exists()) {
      return dir;
    } else {
      dir.mkdir();
      return dir;
    }
  }
}
