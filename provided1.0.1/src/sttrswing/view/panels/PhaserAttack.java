package sttrswing.view.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JPanel;
import sttrswing.controller.GameController;
import sttrswing.model.interfaces.GameModel;
import sttrswing.view.Pallete;
import sttrswing.view.guicomponents.Slider;

/**
 * Panel that allows the player to allocate energy to the starship's phasers and fire them.
 */
public class PhaserAttack extends JPanel {

  private static final int DEFAULT_INCREMENT = 100;

  private final GameModel game;
  private final GameController controller;
  private final Slider slider;

  /**
   * Creates a new {@link PhaserAttack} panel for adjusting phaser energy.
   *
   * @param game the current game model providing available energy
   * @param controller the controller used to restore the default view
   */
  public PhaserAttack(final GameModel game, final GameController controller) {
    this.game = Objects.requireNonNull(game, "game");
    this.controller = Objects.requireNonNull(controller, "controller");

    setLayout(new BorderLayout());
    setBackground(Pallete.BLACK.color());
    setOpaque(true);

    int maximumEnergy = Math.max(1, this.game.spareEnergy());
    this.slider = new Slider(maximumEnergy, DEFAULT_INCREMENT);
    add(this.slider, BorderLayout.CENTER);

    JButton fireButton = buildFireButton();
    add(fireButton, BorderLayout.EAST);
  }

  private JButton buildFireButton() {
    JButton fireButton = new JButton("Fire Phasers!");
    fireButton.setBackground(Pallete.BLUE.color());
    fireButton.setForeground(Pallete.WHITE.color());
    ActionListener listener = event -> {
      int selectedEnergy = slider.getSelectedValue();
      game.firePhasers(selectedEnergy);
      controller.setDefaultView(game);
    };
    fireButton.addActionListener(listener);
    return fireButton;
  }
}
