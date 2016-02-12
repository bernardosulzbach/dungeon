package org.mafagafogigante.dungeon.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

class DungeonScrollBarUi extends BasicScrollBarUI {

  private static final Dimension ZERO_DIMENSION = new Dimension(0, 0);
  private static final int THUMB_MARGIN = 4;
  private static final int THUMB_WIDTH = 4;

  /**
   * This is a temporary solution to remove the buttons from BasicScrollBarUI.
   *
   * @return a JButton with all sizes set to zero
   */
  private static JButton createZeroButton() {
    JButton button = new JButton();
    button.setPreferredSize(ZERO_DIMENSION);
    button.setMinimumSize(ZERO_DIMENSION);
    button.setMaximumSize(ZERO_DIMENSION);
    return button;
  }

  @Override
  protected void paintTrack(Graphics graphics, JComponent component, Rectangle trackBounds) {
  }

  @Override
  protected void paintThumb(Graphics graphics, JComponent component, Rectangle thumbBounds) {
    if (thumbBounds.isEmpty() || !component.isEnabled()) {
      return;
    }
    graphics.setColor(SharedConstants.MARGIN_COLOR);
    int topLeftX = thumbBounds.width - THUMB_WIDTH - THUMB_MARGIN;
    int topLeftY = thumbBounds.y + THUMB_MARGIN;
    int height = thumbBounds.height - THUMB_MARGIN * 2;
    graphics.fillRect(topLeftX, topLeftY, THUMB_WIDTH, height);
  }

  @Override
  protected JButton createDecreaseButton(int orientation) {
    return createZeroButton();
  }

  @Override
  protected JButton createIncreaseButton(int orientation) {
    return createZeroButton();
  }

}
