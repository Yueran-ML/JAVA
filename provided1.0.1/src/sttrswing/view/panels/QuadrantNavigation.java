package sttrswing.view.panels;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import javax.swing.JPanel;
import sttrswing.controller.GameController;
import sttrswing.model.interfaces.GameModel;
import sttrswing.view.Pallete;
import sttrswing.view.guicomponents.MapSquare;

/**
 * Panel that allows in-quadrant navigation by clicking on a sector of the quadrant grid.
 */
public class QuadrantNavigation extends JPanel {

  private static final int GRID_SIZE = 8;
  private static final String EMPTY_SYMBOL = "   ";

  private final GameModel game;
  private final GameController controller;

  /**
   * Creates a new {@link QuadrantNavigation} panel using the provided game state and controller.
   *
   * @param game       the game model containing the quadrant information.
   * @param controller the controller used to switch back to the default view after movement.
   */
  public QuadrantNavigation(final GameModel game, final GameController controller) {
    this.game = Objects.requireNonNull(game, "game");
    this.controller = Objects.requireNonNull(controller, "controller");
    this.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
    this.setBackground(Pallete.BLACK.color());
    this.setOpaque(true);
    this.populateGrid();
  }

  private void populateGrid() {
    for (int y = 0; y < GRID_SIZE; y++) {
      for (int x = 0; x < GRID_SIZE; x++) {
        MapSquare square = createSquare(x, y);
        this.add(square);
      }
    }
  }

  private MapSquare createSquare(final int targetX, final int targetY) {
    MapSquare square = new MapSquare(EMPTY_SYMBOL);
    square.setOpaque(true);
    ActionListener listener = event -> handleNavigation(targetX, targetY);
    square.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(final MouseEvent e) {
        listener.actionPerformed(new ActionEvent(square, ActionEvent.ACTION_PERFORMED, "click"));
      }
    });
    return square;
  }

  private void handleNavigation(final int targetX, final int targetY) {
    int startX = this.game.playerPosition().getX();
    int startY = this.game.playerPosition().getY();
    int deltaX = targetX - startX;
    int deltaY = targetY - startY;
    double distance = Math.hypot(deltaX, deltaY);

    if (distance > 0) {
      int course = calculateCourse(deltaX, deltaY);
      this.game.moveWithinQuadrant(course, distance);
    }
    this.controller.setDefaultView(this.game);
  }

  private int calculateCourse(final int deltaX, final int deltaY) {
    double angle = Math.toDegrees(Math.atan2(-deltaY, deltaX));
    if (angle < 0) {
      angle += 360;
    }
    int index = (int) Math.floor((angle + 22.5) / 45.0);
    return (index % 8) + 1;
  }
}
