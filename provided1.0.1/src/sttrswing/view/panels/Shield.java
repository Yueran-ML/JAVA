package sttrswing.view.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.JButton;
import sttrswing.controller.GameController;
import sttrswing.model.interfaces.GameModel;
import sttrswing.view.Pallete;
import sttrswing.view.View;
import sttrswing.view.guicomponents.Slider;

/**
 * Panel that allows the player to redistribute energy into the Enterprise's shields.
 */
public class Shield extends View {

    private static final String TITLE = "Shield";
    private static final int DEFAULT_INCREMENT = 100;

    private final GameModel game;
    private final GameController controller;
    private final Slider slider;

    /**
     * Creates a new {@link Shield} panel that lets the player adjust shield energy.
     *
     * @param game the current game model supplying energy information
     * @param controller the controller used to return to the default view once shields are set
     */
    public Shield(GameModel game, GameController controller) {
        super(TITLE);
        this.game = Objects.requireNonNull(game, "game");
        this.controller = Objects.requireNonNull(controller, "controller");

        setLayout(new BorderLayout());
        setBackground(Pallete.BLACK.color());
        setOpaque(true);

        int maximumEnergy = Math.max(1, this.game.playerEnergy());
        this.slider = new Slider(maximumEnergy, DEFAULT_INCREMENT);
        add(this.slider, BorderLayout.CENTER);

        JButton adjustButton = buildAdjustButton();
        add(adjustButton, BorderLayout.EAST);
    }

    private JButton buildAdjustButton() {
        JButton adjustButton = new JButton("Adjust Shields!");
        adjustButton.setBackground(Pallete.BLUE.color());
        adjustButton.setForeground(Pallete.WHITE.color());
        ActionListener listener = event -> {
            int selectedEnergy = slider.getSelectedValue();
            game.shields(selectedEnergy);
            controller.setDefaultView(new QuadrantNavigation(game, controller));
        };
        adjustButton.addActionListener(listener);
        trackButton(adjustButton);
        trackListener(listener);
        return adjustButton;
    }

    /**
     * Returns the slider used to select the shield energy allocation. Intended for testing.
     *
     * @return the slider component contained within this panel
     */
    public Slider getSlider() {
        return slider;
    }
}
