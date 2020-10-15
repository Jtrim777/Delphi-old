package com.jtrimble.delphi.registry;

import com.jtrimble.delphi.util.GameLogger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class ResourceLoader<R> {
  private String rootPath;
  private boolean doRecurseSubs;
  private String fileType;
  private Function<File, R> loader;

  public ResourceLoader(String rootPath, String extension,
      Function<File, R> loader, boolean includeSubs) {
    this.rootPath = rootPath;
    this.fileType = extension;
    this.loader = loader;
    this.doRecurseSubs = includeSubs;
  }

  private String getName(File file) {
    String name = file.getName();

    return name.substring(0, name.length() - (fileType.length()));
  }

  public Map<String, R> loadResources() {

    int md = doRecurseSubs ? 10 : 1;

    Map<String, R> out = new HashMap<>();

    try (Stream<Path> paths = Files.walk(Paths.get(rootPath), md)) {
      paths.map(Path::toString).filter(s -> s.endsWith(fileType))
          .map(File::new)
          .forEach(file -> {
            R resource = loader.apply(file);
            out.put(getName(file), resource);
          });
    } catch (IOException e) {
      GameLogger.DEFAULT.log("Error loading resources for root resource path " + rootPath +
          " (file type \""+fileType+"\")");
    }

    return out;
  }
}
