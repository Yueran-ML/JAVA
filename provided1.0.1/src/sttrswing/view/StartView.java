package sttrswing.view;

import sttrswing.controller.GameController;
import sttrswing.model.interfaces.GameModel;
import sttrswing.view.panels.EnterpriseStatus;
import sttrswing.view.panels.Options;
import sttrswing.view.panels.QuadrantScan;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Objects;

/**
 * {@inheritDoc}
 */
public class StartView extends StandardLayoutView {

    private static final String TITLE = "Welcome";
    private static final String WELCOME_PANEL_TITLE = "Welcome";
    private static final String WELCOME_LABEL_TEXT = "Star Trek";
    private static final String WELCOME_MESSAGE =
            "WELCOME CAPTAIN\nClick the Start button to begin your mission.";

    private JButton button;
    private JTextArea textArea;

    public StartView(GameModel game, GameController controller) {
        super(TITLE);

        GameModel gameModel = Objects.requireNonNull(game, "game");
        GameController gameController = Objects.requireNonNull(controller, "controller");

        View welcomePanel = createWelcomePanel(gameModel, gameController);

        this.addViewPanel(welcomePanel)
                .addViewPanel(new EnterpriseStatus(gameModel))
                .addViewPanel(new QuadrantScan(gameModel))
                .addViewPanel(new Options(gameModel, gameController));
    }

    public JButton getButton() {
        return this.button;
    }

    public JTextArea getText() {
        return this.textArea;
    }

    private View createWelcomePanel(GameModel game, GameController controller) {
        View panel = new View(WELCOME_PANEL_TITLE);
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(WELCOME_LABEL_TEXT, SwingConstants.CENTER);
        label.setFont(new Font("Monospaced", Font.BOLD, 24));
        label.setForeground(Pallete.GREENTERMINAL.color());
        panel.add(label, BorderLayout.NORTH);

        this.textArea = new JTextArea();
        this.textArea.setEditable(false);
        this.textArea.setBackground(Pallete.BLACK.color());
        this.textArea.setForeground(Pallete.GREENPALE.color());
        this.textArea.setFont(new Font("Monospaced", Font.BOLD, 16));
        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);
        this.textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.textArea.setText(WELCOME_MESSAGE);
        panel.add(this.textArea, BorderLayout.CENTER);

        this.button = panel.buildButton("START", e -> controller.setDefaultView(game));
        this.button.setFocusPainted(false);
        panel.add(this.button, BorderLayout.SOUTH);

        return panel;
    }
}
