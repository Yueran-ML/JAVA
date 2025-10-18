package sttrswing.view.panels;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.JPanel;
import sttrswing.controller.GameController;
import sttrswing.model.interfaces.GameModel;
import sttrswing.view.Pallete;
import sttrswing.view.View;
import sttrswing.view.guicomponents.DirectionButton;

/**
 * Panel that allows the player to fire torpedoes in any of the eight compass directions.
 */
public class Torpedo extends View {

  private static final int ROWS = 3;
  private static final int COLUMNS = 3;

  private final GameModel game;
  private final GameController controller;

  /**
   * Creates a new {@link Torpedo} panel.
   *
   * @param game the game model providing torpedo firing capabilities
   * @param controller the controller used to restore the default view
   */
  public Torpedo(final GameModel game, final GameController controller) {
    super("Torpedo Controls");
    this.game = Objects.requireNonNull(game, "game");
    this.controller = Objects.requireNonNull(controller, "controller");

    setLayout(new GridLayout(ROWS, COLUMNS));
    setBackground(Pallete.BLACK.color());
    setOpaque(true);

    add(createDirectionButton(4));
    add(createDirectionButton(3));
    add(createDirectionButton(2));
    add(createDirectionButton(5));
    add(createCenterPlaceholder());
    add(createDirectionButton(1));
    add(createDirectionButton(6));
    add(createDirectionButton(7));
    add(createDirectionButton(8));
  }

  private JPanel createCenterPlaceholder() {
    JPanel placeholder = new JPanel();
    placeholder.setOpaque(false);
    return placeholder;
  }

  private DirectionButton createDirectionButton(final int course) {
    DirectionButton button = new DirectionButton(course, true);
    button.setBackground(Pallete.GREY.color());
    button.setForeground(Pallete.WHITE.color());
    button.setFocusPainted(false);
    ActionListener listener = event -> {
      game.fireTorpedo(course);
      controller.setDefaultView(new QuadrantNavigation(game, controller));
    };
    button.addActionListener(listener);
    return button;
  }
}
