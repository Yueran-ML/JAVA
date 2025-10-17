package sttrswing.view.panels;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import sttrswing.controller.GameController;
import sttrswing.model.interfaces.GameModel;
import sttrswing.view.Pallete;

/**
 * Panel that allows the player to initiate warp travel between quadrants.
 */
public class WarpNavigation extends JPanel {

    private static final int ROWS = 4;
    private static final int COLUMNS = 2;
    private static final int FIELD_COLUMNS = 10;

    private final GameModel game;
    private final GameController controller;
    private final JTextField courseField;
    private final JTextField warpFactorField;

    /**
     * Creates a new {@link WarpNavigation} panel that provides inputs for warp travel.
     *
     * @param game the game model used to perform warp navigation
     * @param controller the controller that can switch back to the default view
     */
    public WarpNavigation(GameModel game, GameController controller) {
        this.game = Objects.requireNonNull(game, "game");
        this.controller = Objects.requireNonNull(controller, "controller");

        setLayout(new GridLayout(ROWS, COLUMNS));
        setBackground(Pallete.BLACK.color());
        setOpaque(true);

        this.courseField = new JTextField(FIELD_COLUMNS);
        this.warpFactorField = new JTextField(FIELD_COLUMNS);

        add(new JLabel("Course"));
        add(this.courseField);
        add(new JLabel("Warp Factor"));
        add(this.warpFactorField);

        add(new JLabel());
        add(new JLabel());

        JButton engageButton = buildEngageButton();
        add(new JLabel());
        add(engageButton);
    }

    private JButton buildEngageButton() {
        JButton engageButton = new JButton("Engage");
        engageButton.setBackground(Pallete.BLUE.color());
        engageButton.setForeground(Pallete.WHITE.color());
        ActionListener listener = event -> {
            try {
                int course = Integer.parseInt(courseField.getText().trim());
                double warpFactor = Double.parseDouble(warpFactorField.getText().trim());
                game.moveBetweenQuadrants(course, warpFactor);
                controller.setDefaultView(game);
            } catch (NumberFormatException ignored) {
                courseField.setText("");
                warpFactorField.setText("");
                courseField.requestFocusInWindow();
            }
        };
        engageButton.addActionListener(listener);
        return engageButton;
    }
}
