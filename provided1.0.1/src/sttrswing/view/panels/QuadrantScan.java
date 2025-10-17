package sttrswing.view.panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Objects;

import sttrswing.model.enums.Faction;
import sttrswing.model.interfaces.GameModel;
import sttrswing.model.interfaces.HasFaction;
import sttrswing.model.interfaces.HasPosition;
import sttrswing.model.interfaces.HasSymbol;
import sttrswing.view.Pallete;
import sttrswing.view.View;
import sttrswing.view.guicomponents.MapSquare;

/**
 * A view representing the internal state of the quadrant the player is in as a grid of map
 * squares.
 */
public class QuadrantScan extends View {

    private static final int GRID_ROWS = 8;
    private static final int GRID_COLS = 8;
    private static final String EMPTY_SYMBOL = "   ";

    private final GameModel game;

    /**
     * Construct a new {@link QuadrantScan} using data from the provided {@link GameModel}.
     *
     * @param game the game model supplying quadrant information.
     */
    public QuadrantScan(GameModel game) {
        super("Quadrant Scan");
        this.game = Objects.requireNonNull(game, "game");
        this.setLayout(new GridLayout(GRID_ROWS, GRID_COLS));
        this.setBackground(Pallete.BLACK.color());
        this.setOpaque(true);
        this.populateGrid();
    }

    private void populateGrid() {
        MapSquare[][] squares = new MapSquare[GRID_ROWS][GRID_COLS];
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                squares[row][col] = buildEmptyMapSquare();
            }
        }

        var entities = game.getSymbolsForQuadrant();
        for (var entity : entities) {
            int x = entity.getX();
            int y = entity.getY();
            if (isWithinBounds(x, y)) {
                squares[y][x] = buildMapSquare(entity);
            }
        }

        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLS; col++) {
                this.add(squares[row][col]);
            }
        }
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < GRID_COLS && y >= 0 && y < GRID_ROWS;
    }

    /**
     * Public for test visibility. Builds a {@link MapSquare} representing an empty sector.
     *
     * @return a {@link MapSquare} with default styling representing an empty sector.
     */
    public MapSquare buildEmptyMapSquare() {
        MapSquare square = new MapSquare(EMPTY_SYMBOL);
        square.setOpaque(true);
        return square;
    }

    /**
     * Public for test visibility. Builds a {@link MapSquare} representing a populated sector.
     *
     * @param data entity data providing symbol, faction and position.
     * @param <T>  type that combines position, symbol and faction information.
     * @return a {@link MapSquare} displaying the entity information.
     */
    public <T extends HasPosition & HasSymbol & HasFaction> MapSquare buildMapSquare(T data) {
        Objects.requireNonNull(data, "data");
        Color highlight = colorForFaction(data.faction());
        MapSquare square = new MapSquare(data.symbol(), highlight);
        square.setOpaque(true);
        return square;
    }

    private Color colorForFaction(Faction faction) {
        if (faction == null) {
            return Pallete.GREY.color();
        }
        return switch (faction) {
            case FEDERATION -> Pallete.GREENTERMINAL.color();
            case KLINGON -> Pallete.RED.color();
            case NEUTRAL -> Pallete.YELLOW.color();
        };
    }
}
