package sttrswing.controller;

import sttrswing.model.Enterprise;
import sttrswing.model.Galaxy;
import sttrswing.model.interfaces.GameModel;
import sttrswing.view.LoseGameView;
import sttrswing.view.StandardLayoutView;
import sttrswing.view.StartView;
import sttrswing.view.View;
import sttrswing.view.WinGameView;
import sttrswing.view.panels.EnterpriseStatus;
import sttrswing.view.panels.NearbyQuadrantScan;
import sttrswing.view.panels.Options;
import sttrswing.view.panels.PhaserAttack;
import sttrswing.view.panels.QuadrantNavigation;
import sttrswing.view.panels.QuadrantScan;
import sttrswing.view.panels.Shield;
import sttrswing.view.panels.Torpedo;
import sttrswing.view.panels.WarpNavigation;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

public class GameController extends JFrame {

    private static final String DATA_DIR = "data";
    private static final String SAVE_FILE_EXTENSION = "trek";
    private static final String SAVE_FILE_DESCRIPTION = "Star Trek Save Files (*.trek)";
    private static final String DEFAULT_SAVE_FILE_NAME = "save";

    private View currentView;
    private final GameModel game;
    private final JMenu fileMenu;
    private final Dimension windowSize;
    private String title = "";

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
        return this.fileMenu;
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
        setTitle("Quadrant Navigation");
        StandardLayoutView navLayout = new StandardLayoutView("Quadrant Navigation");
        navLayout.addViewPanel(new QuadrantScan(game))
                .addViewPanel(new EnterpriseStatus(game))
                .addViewPanel(new QuadrantNavigation(game, this))
                .addViewPanel(new Options(game, this));
        setView(navLayout);
    }

    public void setCurrentQuadrantScanView(GameModel game) {
        game.scanQuadrant();
        String report = game.lastActionReport();
        String title = (report == null || report.isBlank()) ? "Scanned Quadrant" : report;
        setTitle(title);
        StandardLayoutView scanLayout = new StandardLayoutView(title);
        scanLayout.addViewPanel(new QuadrantScan(game))
                .addViewPanel(new EnterpriseStatus(game))
                .addViewPanel(new Options(game, this));
        setView(scanLayout);
    }

    public void setScanNearbyQuadrantView(GameModel game) {
        setTitle("Long Range Scan");
        StandardLayoutView scanLayout = new StandardLayoutView("Long Range Scan");
        scanLayout.addViewPanel(new QuadrantScan(game))
                .addViewPanel(new EnterpriseStatus(game))
                .addViewPanel(new NearbyQuadrantScan(game))
                .addViewPanel(new Options(game, this));
        setView(scanLayout);
    }

    public void setWarpNavigationView(GameModel game) {
        setTitle("Warp Controls");
        StandardLayoutView warpLayout = new StandardLayoutView("Warp Controls");
        warpLayout.addViewPanel(new QuadrantScan(game))
                .addViewPanel(new EnterpriseStatus(game))
                .addViewPanel(new WarpNavigation(game, this))
                .addViewPanel(new Options(game, this));
        setView(warpLayout);
    }

    public void setPhaserAttackView(GameModel game) {
        setTitle("Phaser Controls");
        StandardLayoutView phaserLayout = new StandardLayoutView("Phaser Controls");
        phaserLayout.addViewPanel(new QuadrantScan(game))
                .addViewPanel(new EnterpriseStatus(game))
                .addViewPanel(new PhaserAttack(game, this))
                .addViewPanel(new Options(game, this));
        setView(phaserLayout);
    }

    public void setTorpedoView(GameModel game) {
        setTitle("Torpedo Controls");
        StandardLayoutView torpedoLayout = new StandardLayoutView("Torpedo Controls");
        torpedoLayout.addViewPanel(new QuadrantScan(game))
                .addViewPanel(new EnterpriseStatus(game))
                .addViewPanel(new Torpedo(game, this))
                .addViewPanel(new Options(game, this));
        setView(torpedoLayout);
    }

    public void setShieldsView(GameModel game) {
        setTitle("Shield Controls");
        StandardLayoutView shieldLayout = new StandardLayoutView("Shield Controls");
        shieldLayout.addViewPanel(new QuadrantScan(game))
                .addViewPanel(new EnterpriseStatus(game))
                .addViewPanel(new Shield(game, this))
                .addViewPanel(new Options(game, this));
        setView(shieldLayout);
    }

    public void setDefaultView(GameModel game) {
        if (game.hasWon()) {
            setWinGameView(game);
            return;
        }
        if (game.hasLost()) {
            setLoseGameView(game);
            return;
        }

        setTitle("Default");
        StandardLayoutView defaultLayout = new StandardLayoutView("Default");
        defaultLayout.addViewPanel(new QuadrantScan(game))
                .addViewPanel(new EnterpriseStatus(game))
                .addViewPanel(new QuadrantNavigation(game, this))
                .addViewPanel(new Options(game, this));
        setView(defaultLayout);
    }

    @Override
    public void setTitle(String title) {
        this.title = title == null ? "" : title;
        if (this.title.isEmpty()) {
            super.setTitle("Star Trek");
        } else {
            super.setTitle("Star Trek | " + this.title);
        }
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    public View getView() {
        return this.currentView;
    }

    private void setView(View view) {
        if (this.currentView != null) {
            this.getContentPane().removeAll();
        }

        this.currentView = view;
        this.getContentPane().add(view);

        if (this.title != null && !this.title.isEmpty()) {
            super.setTitle("Star Trek | " + this.title);
        } else {
            super.setTitle("Star Trek");
        }

        this.revalidate();
        this.repaint();
    }

    private void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter(SAVE_FILE_DESCRIPTION,
                SAVE_FILE_EXTENSION));

        File projectRoot = new File(System.getProperty("user.dir"));
        File dataDirectory = new File(projectRoot, DATA_DIR);
        fileChooser.setCurrentDirectory(projectRoot);
        fileChooser.setSelectedFile(new File(dataDirectory, DEFAULT_SAVE_FILE_NAME
                + "." + SAVE_FILE_EXTENSION));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();
        if (!selectedFile.getName().toLowerCase().endsWith("." + SAVE_FILE_EXTENSION)) {
            selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName()
                    + "." + SAVE_FILE_EXTENSION);
        }

        try {
            File canonicalDataDir = dataDirectory.getCanonicalFile();
            File canonicalSelectedFile = selectedFile.getCanonicalFile();
            if (canonicalSelectedFile.getParentFile() == null
                    || !canonicalSelectedFile.getParentFile().equals(canonicalDataDir)) {
                JOptionPane.showMessageDialog(this,
                        "Save files must be stored inside the data directory.",
                        "Invalid Save Location",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String exported = this.game.export();
            GameSaver.save(canonicalSelectedFile.getAbsolutePath(), exported);
            JOptionPane.showMessageDialog(this,
                    "Game saved successfully.",
                    "Save Game",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to save game: " + ex.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter(SAVE_FILE_DESCRIPTION,
                SAVE_FILE_EXTENSION));

        File projectRoot = new File(System.getProperty("user.dir"));
        File dataDirectory = new File(projectRoot, DATA_DIR);
        if (dataDirectory.exists()) {
            fileChooser.setCurrentDirectory(dataDirectory);
        } else {
            fileChooser.setCurrentDirectory(projectRoot);
        }

        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();
        if (selectedFile == null) {
            return;
        }

        if (!selectedFile.getName().toLowerCase().endsWith("." + SAVE_FILE_EXTENSION)) {
            JOptionPane.showMessageDialog(this,
                    "Please select a ." + SAVE_FILE_EXTENSION + " save file.",
                    "Invalid File",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File canonicalDataDir = dataDirectory.getCanonicalFile();
            File canonicalSelectedFile = selectedFile.getCanonicalFile();
            if (canonicalSelectedFile.getParentFile() == null
                    || !canonicalSelectedFile.getParentFile().equals(canonicalDataDir)) {
                JOptionPane.showMessageDialog(this,
                        "Save files must be loaded from the data directory.",
                        "Invalid Save Location",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String content = GameLoader.load(canonicalSelectedFile.getAbsolutePath());
            GameLoader.GameState loadedState = GameLoader.parse(content);
            Enterprise enterprise = loadedState.enterprise();
            Galaxy galaxy = loadedState.galaxy();
            this.game.load(enterprise, galaxy);
            setDefaultView(this.game);
            JOptionPane.showMessageDialog(this,
                    "Game loaded successfully.",
                    "Load Game",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to load game: " + ex.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
