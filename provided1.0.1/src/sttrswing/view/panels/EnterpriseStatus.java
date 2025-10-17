package sttrswing.view.panels;

import java.awt.BorderLayout;
import java.util.Objects;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import sttrswing.model.interfaces.GameModel;
import sttrswing.model.interfaces.HasPosition;
import sttrswing.view.Pallete;
import sttrswing.view.View;

/**
 * Panel that displays the Enterprise's vital statistics in a tabular layout.
 */
public class EnterpriseStatus extends View {

    private static final String TITLE = "Enterprise Status";
    private static final String[] COLUMN_NAMES = {"Stat", "Value"};

    public EnterpriseStatus(GameModel game) {
        super(TITLE);
        GameModel gameModel = Objects.requireNonNull(game, "game");
        this.setLayout(new BorderLayout());
        this.setBackground(Pallete.BACKGROUND_COLOR);
        this.setOpaque(true);

        JTable table = buildStatusTable(gameModel);
        JTableHeader header = table.getTableHeader();
        header.setBackground(Pallete.BACKGROUND_COLOR);
        header.setForeground(Pallete.TEXT_COLOR);
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);
        header.setOpaque(true);

        this.add(header, BorderLayout.NORTH);
        this.add(table, BorderLayout.CENTER);
    }

    private JTable buildStatusTable(GameModel game) {
        Object[][] data = new Object[][]{
                {"Quadrant (X,Y)", formatPosition(game.galaxyPosition())},
                {"Sector (X,Y)", formatPosition(game.playerPosition())},
                {"Torpedoes", game.spareTorpedoes()},
                {"Energy", game.playerEnergy()},
                {"Shields", game.playerShields()},
                {"Klingons left in Galaxy", game.totalKlingonCount()},
                {"Starbases left in Galaxy", game.totalStarbaseCount()}
        };

        DefaultTableModel model = new DefaultTableModel(data, COLUMN_NAMES) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setBackground(Pallete.BACKGROUND_COLOR);
        table.setForeground(Pallete.TEXT_COLOR);
        table.setGridColor(Pallete.GRID_COLOR);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setFocusable(false);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setOpaque(true);
        table.setFillsViewportHeight(true);
        table.setDefaultEditor(Object.class, null);
        return table;
    }

    private String formatPosition(HasPosition position) {
        if (position == null) {
            return "(?,?)";
        }
        return "(" + position.getX() + "," + position.getY() + ")";
    }
}

