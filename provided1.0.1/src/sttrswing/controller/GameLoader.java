package sttrswing.controller;

import sttrswing.model.Enterprise;
import sttrswing.model.Galaxy;
import sttrswing.model.Quadrant;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for loading and parsing saved game state.
 */
public final class GameLoader {

    private static final String ENTERPRISE_PREFIX = "[e]";
    private static final String QUADRANT_PREFIX = "[q]";

    private GameLoader() {
        // Utility class
    }

    /**
     * Load the contents of a save file into memory.
     *
     * @param path path to the save file.
     * @return file contents as a string.
     * @throws IOException              if the file cannot be read.
     * @throws IllegalArgumentException if the path is null or blank.
     */
    public static String load(String path) throws IOException {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Path must not be empty.");
        }
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }

    /**
     * Parse the provided save data into {@link Enterprise} and {@link Galaxy} instances.
     *
     * @param content stringified game state.
     * @return parsed {@link GameState} containing new model instances.
     * @throws IllegalArgumentException if the content is malformed.
     */
    public static GameState parse(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Save data must not be empty.");
        }

        Enterprise enterprise = null;
        List<Quadrant> quadrants = new ArrayList<>();

        String[] lines = content.split("\\R");
        for (String rawLine : lines) {
            if (rawLine == null) {
                continue;
            }
            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }
            if (line.startsWith(ENTERPRISE_PREFIX)) {
                enterprise = parseEnterprise(line);
            } else if (line.startsWith(QUADRANT_PREFIX)) {
                quadrants.add(parseQuadrant(line));
            }
        }

        if (enterprise == null) {
            throw new IllegalArgumentException("Missing enterprise data in save file.");
        }
        if (quadrants.isEmpty()) {
            throw new IllegalArgumentException("Missing quadrant data in save file.");
        }

        return new GameState(enterprise, new Galaxy(new ArrayList<>(quadrants)));
    }

    private static Enterprise parseEnterprise(String line) {
        int x = parseIntegerValue(line, "x:");
        int y = parseIntegerValue(line, "y:");
        int energy = parseIntegerValue(line, "e:");
        int shields = parseIntegerValue(line, "s:");
        int torpedoes = parseIntegerValue(line, "t:");
        return new Enterprise(x, y, energy, shields, torpedoes);
    }

    private static Quadrant parseQuadrant(String line) {
        int x = parseIntegerValue(line, "x:");
        int y = parseIntegerValue(line, "y:");
        String symbol = parseSymbol(line);
        if (symbol.length() != 3) {
            throw new IllegalArgumentException("Invalid quadrant symbol: " + symbol);
        }
        int stars = parseDigit(symbol.charAt(0));
        int starbases = parseDigit(symbol.charAt(1));
        int klingons = parseDigit(symbol.charAt(2));
        return new Quadrant(x, y, starbases, klingons, stars);
    }

    private static int parseIntegerValue(String line, String token) {
        int start = line.indexOf(token);
        if (start < 0) {
            throw new IllegalArgumentException("Missing token '" + token + "' in line: " + line);
        }
        start += token.length();
        int end = start;
        while (end < line.length() && Character.isDigit(line.charAt(end))) {
            end++;
        }
        if (end == start) {
            throw new IllegalArgumentException("No numeric value for token '" + token + "' in line: " + line);
        }
        return Integer.parseInt(line.substring(start, end));
    }

    private static String parseSymbol(String line) {
        int start = line.indexOf("s:");
        if (start < 0) {
            throw new IllegalArgumentException("Missing symbol declaration in line: " + line);
        }
        start += 2;
        int end = start;
        while (end < line.length() && Character.isDigit(line.charAt(end))) {
            end++;
        }
        if (end == start) {
            throw new IllegalArgumentException("Symbol does not contain digits: " + line);
        }
        return line.substring(start, end);
    }

    private static int parseDigit(char digit) {
        if (!Character.isDigit(digit)) {
            throw new IllegalArgumentException("Invalid digit in symbol: " + digit);
        }
        return Character.digit(digit, 10);
    }

    /**
     * Container for parsed game state components.
     */
    public static final class GameState {
        private final Enterprise enterprise;
        private final Galaxy galaxy;

        public GameState(Enterprise enterprise, Galaxy galaxy) {
            this.enterprise = Objects.requireNonNull(enterprise, "Enterprise must not be null.");
            this.galaxy = Objects.requireNonNull(galaxy, "Galaxy must not be null.");
        }

        public Enterprise enterprise() {
            return this.enterprise;
        }

        public Galaxy galaxy() {
            return this.galaxy;
        }
    }
}
