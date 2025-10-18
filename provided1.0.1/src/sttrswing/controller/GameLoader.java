package sttrswing.controller;

import sttrswing.model.Enterprise;
import sttrswing.model.Galaxy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GameLoader {
    private final String path;
    private String content;
    private Boolean succeeded = false;

    public GameLoader(String path) {
        this.path = path;
    }

    public void load() {
        try {
            this.content = Files.readString(Path.of(this.path), StandardCharsets.UTF_8);
            this.succeeded = true;
        } catch (Exception e) {
            this.content = null;
            this.succeeded = false;
        }
    }

    public Boolean success() {
        return Boolean.TRUE.equals(this.succeeded);
    }

    public Enterprise buildEnterprise() {
        if (!success() || this.content == null) throw new IllegalStateException("No content loaded");
        String line = enterpriseLine();
        if (line == null) throw new IllegalStateException("Enterprise line missing");
        try {
            int x = parseLineForX(line);
            int y = parseLineForY(line);
            int e = parseLineForEnergy(line);
            int s = parseLineForShields(line);
            int t = parseLineForTorpedoes(line);
            try {
                var ctor = Enterprise.class.getDeclaredConstructor(int.class, int.class, int.class, int.class, int.class);
                ctor.setAccessible(true);
                return ctor.newInstance(x, y, e, s, t);
            } catch (NoSuchMethodException ex) {
                try {
                    var ctor0 = Enterprise.class.getDeclaredConstructor();
                    ctor0.setAccessible(true);
                    return ctor0.newInstance();
                } catch (Exception ex2) {
                    throw new IllegalStateException("No suitable Enterprise constructor", ex2);
                }
            }
        } catch (IOException ioe) {
            throw new IllegalStateException("Failed to parse enterprise line", ioe);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to construct Enterprise", e);
        }
    }

    public Galaxy buildGalaxy() {
        if (!success() || this.content == null) throw new IllegalStateException("No content loaded");
        try {
            Galaxy g;
            try {
                var ctor0 = Galaxy.class.getDeclaredConstructor();
                ctor0.setAccessible(true);
                g = ctor0.newInstance();
            } catch (NoSuchMethodException ex) {
                g = null;
            } catch (Exception ex) {
                throw new IllegalStateException("Failed to construct Galaxy", ex);
            }
            ArrayList<String> lines = galaxyLines();
            if (g != null) {
                for (String line : lines) {
                    int x = parseLineForX(line);
                    int y = parseLineForY(line);
                    Map<String, Integer> m = parseLineForQuadrantSymbol(line);
                    int stars = m.getOrDefault("stars", 0);
                    int klingons = m.getOrDefault("klingons", 0);
                    int starbases = m.getOrDefault("starbases", 0);
                    boolean set = false;
                    try {
                        var m1 = Galaxy.class.getMethod("addQuadrant", int.class, int.class, int.class, int.class, int.class);
                        m1.invoke(g, x, y, stars, klingons, starbases);
                        set = true;
                    } catch (NoSuchMethodException ignore) {}
                    if (!set) {
                        try {
                            var m2 = Galaxy.class.getMethod("setQuadrant", int.class, int.class, int.class, int.class, int.class);
                            m2.invoke(g, x, y, stars, klingons, starbases);
                            set = true;
                        } catch (NoSuchMethodException ignore) {}
                    }
                }
            }
            if (g == null) {
                try {
                    var ctor = Galaxy.class.getDeclaredConstructor(int.class, int.class, int.class, int.class, int.class);
                    ctor.setAccessible(true);
                    String first = lines.isEmpty() ? "" : lines.get(0);
                    int x = parseLineForX(first);
                    int y = parseLineForY(first);
                    Map<String, Integer> m = parseLineForQuadrantSymbol(first);
                    int stars = m.getOrDefault("stars", 0);
                    int klingons = m.getOrDefault("klingons", 0);
                    int starbases = m.getOrDefault("starbases", 0);
                    return ctor.newInstance(x, y, stars, klingons, starbases);
                } catch (Exception ex) {
                    throw new IllegalStateException("No suitable Galaxy constructor", ex);
                }
            }
            return g;
        } catch (IOException ioe) {
            throw new IllegalStateException("Failed to parse galaxy lines", ioe);
        }
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

    public Map<String,Integer> parseLineForQuadrantSymbol(String line) throws IOException {
        if (line == null) throw new IOException("line is null");
        String lower = line.toLowerCase(Locale.ROOT);
        int idx = lower.indexOf("s:");
        if (idx < 0) throw new IOException("symbol section missing");
        int start = idx + 2;
        int end = start;
        while (end < lower.length() && Character.isDigit(lower.charAt(end))) end++;
        if (end == start) throw new IOException("no digits after s:");
        String digits = lower.substring(start, end);
        HashMap<String,Integer> map = new HashMap<>();
        int[] vals = new int[]{0,0,0};
        for (int i = digits.length() - 1, pos = 0; i >= 0 && pos < 3; i--, pos++) {
            char c = digits.charAt(i);
            if (!Character.isDigit(c)) throw new IOException("non-digit in s: field");
            vals[pos] = c - '0';
        }
        map.put("starbases", vals[0]);
        map.put("klingons", vals[1]);
        map.put("stars", vals[2]);
        return map;
    }

    public String enterpriseLine() {
        if (this.content == null) return null;
        String[] lines = this.content.split("\\R");
        for (String line : lines) {
            if (line.trim().startsWith("[e]")) return line;
        }
        return null;
    }

    public ArrayList<String> galaxyLines() {
        ArrayList<String> result = new ArrayList<>();
        if (this.content == null) return result;
        String[] lines = this.content.split("\\R");
        for (String line : lines) {
            if (line.trim().startsWith("[q]")) result.add(line);
        }
        return result;
    }

    public String toString() {
        return "GameLoader{path=" + path + ", success=" + success() + "}";
    }

    private int parseIntegerValue(String line, String token) throws IOException {
        if (line == null) throw new IOException("line is null");
        String lower = line.toLowerCase(Locale.ROOT);
        int idx = lower.indexOf(token);
        if (idx < 0) throw new IOException("token missing: " + token);
        int start = idx + token.length();
        int end = start;
        while (end < lower.length() && Character.isDigit(lower.charAt(end))) end++;
        if (end == start) throw new IOException("no digits after token: " + token);
        try {
            return Integer.parseInt(lower.substring(start, end));
        } catch (NumberFormatException e) {
            throw new IOException("invalid number after token: " + token, e);
        }
    }
}

