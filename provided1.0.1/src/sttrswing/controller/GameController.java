package sttrswing.controller;

import sttrswing.model.interfaces.GameModel;
import sttrswing.model.Enterprise;
import sttrswing.model.Galaxy;
import sttrswing.view.StandardLayoutView;
import sttrswing.view.LoseGameView;
import sttrswing.view.StartView;
import sttrswing.view.View;
import sttrswing.view.WinGameView;
import sttrswing.view.panels.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class GameController extends JFrame {

    private View currentView;
    private final GameModel game;
    private final JMenu fileMenu;
    private final Dimension windowSize
    private String tittle;

    public GameController(Dimension windowSize, GameModel game) {
        super("Star Trek");
        this.windowSize = windowSize;
        this.game = game;

        this.setSize(windowSize);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        this.fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load");

        saveItem.addActionListener(e -> saveGame());
        loadItem.addActionListener(e -> loadGame());

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
    }

    public JMenu getFileMenu() {
        return this.fileMenu
    }

    public void end() {
        this.dispose();
        System.exit(0);
    }

    public void start(GameModel game) {
        StartView startView = new StartView(game, this);
        setView(startView);
        this.setVisible(true);
    }

    public void setWinGameView(GameModel game) {
        WinGameView winView = new WinGameView(game, this);
        setView(winView);
    }

    public void setLoseGameView(GameModel game) {
        LoseGameView loseView = new LoseGameView(game, this);
        setView(loseView);
    }

    public void setQuadrantNavigationView(GameModel game) {
        StandardLayoutView navLayout = new StandardLayoutView("Quadrant Navigation");
        navLayout.addViewPanel(new QuadrantScan(game))
                 .addViewPanel(new EnterpriseStatus(game))
                 .addViewPanel(new QuadrantNavigation(game, this))
                 .addViewPanel(new Options(game, this));
        setView(navLayout);
    }

    public void setCurrentQuadrantScanView(GameModel game) {
        StandardLayoutView scanLayout = new StandardLayoutView("Scanned Quadrant" + getQuadrantInfo)
    }

    public void setShieldsView(GameModel game) {}

    public View getView() {
        return this.currentView;
    }

    private void setView(View view) {
        if (this.currentView != null) {
            this.getContentPane().removeAll();
        }

        this.currentView = view;
        this.getContentPane().add(view);

        String viewTitle = getTitle();
        if (viewTitle != null && !viewTitle.isEmpty()) {
            this.setTitle("Star Trek |" + viewTitle);
        } else {
            this.setTitle("Star Trek");
        }

        this.revalidate();
        this.repaint();
    }

    private void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Star Trek Sava Files (*.txt)", )
    }
}
