package sttrswing.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameLoader is responsible for loading the game state from a .trek file and reconstructing the
 * model entities.
 */
public class GameLoader {

  private static final String ENTERPRISE_PREFIX = "[e]";
  private static final String QUADRANT_PREFIX = "[q]";

  private final String path;
  private String cachedContent;
  private boolean loadAttempted;
  private boolean lastLoadSuccessful;

  public GameLoader(String path) {
    if (path == null || path.isBlank()) {
      throw new IllegalArgumentException("Path must not be null or blank.");
    }
    this.path = path;
    this.cachedContent = null;
    this.loadAttempted = false;
    this.lastLoadSuccessful = false;
  }

  public void load() {
    this.loadAttempted = true;
    try {
      this.cachedContent = Files.readString(Path.of(this.path));
      this.lastLoadSuccessful = true;
    } catch (IOException e) {
      this.cachedContent = null;
      this.lastLoadSuccessful = false;
    }
  }

  public Boolean success() {
    if (!this.loadAttempted) {
      return Boolean.FALSE;
    }
    return this.lastLoadSuccessful;
  }

  public Enterprise buildEnterprise() {
    ensureContentIsAvailable();
    String line = enterpriseLine();
    try {
      int x = parseLineForX(line);
      int y = parseLineForY(line);
      int energy = parseLineForEnergy(line);
      int shields = parseLineForShields(line);
      int torpedoes = parseLineForTorpedoes(line);
      return new Enterprise(x, y, energy, shields, torpedoes);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to parse enterprise data.", e);
    }
  }

  public Galaxy buildGalaxy() {
    ensureContentIsAvailable();
    ArrayList<String> lines = galaxyLines();
    if (lines.isEmpty()) {
      throw new IllegalStateException("No quadrant data present in cached content.");
    }
    ArrayList<Quadrant> quadrants = new ArrayList<>();
    for (String line : lines) {
      try {
        int x = parseLineForX(line);
        int y = parseLineForY(line);
        Map<String, Integer> counts = parseLineForQuadrantSymbol(line);
        Integer stars = counts.get("stars");
        Integer starbases = counts.get("starbases");
        Integer klingons = counts.get("klingons");
        if (stars == null || starbases == null || klingons == null) {
          throw new IOException("Missing counts for quadrant.");
        }
        quadrants.add(new Quadrant(x, y, starbases, klingons, stars));
      } catch (IOException e) {
        throw new IllegalStateException("Unable to parse quadrant data.", e);
      }
    }
    return new Galaxy(quadrants);
  }

  public int parseLineForX(String line) throws IOException {
    return parseIntegerToken(line, "x:");
  }

  public int parseLineForY(String line) throws IOException {
    return parseIntegerToken(line, "y:");
  }

  public Map<String, Integer> parseLineForQuadrantSymbol(String line) throws IOException {
    if (line == null) {
      throw new IOException("Line cannot be null.");
    }
    String trimmed = line.trim();
    int index = trimmed.indexOf("s:");
    if (index < 0) {
      throw new IOException("Quadrant symbol token 's:' missing.");
    }
    int start = index + 2;
    int end = start;
    while (end < trimmed.length() && Character.isDigit(trimmed.charAt(end))) {
      end++;
    }
    if (end == start) {
      throw new IOException("Quadrant symbol missing digit sequence.");
    }
    String symbol = trimmed.substring(start, end);
    if (symbol.length() != 3) {
      throw new IOException("Quadrant symbol must be exactly three digits.");
    }
    int stars = parseDigit(symbol.charAt(0));
    int starbases = parseDigit(symbol.charAt(1));
    int klingons = parseDigit(symbol.charAt(2));
    Map<String, Integer> result = new HashMap<>();
    result.put("stars", stars);
    result.put("starbases", starbases);
    result.put("klingons", klingons);
    return result;
  }

  public String toString() {
    return "GameLoader[path=" + this.path + ", success=" + success() + "]";
  }

  public String enterpriseLine() {
    ensureContentIsAvailable();
    for (String line : contentLines()) {
      if (line != null) {
        String trimmed = line.trim();
        if (!trimmed.isEmpty() && trimmed.startsWith(ENTERPRISE_PREFIX)) {
          return trimmed;
        }
      }
    }
    throw new IllegalStateException("Enterprise line not found in cached content.");
  }

  public ArrayList<String> galaxyLines() {
    ensureContentIsAvailable();
    ArrayList<String> lines = new ArrayList<>();
    for (String line : contentLines()) {
      if (line != null) {
        String trimmed = line.trim();
        if (!trimmed.isEmpty() && trimmed.startsWith(QUADRANT_PREFIX)) {
          lines.add(trimmed);
        }
      }
    }
    return lines;
  }

  public int parseLineForEnergy(String line) throws IOException {
    return parseIntegerToken(line, "e:");
  }

  public int parseLineForShields(String line) throws IOException {
    return parseIntegerToken(line, "s:");
  }

  public int parseLineForTorpedoes(String line) throws IOException {
    return parseIntegerToken(line, "t:");
  }

  private void ensureContentIsAvailable() {
    if (this.cachedContent == null) {
      throw new IllegalStateException("No cached content available. Call load() and ensure it succeeds first.");
    }
  }

  private List<String> contentLines() {
    return List.of(this.cachedContent.split("\\R", -1));
  }

  private int parseIntegerToken(String line, String token) throws IOException {
    if (line == null) {
      throw new IOException("Line cannot be null.");
    }
    String trimmed = line.trim();
    int index = trimmed.indexOf(token);
    if (index < 0) {
      throw new IOException("Token '" + token + "' missing from line.");
    }
    int start = index + token.length();
    int end = start;
    while (end < trimmed.length() && Character.isDigit(trimmed.charAt(end))) {
      end++;
    }
    if (end == start) {
      throw new IOException("No digits found for token '" + token + "'.");
    }
    String number = trimmed.substring(start, end);
    try {
      return Integer.parseInt(number);
    } catch (NumberFormatException e) {
      throw new IOException("Unable to parse integer for token '" + token + "'.", e);
    }
  }

  private int parseDigit(char value) throws IOException {
    if (!Character.isDigit(value)) {
      throw new IOException("Quadrant symbol contains non-digit character.");
    }
    return Character.digit(value, 10);
  }
}
