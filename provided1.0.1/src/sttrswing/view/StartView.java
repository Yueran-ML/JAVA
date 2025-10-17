package sttrswing.view;

import sttrswing.model.interfaces.GameModel;
import sttrswing.controller.GameController;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import javax.swing.*;

public class StartView extends View {
    private JButton button;
    private JTextArea textArea;

    public StartView(GameModel game, GameController controller) {
        super("Welcome");

        View welcomePanel = createWelcomePanel(game, controller);

        this.addViewPanel(welcomePanel)
                .addViewPanel(new EnterpriseStatus(game))
                .addViewPanel(new QuadrantScan(game))
                .addViewPanel(new Options(game, controller));
    }

    private View createWelcomePanel(GameModel game, GameController controller) {
        View panel = new View("welcome");
        panel.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(Pallete.BLACK.color());
        textArea.setForeground(Pallete.GREENPALE.color());
        textArea.setFont(new Font("Monospaced", Font.BOLD, 16));
        textArea.setText("WELCOME CAPTAIN Click the Start button to start the game!");

        button = panel.buildButton("START", e ->controller.setDefaultView(game));
    }
}
