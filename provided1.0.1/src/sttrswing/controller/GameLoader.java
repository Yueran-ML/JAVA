package sttrswing.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameLoader {

  private static final String ENTERPRISE_PREFIX = "[e]";
  private static final String QUADRANT_PREFIX = "[q]";

  private final String path;
  private String content;
  private boolean loadAttempted;
  private boolean loadSucceeded;

  public GameLoader(String path) {
    if (path == null || path.isBlank()) {
      throw new IllegalArgumentException("Path must not be null or blank.");
    }
    this.path = path;
  }

  public void load() {
    this.loadAttempted = true;
    try {
      this.content = Files.readString(Path.of(this.path));
      this.loadSucceeded = true;
    } catch (IOException ex) {
      this.content = null;
      this.loadSucceeded = false;
    }
  }

  public Boolean success() {
    return this.loadAttempted && this.loadSucceeded;
  }

  public Enterprise buildEnterprise() {
    String cached = ensureContent();
    Enterprise enterprise = null;
    for (String rawLine : splitLines(cached)) {
      String line = rawLine.trim();
      if (line.isEmpty()) {
        continue;
      }
      if (line.startsWith(ENTERPRISE_PREFIX)) {
        try {
          int x = parseLineForX(line);
          int y = parseLineForY(line);
          int energy = parseLineForEnergy(line);
          int shields = parseLineForShields(line);
          int torpedoes = parseLineForTorpedoes(line);
          enterprise = new Enterprise(x, y, energy, shields, torpedoes);
        } catch (IOException e) {
          throw new IllegalStateException("Failed to parse enterprise line", e);
        }
        break;
      }
    }
    if (enterprise == null) {
      throw new IllegalArgumentException("Missing enterprise data in loaded content.");
    }
    return enterprise;
  }

  public Galaxy buildGalaxy() {
    String cached = ensureContent();
    List<Quadrant> quadrants = new ArrayList<>();
    for (String rawLine : splitLines(cached)) {
      String line = rawLine.trim();
      if (line.isEmpty()) {
        continue;
      }
      if (line.startsWith(QUADRANT_PREFIX)) {
        try {
          int x = parseLineForX(line);
          int y = parseLineForY(line);
          Map<String, Integer> symbolCounts = parseLineForQuadrantSymbol(line);
          Integer stars = symbolCounts.get("stars");
          Integer starbases = symbolCounts.get("starbases");
          Integer klingons = symbolCounts.get("klingons");
          if (stars == null || starbases == null || klingons == null) {
            throw new IllegalStateException("Quadrant symbol map missing required entries.");
          }
          quadrants.add(new Quadrant(x, y, starbases, klingons, stars));
        } catch (IOException e) {
          throw new IllegalStateException("Failed to parse quadrant line", e);
        }
      }
    }
    if (quadrants.isEmpty()) {
      throw new IllegalArgumentException("Missing quadrant data in loaded content.");
    }
    return new Galaxy(new ArrayList<>(quadrants));
  }

  public int parseLineForX(String line) throws IOException {
    return parseIntegerValue(line, "x:");
  }

  public int parseLineForY(String line) throws IOException {
    return parseIntegerValue(line, "y:");
  }

  public int parseLineForEnergy(String line) throws IOException {
    return parseIntegerValue(line, "e:");
  }

  public int parseLineForShields(String line) throws IOException {
    return parseIntegerValue(line, "s:");
  }

  public int parseLineForTorpedoes(String line) throws IOException {
    return parseIntegerValue(line, "t:");
  }

  public Map<String, Integer> parseLineForQuadrantSymbol(String line) throws IOException {
    if (line == null) {
      throw new IOException("Line must not be null.");
    }
    int tokenIndex = line.indexOf("s:");
    if (tokenIndex < 0) {
      throw new IOException("Missing symbol declaration in line: " + line);
    }
    int start = tokenIndex + 2;
    while (start < line.length() && Character.isWhitespace(line.charAt(start))) {
      start++;
    }
    int end = start;
    while (end < line.length() && Character.isDigit(line.charAt(end))) {
      end++;
    }
    if (end <= start) {
      throw new IOException("Symbol does not contain digits: " + line);
    }
    String symbol = line.substring(start, end);
    if (symbol.length() != 3) {
      throw new IOException("Invalid quadrant symbol length: " + symbol);
    }
    int stars = parseDigit(symbol.charAt(0));
    int starbases = parseDigit(symbol.charAt(1));
    int klingons = parseDigit(symbol.charAt(2));
    Map<String, Integer> counts = new LinkedHashMap<>();
    counts.put("stars", stars);
    counts.put("starbases", starbases);
    counts.put("klingons", klingons);
    return counts;
  }

  @Override
  public String toString() {
    return "GameLoader[path=" + this.path + ", success=" + success() + "]";
  }

  private String ensureContent() {
    if (!this.loadAttempted) {
      throw new IllegalStateException("Load has not been attempted.");
    }
    if (!this.loadSucceeded || this.content == null || this.content.isBlank()) {
      throw new IllegalStateException("No content available to parse.");
    }
    return this.content;
  }

  private List<String> splitLines(String text) {
    return List.of(text.split("\\R"));
  }

  private int parseIntegerValue(String line, String token) throws IOException {
    if (line == null) {
      throw new IOException("Line must not be null.");
    }
    int tokenIndex = line.indexOf(token);
    if (tokenIndex < 0) {
      throw new IOException("Missing token '" + token + "' in line: " + line);
    }
    int start = tokenIndex + token.length();
    while (start < line.length() && Character.isWhitespace(line.charAt(start))) {
      start++;
    }
    if (start >= line.length()) {
      throw new IOException("Missing numeric value for token '" + token + "' in line: " + line);
    }
    int end = start;
    if (line.charAt(end) == '-' || line.charAt(end) == '+') {
      end++;
    }
    while (end < line.length() && Character.isDigit(line.charAt(end))) {
      end++;
    }
    if (end <= start || (end == start + 1 && (line.charAt(start) == '-' || line.charAt(start) == '+'))) {
      throw new IOException("No numeric value for token '" + token + "' in line: " + line);
    }
    String number = line.substring(start, end);
    try {
      return Integer.parseInt(number);
    } catch (NumberFormatException ex) {
      throw new IOException("Invalid integer for token '" + token + "' in line: " + line, ex);
    }
  }

  private int parseDigit(char digit) throws IOException {
    if (!Character.isDigit(digit)) {
      throw new IOException("Invalid digit in symbol: " + digit);
    }
  }
}
