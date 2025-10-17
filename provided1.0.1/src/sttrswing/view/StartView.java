package sttrswing.view;

import sttrswing.model.interfaces.GameModel;

import javax.swing.*;

public class StartView extends View {
    private JButton button;
    private JTextArea textArea;

    public StartView(GameModel game, GameController controller) {
        super("Welcome");

        View welcomePanel = createWelcomePanel(game, controller);

        this.addViewPanel(welcomePanel)
        this.addViewPanel
    }
}
