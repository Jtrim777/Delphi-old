package com.jtrimble.delphi.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameLogger {
  private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("HH:mm:ss");
  public static final GameLogger DEFAULT = new GameLogger();

  private FileWriter outStream = null;
  private int levelAdvance = 0;
  private boolean logDebug = true;

  public void direct(String filepath) throws IOException {
    outStream = new FileWriter(new File(filepath));
  }

  private String getTimestamp() {
    return FORMATTER.format(new Date());
  }

  private String assembleMessage(int level, String locale, String message, LogLevel severity) {
    String indent = "";
    for (int i=0;i<level+levelAdvance;i++) {
      indent += "    ";
    }

    String prefix = "";
    String footer = "";
    switch (severity) {
      case DEBUG:
        prefix = "\u001b[36m>>>";
        footer = "\u001b[0m";
        break;
      case WARNING:
        prefix = "\u001b[33m!";
        footer = "\u001b[0m";
        break;
      case ERROR:
        prefix = "\u001b[31m!!!";
        footer = "\u001b[0m";
        break;
    }

    String localeBracket = locale == null ? "" : " ["+locale+"]";

    if (level + levelAdvance > 0) {
      return String.format("%s%s[%s]%s %s%s", indent, prefix, getTimestamp(), localeBracket,  message, footer);
    } else {
      return String.format("%s[%s]%s :: %s%s", prefix, getTimestamp(), localeBracket, message, footer);
    }
  }

  private void write(String out) {
    if (outStream == null) {
      System.out.println(out);
    } else {
      try {
        outStream.append(out);
      } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException();
      }
    }
  }

  public void log(String message, int level, String locale, LogLevel severity) {
    if (severity == LogLevel.DEBUG && !logDebug) {
      return;
    }

    String out = assembleMessage(level, locale, message, severity);

    write(out);
  }

  public void log(String message) {
    log(message, 0, null, LogLevel.INFO);
  }

  public void log(String message, int level) {
    log(message, level, null, LogLevel.INFO);
  }

  public void log(String message, String locale) {
    log(message, 0, locale, LogLevel.INFO);
  }

  public void log(String message, int level, String locale) {
    log(message, level, locale, LogLevel.INFO);
  }

  public void log(String message, LogLevel severity) {
    log(message, 0, null, severity);
  }

  public void log(String message, int level, LogLevel severity) {
    log(message, level, null, severity);
  }

  public void log(String message, String locale, LogLevel severity) {
    log(message, 0, locale, severity);
  }

  public void separate() {
    write("\n");
  }

  public void advanceLogLevel(int by) {
    this.levelAdvance += by;
  }
  public void returnLogLevel(int to) { this.levelAdvance = to; }
  public void resetLogLevel() {
    this.levelAdvance = 0;
  }

  public int getLogLevel() {
    return levelAdvance;
  }

  public void supressDebug() {
    this.logDebug = false;
  }

  public enum LogLevel {
    INFO,
    DEBUG,
    WARNING,
    ERROR
  }
}
