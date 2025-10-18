package sttrswing.view.panels;

import java.awt.GridLayout;
import java.util.Objects;

import javax.swing.JButton;
import sttrswing.controller.GameController;
import sttrswing.model.interfaces.GameModel;
import sttrswing.view.Pallete;
import sttrswing.view.View;

/**
 * Panel presenting navigation and combat options for the player.
 */
public class Options extends View {

    private static final int ROWS = 7;
    private static final int COLUMNS = 1;
    private static final int H_GAP = 0;
    private static final int V_GAP = 5;

    private final GameModel game;
    private final GameController controller;

    public Options(GameModel game, GameController controller) {
        super("Options");
        this.game = Objects.requireNonNull(game, "game");
        this.controller = Objects.requireNonNull(controller, "controller");

        this.setLayout(new GridLayout(ROWS, COLUMNS, H_GAP, V_GAP));
        this.setBackground(Pallete.BLACK.color());
        this.setOpaque(true);

        addOptionButton("Quadrant Navigation", () -> this.controller.setQuadrantNavigationView(this.game));
        addOptionButton("Warp Navigation", () -> this.controller.setWarpNavigationView(this.game));
        addOptionButton("Phasers", () -> this.controller.setPhaserAttackView(this.game));
        addOptionButton("10 x Torpedoes", () -> this.controller.setTorpedoView(this.game));
        addOptionButton("Shields", () -> this.controller.setShieldsView(this.game));
        addOptionButton("Short Range Scan", () -> this.controller.setCurrentQuadrantScanView(this.game));
        addOptionButton("Long Range Scan", () -> this.controller.setScanNearbyQuadrantView(this.game));
    }

    private void addOptionButton(String label, Runnable action) {
        JButton button = new JButton(label);
        button.setBackground(Pallete.GREY.color());
        button.setForeground(Pallete.BLACK.color());
        button.addActionListener(event -> action.run());
        button.setFocusPainted(false);
        button.setOpaque(true);
        this.add(button);
    }
}

