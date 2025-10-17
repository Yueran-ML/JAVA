package sttrswing.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Utility responsible for writing game save data to disk.
 */
public final class GameSaver {

  private GameSaver() {
    // Utility class
  }

  /**
   * Persist the provided game data to the supplied file path.
   *
   * @param path    location on disk to save to.
   * @param content game state serialised as a string.
   * @throws IOException              if writing to disk fails.
   * @throws IllegalArgumentException if either argument is null or empty.
   */
  public static void save(String path, String content) throws IOException {
    if (path == null || path.isBlank()) {
      throw new IllegalArgumentException("Path must not be empty.");
    }
    Objects.requireNonNull(content, "Content must not be null.");

    Path savePath = Path.of(path);
    Path parent = savePath.getParent();
    if (parent != null && !Files.exists(parent)) {
      Files.createDirectories(parent);
    }
    Files.writeString(savePath, content, StandardCharsets.UTF_8);
  }
}
