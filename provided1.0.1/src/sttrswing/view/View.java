package sttrswing.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class View extends JPanel {
    private String tittle;
    private JLabel lastlable;
    private ArrayList<JButton> buttons;
    private ArrayList<ActionListener> listeners;



    public View(String tittle) {
        this.tittle = tittle;
        this.buttons = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.setBackground(Pallete.BLACK.color());
    }

    public void addLable(JLabel label) {
        label.setFont(new Font("Monospaced", Font.BOLD, 14));
        label.setForeground(Pallete.GREENTERMINAL.color());
        this.lastlable = label;
        this.add(label);
    }

    public JButton buildButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.setFont(new Font("Monospaced", Font.BOLD, 12));
        button.setBackground(Pallete.GREY.color());
        button.setForeground(Pallete.BLACK.color());
        button.addActionListener(listener);
        this.trackButton(button);
        this.trackListener(listener);
        return button;
    }

    public void cleanup() {
        for (int i = 0; i < buttons.size(); i++) {
            JButton button = buttons.get(i);
            ActionListener listener = listeners.get(i);
            button.removeActionListener(listener);
        }
        buttons.clear();
        listeners.clear();
    }

    public ArrayList<JButton> getButtons() {
        return buttons;
    }

    public JLabel getLabel() {
        return lastlable;
    }

    public ArrayList<ActionListener> getListeners() {
        return listeners;
    }

    public String getTittle() {
        return tittle;
    }

    public void trackButton(JButton button) {
        this.buttons.add(button);
    }

    public void trackListener(ActionListener action) {
        this.listeners.add(action);
    }
}
