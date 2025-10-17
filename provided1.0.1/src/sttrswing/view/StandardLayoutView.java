package sttrswing.view;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * A view that arranges up to four child views in a 2x2 grid layout.
 */
public class StandardLayoutView extends View {

    private static final int MAX_PANELS = 4;

    private final ArrayList<View> viewPanels;

    public StandardLayoutView(String title) {
        super(title);
        this.viewPanels = new ArrayList<>(MAX_PANELS);
        this.setLayout(new GridLayout(2, 2, 5, 5));
    }

    public StandardLayoutView addViewPanel(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View cannot be null");
        }
        if (this.viewPanels.size() >= MAX_PANELS) {
            throw new IllegalStateException("StandardLayoutView can only contain up to " + MAX_PANELS + " panels");
        }
        this.viewPanels.add(view);
        this.add(view);
        return this;
    }

    public StandardLayoutView addViewPanel(JPanel panel) {
        if (panel instanceof View viewPanel) {
            return addViewPanel(viewPanel);
        }
        throw new IllegalArgumentException("Panel must extend View");
    }

    public ArrayList<View> getViewPanels() {
        return new ArrayList<>(this.viewPanels);
    }
}
