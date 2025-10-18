package sttrswing.view.panels;

import java.awt.GridLayout;
import java.util.Map;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import sttrswing.model.interfaces.GameModel;
import sttrswing.view.Pallete;
import sttrswing.view.View;

/**
 * Panel that displays the long-range scan of the quadrants surrounding the Enterprise.
 */
public class NearbyQuadrantScan extends View {

  private static final int GRID_ROWS = 3;
  private static final int GRID_COLUMNS = 3;
  private static final String UNKNOWN_SYMBOL = "???";
  private static final String ENTERPRISE_SYMBOL = " E ";

  /**
   * Creates a new {@link NearbyQuadrantScan} populated with data from the supplied game model.
   *
   * @param game the game model providing the surrounding quadrant information
   */
  public NearbyQuadrantScan(final GameModel game) {
    super("Long Range Scan");
    Objects.requireNonNull(game, "game");
    Map<String, String> surrounding =
        Objects.requireNonNull(game.getSurroundingQuadrants(), "surroundingQuadrants");

    this.setLayout(new GridLayout(GRID_ROWS, GRID_COLUMNS));
    this.setBackground(Pallete.BLACK.color());
    this.setOpaque(true);

    populateGrid(surrounding);
  }

  private void populateGrid(final Map<String, String> surrounding) {
    String[][] positions = {
      {"topLeft", "top", "topRight"},
      {"left", "center", "right"},
      {"bottomLeft", "bottom", "bottomRight"}
    };

    for (String[] row : positions) {
      for (String key : row) {
        String symbol = symbolForKey(surrounding, key);
        this.add(createCellPanel(symbol));
      }
    }
  }

  private String symbolForKey(final Map<String, String> surrounding, final String key) {
    if ("center".equals(key)) {
      return ENTERPRISE_SYMBOL;
    }

    String value = surrounding.get(key);
    if (value == null || value.isBlank()) {
      return UNKNOWN_SYMBOL;
    }
    return value;
  }

  private JPanel createCellPanel(final String symbol) {
    JPanel cell = new JPanel();
    cell.setLayout(new GridLayout(1, 1));
    cell.setBackground(Pallete.BLACK.color());
    cell.setBorder(BorderFactory.createLineBorder(Pallete.GREY.color()));
    cell.setOpaque(true);

    JLabel label = new JLabel(symbol, SwingConstants.CENTER);
    label.setOpaque(true);
    label.setBackground(Pallete.GREYDARK.color());
    label.setForeground(Pallete.GREENTERMINAL.color());
    label.setBorder(BorderFactory.createLineBorder(Pallete.GREENDARK.color()));

    cell.add(label);
    return cell;
  }
}
