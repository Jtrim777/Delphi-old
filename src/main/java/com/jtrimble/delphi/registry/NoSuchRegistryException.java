package com.jtrimble.delphi.registry;

public class NoSuchRegistryException extends RuntimeException {
  public NoSuchRegistryException(String message) {
    super(message);
  }
}
