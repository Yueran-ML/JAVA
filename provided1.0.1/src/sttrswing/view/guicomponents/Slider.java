package sttrswing.view.guicomponents;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import sttrswing.view.Pallete;

/**
 * A simple list based slider component that exposes descending integer options using a
 * {@link javax.swing.JList} within a {@link javax.swing.JScrollPane}.
 */
public class Slider extends JPanel {

  private final JList<Integer> valueList;

  /**
   * Creates a slider that descends from the supplied maximum value in steps defined by the
   * provided increment.
   *
   * @param maximumValue the largest selectable value
   * @param increment the difference between consecutive slider values
   */
  public Slider(final int maximumValue, final int increment) {
    super(new BorderLayout());
    if (maximumValue < 1) {
      throw new IllegalArgumentException("maximumValue must be at least 1");
    }
    if (increment < 1) {
      throw new IllegalArgumentException("increment must be at least 1");
    }

    final Integer[] values = buildValues(maximumValue, increment);

    valueList = new JList<>(values);
    valueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    valueList.setBackground(Pallete.BACKGROUND_COLOR);
    valueList.setForeground(Pallete.TEXT_COLOR);
    valueList.setSelectionBackground(Pallete.SELECTION_BACKGROUND_COLOR);
    valueList.setSelectionForeground(Pallete.SELECTION_TEXT_COLOR);
    if (values.length > 0) {
      valueList.setSelectedIndex(0);
    }

    final JScrollPane scrollPane = new JScrollPane(valueList);
    scrollPane.getViewport().setBackground(Pallete.BACKGROUND_COLOR);

    setLayout(new BorderLayout());
    setBackground(Pallete.BACKGROUND_COLOR);
    add(scrollPane, BorderLayout.CENTER);
  }

  private static Integer[] buildValues(final int maximumValue, final int increment) {
    final List<Integer> values = new ArrayList<>();

    int current = Math.max(maximumValue, 1);
    while (current > 1) {
      values.add(current);
      current -= increment;
      if (current < 1) {
        current = 1;
      }
    }
    if (values.isEmpty() || values.get(values.size() - 1) != 1) {
      values.add(1);
    }

    return values.toArray(new Integer[0]);
  }

  /**
   * Returns the selected integer value from the slider, or zero if no value is selected.
   *
   * @return the selected integer value or zero if none selected
   */
  public int getSelectedValue() {
    final Integer selected = valueList.getSelectedValue();
    if (selected != null) {
      return selected;
    }
    return valueList.getModel().getSize() > 0 ? valueList.getModel().getElementAt(0) : 0;
  }
}
